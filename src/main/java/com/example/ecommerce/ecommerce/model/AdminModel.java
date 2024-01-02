package com.example.ecommerce.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminModel {
    @Id
    private String id;
    @org.springframework.data.mongodb.core.mapping.Field("username")
    private String username;
    @org.springframework.data.mongodb.core.mapping.Field("password")
    private String password;
    @org.springframework.data.mongodb.core.mapping.Field("email")
    private String email;
    @org.springframework.data.mongodb.core.mapping.Field("role")
    private String role;

    public AdminModel(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
