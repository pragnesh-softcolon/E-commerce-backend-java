package com.example.ecommerce.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    @Id
    private String id;
    @org.springframework.data.mongodb.core.mapping.Field("username")
    private String username;
    @org.springframework.data.mongodb.core.mapping.Field("password")
    private String password;
    @org.springframework.data.mongodb.core.mapping.Field("email")
    private String email;
    @org.springframework.data.mongodb.core.mapping.Field("address")
    private String address;
    @org.springframework.data.mongodb.core.mapping.Field("phone")
    private String phone;


    public UserModel(String username, String password, String email, String address, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }
}
