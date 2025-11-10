package com.example.ecom_backend.dto;

import java.util.List;

public record CartDTO(float total, List<CartProductDTO> products) {
}
