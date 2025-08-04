package com.example.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="orders")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Order {

    @Id
    @GeneratedValue
    @Column(updatable=false,nullable=false)
    private UUID orderId;

    private int customerId;
    private int productId;
    private int sellerId;
    private String customerEmail;

    private LocalDateTime orderDate;
}
