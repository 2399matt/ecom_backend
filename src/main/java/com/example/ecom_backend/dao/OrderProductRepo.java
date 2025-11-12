package com.example.ecom_backend.dao;

import com.example.ecom_backend.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepo extends JpaRepository<OrderProduct, Integer> {
}
