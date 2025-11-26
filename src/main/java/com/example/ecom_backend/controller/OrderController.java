package com.example.ecom_backend.controller;

import com.example.ecom_backend.dto.DTOMapper;
import com.example.ecom_backend.dto.JwtPrincipal;
import com.example.ecom_backend.dto.OrderDTO;
import com.example.ecom_backend.dto.OrderInfoRequest;
import com.example.ecom_backend.entity.Cart;
import com.example.ecom_backend.entity.CustomUser;
import com.example.ecom_backend.entity.Order;
import com.example.ecom_backend.event.OrderCreationEvent;
import com.example.ecom_backend.exception.EmptyCartException;
import com.example.ecom_backend.security.EUserDetails;
import com.example.ecom_backend.service.CartService;
import com.example.ecom_backend.service.OrderService;
import com.example.ecom_backend.service.UserService;
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
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    //TODO: Oops. Only RateLimiter here. Save Resilience4j for the microservices where services need to call eachother. NOT MONOLITH.
    private final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final UserService userService;
    private final DTOMapper dtoMapper;
    private final CartService cartService;
    private final ApplicationEventPublisher eventPublisher;

    public OrderController(OrderService orderService, UserService userService, DTOMapper dtoMapper, CartService cartService, ApplicationEventPublisher eventPublisher) {
        this.orderService = orderService;
        this.userService = userService;
        this.dtoMapper = dtoMapper;
        this.cartService = cartService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getUsersOrders(@AuthenticationPrincipal JwtPrincipal principal) {
        List<OrderDTO> orders = dtoMapper.toOrderDTO(orderService.findUserOrders(principal.id()));
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String id, @AuthenticationPrincipal JwtPrincipal principal) {
        Order order = orderService.findById(id);
        if (order.getUser().getId() == principal.id()) {
            return ResponseEntity.ok(dtoMapper.toOrderDTO(order));
        } else {
            log.warn("Unauthorized access of order: {} by user: {}", id, principal.id());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<Void> createOrder(@Valid @RequestBody OrderInfoRequest request, @AuthenticationPrincipal JwtPrincipal principal) {

        //TODO Could just do away with hibernate mapping and only use id's to avoid this query
        // Leaving it for now.

        CustomUser user = userService.findById(principal.id());
        Cart cart = cartService.findUserCart(principal.id());
        log.info("Fetched cart for user id: {} with # of items: {}", principal.id(), cart.getCartProducts().size());
        if (cart.getCartProducts().isEmpty()) {
            throw new EmptyCartException("You must add items to your cart before checking out!");
        }
        Order order = orderService.save(request, cart.getCartProducts(), user);
        log.info("Created order: {}", order.getId());
        cartService.clearCart(principal.id());
        //TODO finish off event
        eventPublisher.publishEvent(new OrderCreationEvent(order, user));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<Void> checkoutFallBack(OrderInfoRequest request, JwtPrincipal principal, Throwable throwable) {
        log.warn("FALLBACK METHOD INVOKED, ERROR: {}", throwable.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
