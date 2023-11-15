package com.differentdoors.firestore.models;

import com.differentdoors.firestore.helpers.DocumentReferenceDeserializer;
import com.differentdoors.firestore.helpers.DocumentReferenceSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.differentdoors.firestore.interfaces.DocumentId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirestoreConversation {
    @DocumentId
    private String id;
    private String title;
    private String body;
    private String managerComment;
    private String comment;
    private Timestamp createdAt;

    @JsonSerialize(using = DocumentReferenceSerializer.class)
    @JsonDeserialize(using = DocumentReferenceDeserializer.class)
    private DocumentReference createdBy;

    private Boolean isApproved;
    private Boolean isRead;
    private Boolean isPublished;
    private Boolean isConfidential;
}
