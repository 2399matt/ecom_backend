package com.example.ecom_backend.controller;

import com.example.ecom_backend.dto.RegRequest;
import com.example.ecom_backend.entity.CustomUser;
import com.example.ecom_backend.exception.UsernameTakenException;
import com.example.ecom_backend.security.JwtService;
import com.example.ecom_backend.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("!test")
@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegistrationController(RegistrationService registrationService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.registrationService = registrationService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/add-user")
    public ResponseEntity<Void> addUser(@Valid @RequestBody RegRequest request) {
        CustomUser user = new CustomUser(request.username(), request.password());
        if (registrationService.registerUser(user)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            throw new UsernameTakenException(request.username());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@Valid @RequestBody RegRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        Authentication authentication = authenticationManager.authenticate(authToken);
        if (authentication.isAuthenticated()) {
            String jwt = jwtService.generateToken(request.username());
            return ResponseEntity.ok(jwt);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
