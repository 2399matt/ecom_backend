package com.example.ecom_backend.dao;

import com.example.ecom_backend.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartProduct, Integer> {

}
