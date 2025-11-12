package com.example.ecom_backend.exception;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime occuredAt, String message, String details) {
}
