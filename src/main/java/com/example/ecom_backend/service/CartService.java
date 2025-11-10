package com.example.ecom_backend.service;

import com.example.ecom_backend.dao.CartRepo;
import com.example.ecom_backend.entity.Cart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepo cartRepo;

    public CartService(CartRepo cartRepo) {
        this.cartRepo = cartRepo;
    }

    public Cart findUserCart(int id) {
        return cartRepo.findUserCart(id);
    }

    @Transactional
    public void save(Cart cart) {
        cartRepo.save(cart);
    }

    @Transactional
    public void update(Cart cart) {
        cartRepo.save(cart);
    }
}
