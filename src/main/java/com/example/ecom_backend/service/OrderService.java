package com.example.ecom_backend.service;

import com.example.ecom_backend.dao.OrderRepo;
import com.example.ecom_backend.dto.OrderInfoRequest;
import com.example.ecom_backend.entity.CartProduct;
import com.example.ecom_backend.entity.CustomUser;
import com.example.ecom_backend.entity.Order;
import com.example.ecom_backend.entity.OrderProduct;
import com.example.ecom_backend.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepo orderRepo;

    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public List<Order> findUserOrders(int id) {
        return orderRepo.findUserOrders(id);
    }

    public Order findById(String id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found!"));
    }

    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    @Transactional
    public void delete(Order order) {
        orderRepo.delete(order);
    }

    @Transactional
    public Order save(OrderInfoRequest request, List<CartProduct> cartProducts, CustomUser user) {
        Order order = new Order();
        order.setId(generateOrderNumber());
        order.setUser(user);
        order.setFirstName(request.firstName());
        order.setLastName(request.lastName());
        order.setAddress(request.address());
        for(CartProduct cartProduct : cartProducts) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setName(cartProduct.getName());
            orderProduct.setPrice(cartProduct.getPrice());
            order.setTotal(order.getTotal() + orderProduct.getPrice());
            order.addItemToOrder(orderProduct);
        }
        //TODO: payment stuff... (would confirm order after verifying)
        order.setConfirmed(true);
        return orderRepo.save(order);
    }

    public long getConfirmedOrderCount() {
        return orderRepo.getConfirmedOrderCount();
    }

    private String generateOrderNumber() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").substring(0, 8);
    }
}
