package com.example.order.service;

import com.example.order.dto.OrderDTO;
import com.example.order.entity.Order;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getOrderHistory(int customerId);

}
