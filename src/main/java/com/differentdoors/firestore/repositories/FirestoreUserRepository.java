package com.differentdoors.firestore.repositories;

import com.differentdoors.firestore.models.FirestoreUser;
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


    public void save(FirestoreUser model) {
        String documentId = getDocumentId(model);
        ApiFuture<WriteResult> resultApiFuture = firestore.collection("users").document(documentId).set(model, SetOptions.merge());
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
