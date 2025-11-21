package com.example.ecom_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegRequest(
        @NotNull(message = "username required") @Size(min = 1, max = 45, message = "1-45 characters.") String username,
        @NotNull(message = "password required") @Size(min = 1, max = 45, message = "1-45 characters.") String password,
        @NotNull(message = "email required.") @Email(message = "Please enter a valid email address.") String email) {
}
