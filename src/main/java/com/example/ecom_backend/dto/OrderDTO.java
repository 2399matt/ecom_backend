package com.example.ecom_backend.dto;

import java.util.List;

public record OrderDTO(String id, List<OrderProductDTO> products, String firstName, String lastName, String address) {
}
