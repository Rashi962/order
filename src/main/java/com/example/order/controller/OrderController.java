package com.example.order.controller;

import com.example.order.dto.OrderDTO;
import com.example.order.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.order.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService=orderService;
    }

    @GetMapping("/getOrderHistory/{customerId}")
    public List<OrderDTO> getOrderHistory(@PathVariable int customerId) {

        return orderService.getOrderHistory(customerId);
    }

}

