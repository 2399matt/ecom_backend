package com.example.ecom_backend.dao;

import com.example.ecom_backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepo extends JpaRepository<Cart,Integer> {

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartProducts WHERE c.user.id=:id")
    Cart findUserCart(@Param("id") int id);

}
