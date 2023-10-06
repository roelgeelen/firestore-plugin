package com.differentdoors.firestore.models;

import com.differentdoors.firestore.interfaces.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Topic {
    @DocumentId
    private String id;
    private String description;
    private String name;
    private List<String> roles;
}
