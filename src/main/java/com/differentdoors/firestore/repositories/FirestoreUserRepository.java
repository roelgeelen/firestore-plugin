package com.differentdoors.firestore.repositories;

import com.differentdoors.firestore.models.FirestoreUser;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

@Repository
public class FirestoreUserRepository extends AbstractFirestoreRepository<FirestoreUser> {
    protected FirestoreUserRepository(Firestore firestore) {
        super(firestore, "users");
    }
}
