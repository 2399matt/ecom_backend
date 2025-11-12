package com.example.ecom_backend.service;

import com.example.ecom_backend.dao.ProductRepo;
import com.example.ecom_backend.dto.ProductDTO;
import com.example.ecom_backend.entity.Product;
import com.example.ecom_backend.exception.ProductNotFoundException;
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
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found!"));
    }

    public List<Product> findByName(String name) {
        return productRepo.findByName(name);
    }

    public List<Product> findMostActiveProducts() {
        return productRepo.findMostActiveProducts();
    }

    @Transactional
    public void incrementTimesAdded(int id) {
        productRepo.incrementTimesAdded(id);
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
    public void update(int id, ProductDTO otherProduct) {
        Product product = findById(id);
        product.setName(otherProduct.name());
        product.setPrice(otherProduct.price());
        product.setStock(otherProduct.stock());
        productRepo.save(product);
    }

    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setStock(productDTO.stock());
        return productRepo.save(product);
    }

    @Transactional
    public void deleteAll() {
        productRepo.deleteAll();
    }
}
