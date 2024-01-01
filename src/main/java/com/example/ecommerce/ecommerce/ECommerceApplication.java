package com.example.ecommerce.ecommerce;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class ECommerceApplication {

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

}
