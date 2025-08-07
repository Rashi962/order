package com.example.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
//Todo : Why do you have annotation for all args constructor & custom all args constructor? Remove the custom implementation
public class OrderDTO {
    private UUID orderId;
    private int customerId;
    private LocalDateTime orderDate;
    private String customerEmail;
    private double totalPrice;
    private List<OrderItemDTO> items;

    public OrderDTO(UUID orderId, int customerId, String customerEmail, LocalDateTime orderDate, List<OrderItemDTO> items, double totalPrice) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerEmail = customerEmail;
        this.orderDate = orderDate;
        this.items = items;
        this.totalPrice = totalPrice;
    }
}
