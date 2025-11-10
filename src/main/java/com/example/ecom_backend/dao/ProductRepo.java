package com.example.ecom_backend.dao;

import com.example.ecom_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {

    @Query("SELECT p FROM Product p WHERE p.name LIKE(LOWER(CONCAT('%', :name, '%')))")
    List<Product> findByName(@Param("term") String name);
}
