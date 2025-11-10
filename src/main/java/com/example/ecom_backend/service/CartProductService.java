package com.example.ecom_backend.service;

import com.example.ecom_backend.dao.CartItemRepo;
import com.example.ecom_backend.entity.Cart;
import com.example.ecom_backend.entity.CartProduct;
import com.example.ecom_backend.entity.Product;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartProductService {

    private final CartItemRepo cartItemRepo;

    public CartProductService(CartItemRepo cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    public CartProduct findById(int id) {
        return cartItemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart Product not found!"));
    }

    @Transactional
    public void save(Product product, Cart cart) {
        CartProduct cartProduct = new CartProduct();
        cartProduct.setName(product.getName());
        cartProduct.setPrice(product.getPrice());
        cartProduct.setCart(cart);
        cartItemRepo.save(cartProduct);
    }

    @Transactional
    public void delete(CartProduct cartProduct) {
        cartItemRepo.delete(cartProduct);
    }
}
