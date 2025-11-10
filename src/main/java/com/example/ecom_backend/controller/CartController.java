package com.example.ecom_backend.controller;

import com.example.ecom_backend.dto.CartDTO;
import com.example.ecom_backend.dto.DTOMapper;
import com.example.ecom_backend.entity.Cart;
import com.example.ecom_backend.security.EUserDetails;
import com.example.ecom_backend.service.CartProductService;
import com.example.ecom_backend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CartProductService cartProductService;
    private final DTOMapper dtoMapper;

    public CartController(CartService cartService, CartProductService cartProductService, DTOMapper dtoMapper) {
        this.cartService = cartService;
        this.cartProductService = cartProductService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public ResponseEntity<CartDTO> findUserCart(@AuthenticationPrincipal EUserDetails userDetails) {
        CartDTO cart = dtoMapper.toCartDTO(cartService.findUserCart(userDetails.getUser().getId()));
        return ResponseEntity.ok(cart);
    }

    //TODO endpoints to add, remove, and clear items from the user cart.
    //TODO orderController will handle checkout process.
    //TODO Check existing repos to see if we're missing any queries.
}
