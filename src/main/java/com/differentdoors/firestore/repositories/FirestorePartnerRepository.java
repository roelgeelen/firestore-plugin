package com.differentdoors.firestore.repositories;

import com.differentdoors.firestore.models.FirestorePartner;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class FirestorePartnerRepository extends AbstractFirestoreRepository<FirestorePartner> {
    @Autowired
    private Firestore firestore;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();

    public List<FirestorePartner> all() {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firestore.collection("partners").get();
        return retrieveListByQuery(querySnapshotApiFuture);
    }

    public Optional<FirestorePartner> get(String documentId) {
        DocumentReference documentReference = firestore.collection("partners").document(documentId);
        return retrieveDocument(documentReference);
    }

    public void save(FirestorePartner model) {
        String documentId = getDocumentId(model);
        try {
            Object partner = objectMapper.readValue(objectMapper.writeValueAsString(model), Object.class);
            firestore.collection("partners").document(documentId).set(partner, SetOptions.merge());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public DocumentReference create(FirestorePartner model) {
        try {
            return firestore.collection("partners").add(model).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String id) {
        firestore.collection("partners").document(id).delete();
    }
}
