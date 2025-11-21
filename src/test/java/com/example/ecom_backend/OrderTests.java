package com.example.ecom_backend;

import com.example.ecom_backend.dto.OrderDTO;
import com.example.ecom_backend.dto.OrderInfoRequest;
import com.example.ecom_backend.entity.*;
import com.example.ecom_backend.service.CartService;
import com.example.ecom_backend.service.OrderService;
import com.example.ecom_backend.service.ProductService;
import com.example.ecom_backend.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {EcomBackendApplication.class})
@Testcontainers
@ActiveProfiles("test")
public class OrderTests {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySqlContainer = new  MySQLContainer<>("mysql:8.0");

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    private CustomUser user;
    private Cart cart;
    private Product product;

    @BeforeEach
    public void init() {
        user = new CustomUser();
        user.setUsername("test");
        user.setPassword(bCryptPasswordEncoder.encode("test"));
        user.setEnabled(true);
        user.setEmail("123@test.com");
        user.setRole(Role.USER);
        userService.save(user);
        cart = new Cart();
        cart.setUser(user);
        cartService.save(cart);
        product = new Product();
        product.setName("product_one");
        product.setPrice(5.0f);
        product.setStock(10);
        productService.save(product);
        cartService.addItem(user.getId(), product);
    }

    @AfterEach
    public void destroy() {
        user = userService.findByUsername("test");
        cart = cartService.findUserCart(user.getId());
        cartService.delete(cart);
        orderService.deleteAll();
        userService.delete(user);
        productService.deleteAll();
    }

    @Test
    public void testOrderCreation() {
        Cart cart = cartService.findUserCart(user.getId());
        assertFalse(cart.getCartProducts().isEmpty());
        OrderInfoRequest request = new OrderInfoRequest("test", "test", "test street");
        HttpEntity<OrderInfoRequest> requestEntity = new HttpEntity<>(request);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("test", "test").exchange("/orders/checkout", HttpMethod.POST, requestEntity, Void.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ResponseEntity<OrderDTO[]> orderResponse = restTemplate.withBasicAuth("test", "test").getForEntity("/orders", OrderDTO[].class);
        assertEquals(HttpStatus.OK, orderResponse.getStatusCode());
        assertEquals(1,  orderResponse.getBody().length);
        assertEquals("test", orderResponse.getBody()[0].firstName());
    }

}
