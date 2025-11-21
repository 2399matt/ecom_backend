package com.example.ecom_backend.event;

import jakarta.validation.constraints.NotNull;

public record ProductAddedEvent(@NotNull int productId) {
}
