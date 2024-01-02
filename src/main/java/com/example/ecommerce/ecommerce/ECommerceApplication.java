package com.example.ecommerce.ecommerce;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.ecommerce.ecommerce.model.AdminModel;
import com.example.ecommerce.ecommerce.repository.AdminRepository;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class ECommerceApplication {

    private final AdminRepository adminRepository;

    public ECommerceApplication(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ECommerceApplication.class, args);
        Logger LOGGER = Logger.getLogger(ECommerceApplication.class.getName());
        Dotenv dotenv = Dotenv.load();
        LOGGER.info("Server started, listening on port " + dotenv.get("PORT"));
    }

    @GetMapping("/v1")
    public String apiRoot() {
        return "Hello World";
    }

    @PostConstruct // Executed after Spring context initialization
    public void createAdminIfNotExists() {
        Dotenv dotenv = Dotenv.load();
        if (adminRepository.findByUsername("admin").isEmpty()) {
            AdminModel adminUser = new AdminModel(
                    dotenv.get("ADMIN_NAME"),
                    BCrypt.withDefaults().hashToString(12, dotenv.get("ADMIN_PASSWORD").toCharArray()),
                    dotenv.get("ADMIN_EMAIL"),
                    "ADMIN"
            );
            adminRepository.save(adminUser);
        }
    }

}
