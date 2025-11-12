package com.example.ecom_backend.service;

import com.example.ecom_backend.dao.CartRepo;
import com.example.ecom_backend.entity.Cart;
import com.example.ecom_backend.entity.CartProduct;
import com.example.ecom_backend.entity.Product;
import com.example.ecom_backend.exception.CartNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepo cartRepo;
    private final CartProductService cartProductService;

    public CartService(CartRepo cartRepo, CartProductService cartProductService) {
        this.cartRepo = cartRepo;
        this.cartProductService = cartProductService;
    }

    public Cart findUserCart(int id) {
        return cartRepo.findUserCart(id)
                .orElseThrow(() -> new CartNotFoundException("Cart for user id: " + id + " not found!"));
    }

    @Transactional
    public void save(Cart cart) {
        cartRepo.save(cart);
    }

    @Transactional
    public void update(Cart cart) {
        cartRepo.save(cart);
    }

    @Transactional
    public void addItem(int userId, Product product) {
        Cart cart = findUserCart(userId);
        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setName(product.getName());
        cartProduct.setPrice(product.getPrice());
        cart.getCartProducts().add(cartProduct);
        cart.setTotal(cart.getTotal() + product.getPrice());
        cartRepo.save(cart);
    }

    public boolean containsItem(int userId, int cartProductId) {
        Cart cart = findUserCart(userId);
        CartProduct cartProduct = cart.getCartProducts().stream().filter(c -> c.getId() == cartProductId).findFirst().orElse(null);
        return cartProduct != null;
    }

    @Transactional
    public void removeItem(int userId, CartProduct cartProduct) {
        Cart cart = findUserCart(userId);
        cart.getCartProducts().removeIf(c -> c.getId() == cartProduct.getId());
        cartProductService.delete(cartProduct);
    }

    @Transactional
    public void clearCart(int userId) {
        Cart cart = findUserCart(userId);
        cart.getCartProducts().clear();
        cart.setTotal(0f);
        cartRepo.save(cart);
    }

    @Transactional
    public void delete(Cart cart) {
        cartRepo.delete(cart);
    }
}
