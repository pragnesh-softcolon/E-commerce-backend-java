package com.example.ecommerce.ecommerce.controller;

import com.example.ecommerce.ecommerce.model.UserModel;
import com.example.ecommerce.ecommerce.repository.UserRepository;
import com.example.ecommerce.ecommerce.service.UserService;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class TokenController {
    @Autowired
    UserRepository userRepository;

    public String generateJWTToken(UserModel userModel)  {
        try {
            // Convert hexadecimal string to bytes
            Dotenv dotenv = Dotenv.load();
            JWSSigner signer = new MACSigner(dotenv.get("JWT_SECRETE").getBytes());
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userModel.getUsername()) // Set the subject
                    .issuer(userModel.getEmail()) // Set the issuer
                    .issueTime(Date.from(Instant.now())) // Set the issue time
                    .expirationTime(Date.from(Instant.now().plusSeconds((3600*(24*1))))) // Set the expiration time
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
    private boolean verifyJWTToken(String token, Map<String,Object> payload)  {
        try {
            // Convert hexadecimal string to bytes

            UserModel userModel = userRepository.findUserByEmail(payload.get("email").toString()).orElseThrow(
                    () -> new Exception("User not found")
            );

            Dotenv dotenv = Dotenv.load();
            JWSVerifier verifier = new MACVerifier(dotenv.get("JWT_SECRETE").getBytes());

//            verify token
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (!signedJWT.getJWTClaimsSet().getSubject().equals(userModel.getUsername())){
                throw new Exception("Invalid token");
            }
            if (!signedJWT.getJWTClaimsSet().getIssuer().equals(userModel.getEmail())){
                throw new Exception("Invalid token");
            }
            if (!signedJWT.getJWTClaimsSet().getJWTID().equals(userModel.getId())){
                throw new Exception("Invalid token");
            }
            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(Date.from(Instant.now()))){
                throw new Exception("Token expired");
            }
            if (!signedJWT.verify(verifier)){
                throw new Exception("Invalid token");
            }
            return true;

        }catch (Exception e){
            Logger logger = Logger.getLogger(UserService.class.getName());
            logger.warning(e.getMessage());
            return false;
        }
    }
}
