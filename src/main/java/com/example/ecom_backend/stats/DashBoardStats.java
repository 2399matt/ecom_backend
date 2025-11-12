package com.example.ecom_backend.stats;

import com.example.ecom_backend.dto.ProductDTO;

import java.util.ArrayList;
import java.util.List;

public class DashBoardStats {

    private long activeUsers;
    private long confirmedOrders;
    private List<ProductDTO> mostActiveProducts;

    public DashBoardStats() {
        mostActiveProducts = new ArrayList<>();
    }

    public DashBoardStats(long activeUsers, List<ProductDTO> mostActiveProducts, long confirmedOrders) {
        this.activeUsers = activeUsers;
        this.mostActiveProducts = mostActiveProducts;
        this.confirmedOrders = confirmedOrders;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(long activeUsers) {
        this.activeUsers = activeUsers;
    }

    public List<ProductDTO> getMostActiveProducts() {
        return mostActiveProducts;
    }

    public void setMostActiveProducts(List<ProductDTO> mostActiveProducts) {
        this.mostActiveProducts = mostActiveProducts;
    }

    public void setConfirmedOrders(long confirmedOrders) {
        this.confirmedOrders = confirmedOrders;
    }
}
