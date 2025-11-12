package com.example.ecom_backend.dao;

import com.example.ecom_backend.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<CustomUser, Integer> {

    boolean existsByUsername(String username);

    Optional<CustomUser> findByUsername(String username);

    @Query("SELECT COUNT(c) FROM CustomUser c")
    long getActiveUserCount();

}
