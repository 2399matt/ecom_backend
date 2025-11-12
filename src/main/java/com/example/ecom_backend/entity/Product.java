package com.example.ecom_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private float price;

    private int stock;

    @Column(name = "times_added", columnDefinition = "int default 0")
    private int timesAddedToCart;

    public Product() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public synchronized int getTimesAddedToCart() {
        return timesAddedToCart;
    }

    public synchronized void setTimesAddedToCart(int timesAddedToCart) {
        this.timesAddedToCart = timesAddedToCart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
