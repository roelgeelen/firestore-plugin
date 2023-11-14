package com.differentdoors.firestore.repositories;

import com.differentdoors.firestore.models.FirestoreTopic;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FirestoreTopicRepository extends AbstractFirestoreRepository<FirestoreTopic> {
    @Autowired
    private Firestore firestore;

    public List<FirestoreTopic> all() {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firestore.collection("topics").get();
        return retrieveListByQuery(querySnapshotApiFuture);
    }


    public Optional<FirestoreTopic> get(String documentId) {
        DocumentReference documentReference = firestore.collection("topics").document(documentId);
        return retrieveDocument(documentReference);

    }

}
