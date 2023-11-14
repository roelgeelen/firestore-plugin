package com.differentdoors.firestore.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
public class FirestoreUserSettingsRepository {
    @Autowired
    private Firestore firestore;

    public Map<String, Object> get(String userId, String documentId) {
        DocumentReference documentReference = firestore.collection("users/" + userId + "/settings").document(documentId);

        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();

        try {
            DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();

            if (documentSnapshot.exists()) {
                return documentSnapshot.getData();
            }

        } catch (InterruptedException | ExecutionException e) {
            log.error("Exception occurred retrieving: {}", e.getMessage());
        }

        return Collections.emptyMap();
    }

}
