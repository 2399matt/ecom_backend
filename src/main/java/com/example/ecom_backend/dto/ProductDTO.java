package com.example.ecom_backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductDTO(int id, @NotNull @Size(min = 1, max = 20) String name, @NotNull float price,
                         @NotNull int stock) {

}
