package com.differentdoors.firestore.helpers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DocumentReferenceDeserializer extends JsonDeserializer<DocumentReference> {

    @Autowired
    Firestore firestore; // Firestore-instantie

    @Override
    public DocumentReference deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String referencePath = jsonParser.getText();
        return firestore.document(referencePath);
    }
}
