package com.example.ecom_backend.exception;

import java.util.List;

public record ArgsErrorResponse(String message, List<String> details) {
}
