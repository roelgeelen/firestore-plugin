package com.differentdoors.firestore.repositories;

import com.differentdoors.firestore.models.FirestoreUser;
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

@Repository
public class FirestoreUserRepository extends AbstractFirestoreRepository<FirestoreUser> {
    @Autowired
    private Firestore firestore;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();


    public void save(FirestoreUser model) {
        String documentId = getDocumentId(model);
        try {
            Object user = objectMapper.readValue(objectMapper.writeValueAsString(model), Object.class);
            firestore.collection("users").document(documentId).set(user, SetOptions.merge());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public List<FirestoreUser> whereArrayContains(String field, List<String> value) {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firestore.collection("users").whereArrayContainsAny(field, value).get();
        return retrieveListByQuery(querySnapshotApiFuture);
    }

    public List<FirestoreUser> whereIn(String field, List<String> value) {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firestore.collection("users").whereIn(field, value).get();
        return retrieveListByQuery(querySnapshotApiFuture);
    }

    public List<FirestoreUser> all() {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firestore.collection("users").get();
        return retrieveListByQuery(querySnapshotApiFuture);
    }


    public Optional<FirestoreUser> get(String documentId) {
        DocumentReference documentReference = firestore.collection("users").document(documentId);
        return retrieveDocument(documentReference);

    }
}
