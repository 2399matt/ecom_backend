package com.example.ecom_backend.controller;

import com.example.ecom_backend.dto.CartDTO;
import com.example.ecom_backend.dto.DTOMapper;
import com.example.ecom_backend.entity.CartProduct;
import com.example.ecom_backend.entity.Product;
import com.example.ecom_backend.event.ProductAddedEvent;
import com.example.ecom_backend.security.EUserDetails;
import com.example.ecom_backend.service.CartProductService;
import com.example.ecom_backend.service.CartService;
import com.example.ecom_backend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CartProductService cartProductService;
    private final ProductService productService;
    private final DTOMapper dtoMapper;
    private final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final ApplicationEventPublisher eventPublisher;

    public CartController(CartService cartService, CartProductService cartProductService, ProductService productService, DTOMapper dtoMapper, ApplicationEventPublisher eventPublisher) {
        this.cartService = cartService;
        this.cartProductService = cartProductService;
        this.productService = productService;
        this.eventPublisher = eventPublisher;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public ResponseEntity<CartDTO> findUserCart(@AuthenticationPrincipal EUserDetails userDetails) {
        CartDTO cart = dtoMapper.toCartDTO(cartService.findUserCart(userDetails.getUser().getId()));
        logger.info("Fetched user's cart with id: {}", userDetails.getUser().getId());
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/add-item/{id}")
    public ResponseEntity<Void> addItemToCart(@PathVariable("id") int productId, @AuthenticationPrincipal EUserDetails userDetails) {
        Product product = productService.findById(productId);
        cartService.addItem(userDetails.getUser().getId(), product);
        eventPublisher.publishEvent(new ProductAddedEvent(product.getId()));
        logger.info("Added product with id {} for user with id {}", productId, userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/remove-item/{id}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable("id") int cartProductId, @AuthenticationPrincipal EUserDetails userDetails) {
        if (cartService.containsItem(userDetails.getUser().getId(), cartProductId)) {
            CartProduct cartProduct = cartProductService.findById(cartProductId);
            logger.info("Removing Cart product with id: {} from user with id: {}", cartProductId, userDetails.getUser().getId());
            cartService.removeItem(userDetails.getUser().getId(), cartProduct);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @PutMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal EUserDetails userDetails) {
        logger.info("Clearing cart for user with id: {}", userDetails.getUser().getId());
        cartService.clearCart(userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //TODO: Create Order entity and controller. /checkout endpoint will handle order creation.
    // If successful, trigger an email event for confirmation and look into Stripe.
}
