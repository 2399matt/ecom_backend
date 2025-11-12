package com.example.ecom_backend.stats;

import com.example.ecom_backend.dto.DTOMapper;
import com.example.ecom_backend.dto.ProductDTO;
import com.example.ecom_backend.service.OrderService;
import com.example.ecom_backend.service.ProductService;
import com.example.ecom_backend.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashBoardService {

    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;
    private final DTOMapper dtoMapper;
    private DashBoardStats dashBoardStats;

    public DashBoardService(ProductService productService, UserService userService, OrderService orderService, DTOMapper dtoMapper) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
        this.dtoMapper = dtoMapper;
    }

    @PostConstruct
    private void init() {
        long activeUsers = userService.getActiveUserCount();
        long confirmedOrders = orderService.getConfirmedOrderCount();
        List<ProductDTO> mostActiveProducts = dtoMapper.toProductDTO(productService.findMostActiveProducts());
        dashBoardStats = new DashBoardStats(activeUsers, mostActiveProducts, confirmedOrders);
    }

    @Scheduled(fixedDelay = 1800000)
    private void updateStats() {
        dashBoardStats.setActiveUsers(userService.getActiveUserCount());
        dashBoardStats.setMostActiveProducts(dtoMapper.toProductDTO(productService.findMostActiveProducts()));
        dashBoardStats.setConfirmedOrders(orderService.getConfirmedOrderCount());
    }

    public DashBoardStats getDashBoardStats() {
        return dashBoardStats;
    }
}
