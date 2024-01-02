package com.example.ecommerce.ecommerce.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.ecommerce.ecommerce.controller.TokenController;
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
//            check user is already exist
            if (userRepository.existsByEmail(payload.get("email").toString())) {
                throw new Exception("User already exist");
            }
            UserModel userModel = userRepository.insert(new UserModel(payload.get("username").toString(),
                    BCrypt.withDefaults().hashToString(12, payload.get("password").toString().toCharArray()), payload.get("email").toString(),
                    payload.get("address").toString(), payload.get("phone").toString()));
            String jwt = new TokenController().generateJWTToken(userModel);
            if (jwt.trim().isEmpty()){
                throw new Exception("JWT token generation failed");
            }
            return Map.of("status", 200, "message", "User registered successfully", "data", userModel,"token",jwt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Map.of("status", 400, "message", e.getMessage());
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
            String jwt = new TokenController().generateJWTToken(userModel);
            if (jwt.trim().isEmpty()){
                throw new Exception("JWT token generation failed");
            }
            return Map.of("status", 200, "message", "User logged in successfully", "data", userModel,"token",jwt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Map.of("status", 400, "message", e.getMessage());
        }
    }


}
