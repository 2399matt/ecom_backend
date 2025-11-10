package com.example.ecom_backend;

import com.example.ecom_backend.entity.CustomUser;
import com.example.ecom_backend.entity.Product;
import com.example.ecom_backend.entity.Role;
import com.example.ecom_backend.service.ProductService;
import com.example.ecom_backend.service.UserService;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class EcomBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomBackendApplication.class, args);
	}

}
