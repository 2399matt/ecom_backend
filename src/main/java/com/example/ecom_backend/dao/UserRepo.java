package com.example.ecom_backend.dao;

import com.example.ecom_backend.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<CustomUser, Integer> {

    boolean existsByUsername(String username);

    Optional<CustomUser> findByUsername(String username);

}
