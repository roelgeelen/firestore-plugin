package com.differentdoors.firestore.helpers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.google.cloud.firestore.DocumentReference;

import java.io.IOException;

public class DocumentReferenceSerializer extends JsonSerializer<DocumentReference> {
    @Override
    public void serialize(DocumentReference documentReference, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(documentReference.getPath());
    }
}
