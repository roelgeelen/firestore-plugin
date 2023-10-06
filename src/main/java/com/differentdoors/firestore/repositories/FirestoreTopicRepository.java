package com.differentdoors.firestore.repositories;

import com.differentdoors.firestore.models.FirestoreTopic;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

@Repository
public class FirestoreTopicRepository extends AbstractFirestoreRepository<FirestoreTopic> {
    protected FirestoreTopicRepository(Firestore firestore) {
        super(firestore, "topics");
    }
}
