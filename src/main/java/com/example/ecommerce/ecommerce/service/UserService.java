package com.example.ecommerce.ecommerce.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.ecommerce.ecommerce.model.UserModel;
import com.example.ecommerce.ecommerce.repository.UserRepository;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

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
            String jwt = generateJWTToken(userModel);
            if (jwt.trim().isEmpty()){
                throw new Exception("JWT token generation failed");
            }
            return Map.of("status", 200, "message", "User registered successfully", "data", userModel,"token",jwt);
        } catch (Exception e) {
            System.out.println(e.toString());
            return Map.of("status", 400, "message", e.toString());
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
            String jwt = generateJWTToken(userModel);
            if (jwt.trim().isEmpty()){
                throw new Exception("JWT token generation failed");
            }
            return Map.of("status", 200, "message", "User logged in successfully", "data", userModel,"token",jwt);
        } catch (Exception e) {
            System.out.println(e.toString());
            return Map.of("status", 400, "message", e.toString());
        }
    }

    private String generateJWTToken(UserModel userModel)  {
        try {
            // Convert hexadecimal string to bytes
            Dotenv dotenv = Dotenv.load();
            JWSSigner signer = new MACSigner(dotenv.get("JWT_SECRETE").getBytes());
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userModel.getUsername()) // Set the subject
                    .issuer(userModel.getEmail()) // Set the issuer
                    .issueTime(Date.from(Instant.now())) // Set the issue time
                    .expirationTime(Date.from(Instant.now().plusSeconds((3600*(24*31))))) // Set the expiration time
                    .jwtID(userModel.getId()) // Set a unique identifier for the token
                    .build();
            // Create the signed JWT
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            // Apply the signature
            signedJWT.sign(signer);
            return signedJWT.serialize();
        }catch (Exception e){
            Logger logger = Logger.getLogger(UserService.class.getName());
            logger.warning(e.toString());
            return "";
        }
    }
}
