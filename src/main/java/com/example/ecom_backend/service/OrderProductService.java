package com.example.ecom_backend.service;

import com.example.ecom_backend.dao.OrderProductRepo;
import com.example.ecom_backend.entity.OrderProduct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderProductService {

    private final OrderProductRepo orderProductRepo;

    public OrderProductService(OrderProductRepo orderProductRepo) {
        this.orderProductRepo = orderProductRepo;
    }

    @Transactional
    public void save(OrderProduct orderProduct) {
        orderProductRepo.save(orderProduct);
    }

    @Transactional
    public void delete(OrderProduct orderProduct) {
        orderProductRepo.delete(orderProduct);
    }
}
