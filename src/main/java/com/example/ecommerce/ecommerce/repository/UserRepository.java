package com.example.ecommerce.ecommerce.repository;

import com.example.ecommerce.ecommerce.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {

    Optional<UserModel> findUserByEmail(String email);
    boolean existsByEmail(String email);
}
