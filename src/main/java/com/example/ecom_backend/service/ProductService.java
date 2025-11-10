package com.example.ecom_backend.service;

import com.example.ecom_backend.dao.ProductRepo;
import com.example.ecom_backend.entity.Product;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> findAll() {
        return productRepo.findAll();
    }

    public Product findById(int id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public List<Product> findByName(String name) {
        return productRepo.findByName(name);
    }

    @Transactional
    public void save(Product product) {
        productRepo.save(product);
    }

    @Transactional
    public void delete(Product product) {
        productRepo.delete(product);
    }

    @Transactional
    public void update(int id, Product otherProduct) {
        Product product = findById(id);
        product.setName(otherProduct.getName());
        product.setPrice(otherProduct.getPrice());
        product.setStock(otherProduct.getStock());
        productRepo.save(product);
    }
}
