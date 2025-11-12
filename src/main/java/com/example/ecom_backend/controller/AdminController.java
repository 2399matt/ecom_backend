package com.example.ecom_backend.controller;

import com.example.ecom_backend.dto.DTOMapper;
import com.example.ecom_backend.dto.OrderDTO;
import com.example.ecom_backend.dto.ProductDTO;
import com.example.ecom_backend.dto.UserDTO;
import com.example.ecom_backend.entity.CustomUser;
import com.example.ecom_backend.entity.Product;
import com.example.ecom_backend.service.OrderService;
import com.example.ecom_backend.service.ProductService;
import com.example.ecom_backend.service.UserService;
import com.example.ecom_backend.stats.DashBoardService;
import com.example.ecom_backend.stats.DashBoardStats;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final DashBoardService dashBoardService;
    private final DTOMapper dtoMapper;

    public AdminController(UserService userService, ProductService productService, OrderService orderService, DashBoardService dashBoardService, DTOMapper dtoMapper) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.dashBoardService = dashBoardService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> fetchAllUsers() {
        return ResponseEntity.ok(dtoMapper.toUserDTO(userService.findAll()));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> fetchAllOrders() {
        return ResponseEntity.ok(dtoMapper.toOrderDTO(orderService.findAll()));
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<DashBoardStats> fetchDashBoardStats() {
        return ResponseEntity.ok(dashBoardService.getDashBoardStats());
    }

    @PostMapping("/create-product")
    public ResponseEntity<Void> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product product = productService.createProduct(productDTO);
        URI location = URI.create("http://localhost:8080/product/" + product.getId());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        Product product = productService.findById(id);
        productService.delete(product);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") int id) {
        CustomUser user = userService.findById(id);
        userService.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-product/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") int id, @Valid @RequestBody ProductDTO productDTO) {
        productService.update(id, productDTO);
        return ResponseEntity.noContent().build();
    }
}
