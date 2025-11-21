package com.example.ecom_backend;

import com.example.ecom_backend.dto.ProductDTO;
import com.example.ecom_backend.entity.CustomUser;
import com.example.ecom_backend.entity.Product;
import com.example.ecom_backend.entity.Role;
import com.example.ecom_backend.service.ProductService;
import com.example.ecom_backend.service.UserService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {EcomBackendApplication.class})
@Testcontainers
@ActiveProfiles("test")
public class ProductTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySqlContainer = new MySQLContainer<>("mysql:8.0");

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private CustomUser user;

    private Product productOne;

    private Product productTwo;

    @BeforeEach
    public void init() {
        user = new CustomUser("test", bCryptPasswordEncoder.encode("test"));
        user.setEnabled(true);
        user.setRole(Role.USER);
        user.setEmail("123@test.com");
        userService.save(user);

        productOne = new Product();
        productOne.setName("productOne");
        productOne.setStock(10);
        productOne.setPrice(50f);
        productTwo = new Product();
        productTwo.setName("productTwo");
        productTwo.setStock(20);
        productTwo.setPrice(100f);
        productService.save(productOne);
        productService.save(productTwo);
    }

    @AfterEach
    public void destroy() {
        productService.delete(productOne);
        productService.delete(productTwo);
        userService.delete(user);
    }

    @Test
    public void testProductFetchAll() {
        ResponseEntity<ProductDTO[]> response = restTemplate.withBasicAuth("test", "test").getForEntity("/products", ProductDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    public void testProductFetchById() {
        ResponseEntity<ProductDTO> response = restTemplate.withBasicAuth("test", "test").getForEntity("/products/{id}", ProductDTO.class, productOne.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("productOne", response.getBody().name());
    }
}
