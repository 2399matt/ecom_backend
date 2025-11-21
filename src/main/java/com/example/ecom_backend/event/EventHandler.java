package com.example.ecom_backend.event;

import com.example.ecom_backend.entity.Order;
import com.example.ecom_backend.entity.OrderProduct;
import com.example.ecom_backend.service.ProductService;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class EventHandler {

    private final ProductService productService;
    private final EmailSender emailSender;

    public EventHandler(ProductService productService, EmailSender emailSender) {
        this.productService = productService;
        this.emailSender = emailSender;
    }

    @EventListener(ProductAddedEvent.class)
    public void handleProductAddedEvent(ProductAddedEvent event) {
        productService.incrementTimesAdded(event.productId());
    }

    @EventListener(OrderCreationEvent.class)
    public void handleOrderConfirmation(OrderCreationEvent event) {
        StringBuilder sb = new StringBuilder();
        Order order = event.order();
        sb.append("Order Number: ").append(order.getId()).append("\n\n");
        sb.append("Total: ").append(order.getTotal()).append("\n\n");
        sb.append("Items: ").append("\n");
        for (OrderProduct product : order.getOrderProducts()) {
            sb.append(product.getName()).append(": $").append(product.getPrice()).append("\n");
        }
        emailSender.sendEmail(event.user().getEmail(), "Order Placed!", sb.toString());

    }
}
