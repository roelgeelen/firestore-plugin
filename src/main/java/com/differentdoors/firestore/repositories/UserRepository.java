package com.differentdoors.firestore.repositories;

import com.differentdoors.firestore.models.User;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends AbstractFirestoreRepository<User> {
    protected UserRepository(Firestore firestore) {
        super(firestore, "users");
    }
}
