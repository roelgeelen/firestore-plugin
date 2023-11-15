package com.differentdoors.firestore.repositories;

import com.differentdoors.firestore.models.FirestoreConversation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class FirestoreConversationRepository extends AbstractFirestoreRepository<FirestoreConversation> {
    @Autowired
    private Firestore firestore;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();

    public void save(String userId, FirestoreConversation model) {
        String documentId = getDocumentId(model);
        try {
            Object conversation = objectMapper.readValue(objectMapper.writeValueAsString(model), Object.class);
            firestore.collection("users/" + userId + "/conversations").document(documentId).set(conversation, SetOptions.merge());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public DocumentReference create(String userId, FirestoreConversation model) {
        try {
            return firestore.collection("users/" + userId + "/conversations").add(model).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String userId, String id) {
        ApiFuture<WriteResult> resultApiFuture = firestore.collection("users/" + userId + "/conversations").document(id).delete();
    }

    public List<FirestoreConversation> allByYear(String userId, int year) {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firestore.collection("users/" + userId + "/conversations")
                .where(Filter.greaterThanOrEqualTo("createdAt", Timestamp.of(Date.valueOf(year+"-01-01"))))
                .where(Filter.lessThan("createdAt", Timestamp.of(Date.valueOf((year+1)+"-01-01"))))
                .orderBy("createdAt", Query.Direction.DESCENDING).get();
        return retrieveListByQuery(querySnapshotApiFuture);
    }


    public Optional<FirestoreConversation> get(String userId, String documentId) {
        DocumentReference documentReference = firestore.collection("users/" + userId + "/conversations").document(documentId);
        return retrieveDocument(documentReference);

    }

}
