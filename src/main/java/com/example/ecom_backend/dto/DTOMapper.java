package com.example.ecom_backend.dto;

import com.example.ecom_backend.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DTOMapper {

    public CartDTO toCartDTO(Cart cart) {
        return new CartDTO(cart.getTotal(),
                cart.getCartProducts()
                        .stream()
                        .map(item -> toCartProductDTO(item))
                        .toList());
    }

    public CartProductDTO toCartProductDTO(CartProduct cartProduct) {
        return new CartProductDTO(cartProduct.getId(), cartProduct.getName(), cartProduct.getPrice());
    }

    public UserDTO toUserDTO(CustomUser user) {
        return new UserDTO(user.getId(), user.getUsername());
    }

    public List<UserDTO> toUserDTO(List<CustomUser> users) {
        return users
                .stream()
                .map(user -> toUserDTO(user))
                .toList();
    }

    public ProductDTO toProductDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getStock());
    }

    public List<ProductDTO> toProductDTO(List<Product> products) {
        return products
                .stream()
                .map(product -> toProductDTO(product))
                .toList();
    }

    public OrderProductDTO toOrderProductDTO(OrderProduct orderProduct) {
        return new OrderProductDTO(orderProduct.getId(), orderProduct.getName(), orderProduct.getPrice());
    }

    public OrderDTO toOrderDTO(Order order) {
        return new OrderDTO(order.getId(),
                order.getOrderProducts().stream().map(p -> toOrderProductDTO(p)).toList(),
                order.getFirstName(),
                order.getLastName(),
                order.getAddress());
    }

    public List<OrderDTO> toOrderDTO(List<Order> orders) {
        return orders.stream()
                .map(order -> toOrderDTO(order))
                .toList();
    }
}
