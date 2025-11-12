package com.example.ecom_backend.service;

import com.example.ecom_backend.entity.Cart;
import com.example.ecom_backend.entity.CustomUser;
import com.example.ecom_backend.entity.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final BCryptPasswordEncoder encoder;

    private final UserService userService;

    private final CartService cartService;

    public RegistrationService(BCryptPasswordEncoder encoder, UserService userService, CartService cartService) {
        this.encoder = encoder;
        this.userService = userService;
        this.cartService = cartService;
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
}
