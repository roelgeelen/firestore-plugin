package com.differentdoors.firestore.repositories;

import com.differentdoors.firestore.models.FirestoreConversation;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FirestoreConversationRepository extends AbstractFirestoreRepository<FirestoreConversation> {
    @Autowired
    private Firestore firestore;


    public void save(String userId, FirestoreConversation model) {
        String documentId = getDocumentId(model);
        ApiFuture<WriteResult> resultApiFuture = firestore.collection("users/"+userId+"/conversations").document(documentId).set(model, SetOptions.merge());
    }

    public void delete(String userId, FirestoreConversation model) {
        String documentId = getDocumentId(model);
        ApiFuture<WriteResult> resultApiFuture = firestore.collection("users/"+userId+"/conversations").document(documentId).delete();

    }

    public List<FirestoreConversation> whereArrayContains(String userId, String field, List<String> value) {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firestore.collection("users/"+userId+"/conversations").whereArrayContainsAny(field, value).get();
        return retrieveListByQuery(querySnapshotApiFuture);
    }

    public List<FirestoreConversation> whereIn(String userId, String field, List<String> value) {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firestore.collection("users/"+userId+"/conversations").whereIn(field, value).get();
        return retrieveListByQuery(querySnapshotApiFuture);
    }

    public List<FirestoreConversation> all(String userId) {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firestore.collection("users/"+userId+"/conversations").get();
        return retrieveListByQuery(querySnapshotApiFuture);
    }


    public Optional<FirestoreConversation> get(String userId, String documentId) {
        DocumentReference documentReference = firestore.collection("users/"+userId+"/conversations").document(documentId);
        return retrieveDocument(documentReference);

    }

}
