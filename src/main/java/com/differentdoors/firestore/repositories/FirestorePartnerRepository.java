package com.differentdoors.firestore.repositories;

import com.differentdoors.firestore.models.FirestorePartner;
import com.differentdoors.firestore.models.FirestorePartnerIndex;
import com.differentdoors.firestore.models.FirestorePartnerSummary;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Normalizer;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Repository
public class FirestorePartnerRepository extends AbstractFirestoreRepository<FirestorePartner> {
    private static final Logger logger = LoggerFactory.getLogger(FirestorePartnerRepository.class);

    /** Locatie van het indexdocument met de lijstweergave van alle partners, zie {@link FirestorePartnerIndex}. */
    private static final String INDEX_COLLECTION = "partners_index";
    private static final String INDEX_DOCUMENT = "all";

    @Autowired
    private Firestore firestore;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();

    public List<FirestorePartner> all() {
        return all(null, null, null);
    }

    public List<FirestorePartner> all(List<String> fields) {
        return all(fields, null, null);
    }

    /**
     * Cursor based pagination. Pass the documentId of the last partner from the
     * previous page as {@code startAfter} to fetch the next page. Only the
     * returned documents are read from Firestore, so skipped records are not
     * billed.
     */
    public List<FirestorePartner> all(List<String> fields, Integer size, String startAfter) {
        Query query = firestore.collection("partners");
        if (fields != null && !fields.isEmpty()) {
            // Always keep documentId so the id is available client-side.
            Set<String> projection = new LinkedHashSet<>(fields);
            projection.add("documentId");
            query = query.select(projection.toArray(new String[0]));
        }
        if (size != null && size > 0) {
            query = query.orderBy(FieldPath.documentId());
            if (startAfter != null && !startAfter.isBlank()) {
                query = query.startAfter(startAfter);
            }
            query = query.limit(size);
        }
        return retrieveListByQuery(query.get());
    }

    /**
     * De lijstweergave van alle partners uit het indexdocument: één Firestore-read,
     * ongeacht het aantal partners. Ontbreekt het document (eerste keer, of handmatig
     * verwijderd), dan wordt het uit de collectie opgebouwd.
     */
    public List<FirestorePartnerSummary> summaries() {
        try {
            DocumentSnapshot snapshot = indexReference().get().get();
            if (snapshot.exists()) {
                FirestorePartnerIndex index = snapshot.toObject(FirestorePartnerIndex.class);
                if (index != null && index.getPartners() != null) {
                    return index.getPartners();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Kon partnerindex niet lezen: {}", e.getMessage());
        }
        return rebuildIndex();
    }

    /**
     * Bouwt het indexdocument opnieuw op uit de partnercollectie. Dit leest elke partner
     * (één read per document) en hoort daarom alleen na een wijziging te gebeuren.
     */
    public List<FirestorePartnerSummary> rebuildIndex() {
        List<FirestorePartnerSummary> summaries = all().stream().map(FirestorePartnerSummary::of).toList();
        try {
            Object index = objectMapper.convertValue(new FirestorePartnerIndex(summaries), Map.class);
            indexReference().set(index).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Kon partnerindex niet schrijven: {}", e.getMessage());
        }
        return summaries;
    }

    private DocumentReference indexReference() {
        return firestore.collection(INDEX_COLLECTION).document(INDEX_DOCUMENT);
    }

    public Optional<FirestorePartner> get(String documentId) {
        DocumentReference documentReference = firestore.collection("partners").document(documentId);
        return retrieveDocument(documentReference);
    }

    public void save(FirestorePartner model) {
        String documentId = getDocumentId(model);
        try {
            Object partner = objectMapper.readValue(objectMapper.writeValueAsString(model), Object.class);
            firestore.collection("partners").document(documentId).set(partner, SetOptions.merge()).get();
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        rebuildIndex();
    }

    public DocumentReference create(FirestorePartner model) {
        try {
            CollectionReference partners = firestore.collection("partners");
            String slug = toSlug(model.getDisplay_name());
            DocumentReference documentReference = slug.isEmpty()
                    ? partners.document()
                    : partners.document(uniqueSlug(partners, slug));
            model.setDocumentId(documentReference.getId());
            Object partner = objectMapper.readValue(objectMapper.writeValueAsString(model), Object.class);
            documentReference.set(partner).get();
            rebuildIndex();
            return documentReference;
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String uniqueSlug(CollectionReference partners, String slug) throws InterruptedException, ExecutionException {
        String candidate = slug;
        int suffix = 2;
        while (partners.document(candidate).get().get().exists()) {
            candidate = slug + "-" + suffix++;
        }
        return candidate;
    }

    public void delete(String id) {
        try {
            firestore.collection("partners").document(id).delete().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        rebuildIndex();
    }

    private static String toSlug(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "");
    }
}
