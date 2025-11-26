package com.example.ecom_backend.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public record JwtPrincipal(String username, int id, List<SimpleGrantedAuthority> authorities) {
}
