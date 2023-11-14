package com.differentdoors.firestore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.differentdoors.firestore.interfaces.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirestoreUser {
    private String id;
    private String name;
    @DocumentId
    private String email;
    private String manager;
    private List<String> tokens;
    private String last_login;
    private List<String> additional_managers = Collections.emptyList();
}
