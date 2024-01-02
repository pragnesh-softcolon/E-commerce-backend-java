package com.example.ecommerce.ecommerce.repository;

import com.example.ecommerce.ecommerce.model.AdminModel;
import com.example.ecommerce.ecommerce.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface AdminRepository extends MongoRepository<AdminModel, String> {

    Optional<AdminModel> findByUsername(String username);

}
