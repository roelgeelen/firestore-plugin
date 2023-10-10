package com.differentdoors.firestore.models;

import com.differentdoors.firestore.interfaces.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FirestoreConversation {
    @DocumentId
    private String id;
    private String body;
    private String comment;
    private String createdAt;
    private Boolean isApproved;
    private Boolean isRead;
}
