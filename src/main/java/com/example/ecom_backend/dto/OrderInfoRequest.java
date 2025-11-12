package com.example.ecom_backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrderInfoRequest(@NotNull(message = "first name required.") @Size(min = 1, max = 50) String firstName,
                               @NotNull(message = "last name required.") @Size(min = 1, max = 50) String lastName,
                               @NotNull(message = "address required.") @Size(min = 1, max = 50) String address) {
}
