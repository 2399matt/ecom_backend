package com.example.ecom_backend.controller;

import com.example.ecom_backend.dto.DTOMapper;
import com.example.ecom_backend.dto.OrderDTO;
import com.example.ecom_backend.dto.OrderInfoRequest;
import com.example.ecom_backend.entity.Cart;
import com.example.ecom_backend.entity.CustomUser;
import com.example.ecom_backend.entity.Order;
import com.example.ecom_backend.exception.EmptyCartException;
import com.example.ecom_backend.security.EUserDetails;
import com.example.ecom_backend.service.CartService;
import com.example.ecom_backend.service.OrderService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final DTOMapper dtoMapper;
    private final CartService cartService;
    private final ApplicationEventPublisher eventPublisher;

    public OrderController(OrderService orderService, DTOMapper dtoMapper, CartService cartService, ApplicationEventPublisher eventPublisher) {
        this.orderService = orderService;
        this.dtoMapper = dtoMapper;
        this.cartService = cartService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String id, @AuthenticationPrincipal EUserDetails eUserDetails) {
        Order order = orderService.findById(id);
        if(order.getUser().getId() == eUserDetails.getUser().getId()) {
            return ResponseEntity.ok(dtoMapper.toOrderDTO(order));
        } else {
            log.warn("Unauthorized access of order: {} by user: {}", id, eUserDetails.getUser().getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Retry(name = "checkout-api", fallbackMethod = "checkoutFallBack")
    @Bulkhead(name = "checkout-api", fallbackMethod = "checkoutFallBack")
    @PostMapping("/checkout")
    public ResponseEntity<Void> createOrder(@Valid @RequestBody OrderInfoRequest request, @AuthenticationPrincipal EUserDetails userDetails) {
        CustomUser user = userDetails.getUser();
        Cart cart = cartService.findUserCart(user.getId());
        if(cart.getCartProducts().isEmpty()) {
            throw new EmptyCartException("You must add items to your cart before checking out!");
        }
        Order order = orderService.save(request, cart.getCartProducts(), user);
        cartService.clearCart(user.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<Void> checkoutFallBack(OrderInfoRequest request, EUserDetails userDetails, Throwable throwable) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
