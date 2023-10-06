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
public class User {
    @DocumentId
    private String id;
    private String name;
    private String email;
    private List<String> tokens;
    private String last_login;
    private List<String> additional_managers;
}
