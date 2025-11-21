package com.example.ecom_backend.event;

import com.example.ecom_backend.entity.CustomUser;
import com.example.ecom_backend.entity.Order;
import jakarta.validation.constraints.NotNull;

public record OrderCreationEvent(@NotNull Order order, @NotNull CustomUser user) {
}
