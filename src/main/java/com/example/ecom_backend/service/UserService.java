package com.example.ecom_backend.service;

import com.example.ecom_backend.dao.UserRepo;
import com.example.ecom_backend.entity.CustomUser;
import com.example.ecom_backend.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<CustomUser> findAll() {
        return userRepo.findAll();
    }

    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    public CustomUser findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found!"));
    }

    public CustomUser findById(int id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found!"));
    }

    public long getActiveUserCount() {
        return userRepo.getActiveUserCount();
    }

    @Transactional
    public void save(CustomUser user) {
        userRepo.save(user);
    }

    @Transactional
    public void delete(CustomUser user) {
        userRepo.delete(user);
    }
}
