package com.example.ecom_backend.event;

import com.example.ecom_backend.service.ProductService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {

    private final ProductService productService;

    public EventHandler(ProductService productService) {
        this.productService = productService;
    }

    @EventListener(ProductAddedEvent.class)
    public void handleProductAddedEvent(ProductAddedEvent event) {
        productService.incrementTimesAdded(event.productId());
    }
}
