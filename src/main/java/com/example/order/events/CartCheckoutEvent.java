package com.example.order.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartCheckoutEvent {

    private int customerId;
    private String customerEmail;
    private List<CartItemDto> items;
    private double totalPrice;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemDto {
        private int productId;
        private String productName;
        private int quantity;
        private double productPrice;
        private int sellerId;
        private double totalPrice;
    }
}
