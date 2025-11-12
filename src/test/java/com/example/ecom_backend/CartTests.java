package com.example.ecom_backend;

import com.example.ecom_backend.dto.CartDTO;
import com.example.ecom_backend.entity.Cart;
import com.example.ecom_backend.entity.CustomUser;
import com.example.ecom_backend.entity.Product;
import com.example.ecom_backend.service.CartProductService;
import com.example.ecom_backend.service.CartService;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {EcomBackendApplication.class})
@Testcontainers
@ActiveProfiles("test")
public class CartTests {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySql = new MySQLContainer<>("mysql:8.0");

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartProductService cartProductService;

    @Autowired
    private TestRestTemplate restTemplate;

    private CustomUser testUser;

    private Cart cart;

    private Product productOne;
    private Product productTwo;

    @BeforeEach
    public void setup() {
        testUser = new CustomUser("test", passwordEncoder.encode("test"));
        testUser.setEnabled(true);
        userService.save(testUser);

        productOne = new Product();
        productTwo = new Product();
        productOne.setName("product_one");
        productOne.setPrice(1.0f);
        productOne.setStock(10);
        productTwo.setName("product_two");
        productTwo.setPrice(2.0f);
        productTwo.setStock(20);
        productService.save(productOne);
        productService.save(productTwo);

        cart = new Cart();
        cart.setUser(testUser);
        cartService.save(cart);
    }

    @AfterEach
    public void tearDown() {
        testUser = userService.findByUsername("test");
        cart = cartService.findUserCart(testUser.getId());
        cartService.delete(cart);
        userService.delete(testUser);
        productService.deleteAll();
    }

    @Test
    public void testFetchCart() {
        ResponseEntity<CartDTO> response = restTemplate.withBasicAuth("test", "test").getForEntity("/cart", CartDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testAddProduct() {
        ResponseEntity<Void> response = restTemplate.withBasicAuth("test", "test").exchange("/cart/add-item/{id}", HttpMethod.PUT, null, Void.class, productOne.getId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        ResponseEntity<CartDTO> cartRes = restTemplate.withBasicAuth("test", "test").getForEntity("/cart", CartDTO.class);
        assertEquals(HttpStatus.OK, cartRes.getStatusCode());
        assertNotNull(cartRes.getBody());
        assertFalse(cartRes.getBody().products().isEmpty());
    }

    @Test
    public void testRemoveProduct() {
        ResponseEntity<Void> response = restTemplate.withBasicAuth("test", "test").exchange("/cart/add-item/{id}", HttpMethod.PUT, null, Void.class, productOne.getId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        restTemplate.withBasicAuth("test", "test").exchange("/cart/remove-item/{id}", HttpMethod.PUT, null, Void.class, productOne.getId());
        ResponseEntity<CartDTO> cartResponse = restTemplate.withBasicAuth("test", "test").getForEntity("/cart", CartDTO.class);
        assertEquals(HttpStatus.OK, cartResponse.getStatusCode());
        assertNotNull(cartResponse.getBody());
        assertEquals(0, cartResponse.getBody().products().size());
    }


}
