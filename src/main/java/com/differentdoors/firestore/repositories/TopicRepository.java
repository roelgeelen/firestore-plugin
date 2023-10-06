package com.differentdoors.firestore.repositories;

import com.differentdoors.firestore.models.Topic;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

@Repository
public class TopicRepository extends AbstractFirestoreRepository<Topic> {
    protected TopicRepository(Firestore firestore) {
        super(firestore, "topics");
    }
}
