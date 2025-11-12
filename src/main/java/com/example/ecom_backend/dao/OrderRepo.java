package com.example.ecom_backend.dao;

import com.example.ecom_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, String> {

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderProducts WHERE o.id=:id")
    Optional<Order> findById(String id);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderProducts WHERE o.user.id=:userId")
    List<Order> findUserOrders(@Param("userId") int userId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.confirmed=true")
    long getConfirmedOrderCount();
}
