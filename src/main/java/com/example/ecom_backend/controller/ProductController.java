package com.example.ecom_backend.controller;

import com.example.ecom_backend.dto.DTOMapper;
import com.example.ecom_backend.dto.ProductDTO;
import com.example.ecom_backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final DTOMapper dtoMapper;

    public ProductController(ProductService productService, DTOMapper dtoMapper) {
        this.productService = productService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> fetchAllProducts() {
        return ResponseEntity.ok(dtoMapper.toProductDTO(productService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> fetchProductById(@PathVariable("id") int id) {
        return ResponseEntity.ok(dtoMapper.toProductDTO(productService.findById(id)));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProductDTO>> fetchProductByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(dtoMapper.toProductDTO(productService.findByName(name)));
    }
}
