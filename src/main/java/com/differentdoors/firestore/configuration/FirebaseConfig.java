package com.differentdoors.firestore.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    @Value("${different_doors.firebase.credential}")
    protected String credentialPath;

    @Value("${different_doors.firebase.database}")
    protected String database;

    @Bean
    public Firestore initialize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(credentialPath);

        assert inputStream != null;
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .setDatabaseUrl(database)
                .build();

        FirebaseApp.initializeApp(options);
        return FirestoreClient.getFirestore();
    }
}
