package com.example.ecom_backend.dto;

import com.example.ecom_backend.entity.Cart;
import com.example.ecom_backend.entity.CartProduct;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    public CartDTO toCartDTO(Cart cart) {
       return new CartDTO(cart.getTotal(),
               cart.getCartProducts().stream().map(item -> toCartProductDTO(item)).toList());
    }

    public CartProductDTO toCartProductDTO(CartProduct cartProduct) {
        return new CartProductDTO(cartProduct.getName(), cartProduct.getPrice());
    }
}
