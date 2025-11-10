package com.example.ecom_backend.exception;

public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException(String username) {
        super(username + " is already in use!");
    }
}
