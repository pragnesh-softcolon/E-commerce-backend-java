package com.example.ecommerce.ecommerce.controller;

import com.example.ecommerce.ecommerce.model.UserModel;
import com.example.ecommerce.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, Object> payload) {

        try {
            if (!payload.containsKey("username") || !payload.containsKey("password")
                    || !payload.containsKey("address") || !payload.containsKey("phone")) {
                throw new Exception("Required fields are missing");
            }
            if (payload.get("username").toString().isEmpty() || payload.get("password").toString().isEmpty()
                    || payload.get("address").toString().isEmpty() || payload.get("phone").toString().isEmpty()) {
                throw new Exception("Required data are missing");
            }
            if (payload.get("phone").toString().length() != 10) {
                throw new Exception("Phone number should be 10 digits");
            }
            if (payload.get("password").toString().length() < 5) {
                throw new Exception("Password should be at least 5 characters");
            }

            return new ResponseEntity<>(userService.registerUser(payload), HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", 400);
            map.put("message", e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> payload) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!payload.containsKey("email") || !payload.containsKey("password")) {
                throw new Exception("Required fields are missing");
            }
            if (payload.get("email").toString().isEmpty() || payload.get("password").toString().isEmpty()) {
                throw new Exception("Required data are missing");
            }
            return new ResponseEntity<>(userService.login(payload), HttpStatus.OK);
        }catch (Exception e){
            map.put("status", 400);
            map.put("message", e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
}
