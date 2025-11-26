package com.example.ecom_backend.service;

import com.example.ecom_backend.entity.Cart;
import com.example.ecom_backend.entity.CustomUser;
import com.example.ecom_backend.entity.Role;
import com.example.ecom_backend.security.JwtService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class RegistrationService {

    private final BCryptPasswordEncoder encoder;

    private final UserService userService;

    private final CartService cartService;

    private final JwtService jwtService;

    public RegistrationService(BCryptPasswordEncoder encoder, UserService userService, CartService cartService, JwtService jwtService) {
        this.encoder = encoder;
        this.userService = userService;
        this.cartService = cartService;
        this.jwtService = jwtService;
    }

    public boolean registerUser(CustomUser user) {
        if (userService.existsByUsername(user.getUsername())) {
            return false;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRole(Role.USER); //TODO: Gonna seed admin account.
        userService.save(user);
        Cart cart = new Cart();
        cart.setUser(user);
        cartService.save(cart);
        return true;
    }

    public String generateToken(String username, int id, Collection<? extends GrantedAuthority> authorities) {
        List<String> roles = authorities.stream().map(role -> role.getAuthority()).toList();
        return jwtService.generateToken(username, id, roles);
    }
}
