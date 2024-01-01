package com.example.ecommerce.ecommerce.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.ecommerce.ecommerce.model.UserModel;
import com.example.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public Map<String, Object> registerUser(Map<String, Object> payload) {
        try {
            UserModel userModel = userRepository.insert(new UserModel(payload.get("username").toString(),
                    BCrypt.withDefaults().hashToString(12, payload.get("password").toString().toCharArray()), payload.get("email").toString(),
                    payload.get("address").toString(), payload.get("phone").toString()));
            return Map.of("Status", 200, "Message", "User registered successfully", "data", userModel);
        } catch (Exception e) {
            return Map.of("Status", 400, "Message", e.toString());
        }
    }

    public Map<String, Object> login(Map<String, Object> payload) {
        try {
            String email = payload.get("email").toString();
            String password = payload.get("password").toString();
            UserModel userModel = userRepository.findUserByEmail(email).orElseThrow(
                    () -> new Exception("User not found")
            );
            if (!(BCrypt.verifyer().verify(password.toCharArray(), userModel.getPassword()).verified)) {
                throw new Exception("Password is incorrect");
            }
            return Map.of("Status", 200, "Message", "User logged in successfully", "data", userModel);
        } catch (Exception e) {
            return Map.of("Status", 400, "Message", e.toString());
        }
    }
}
