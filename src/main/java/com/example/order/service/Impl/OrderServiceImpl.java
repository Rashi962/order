package com.example.order.service.Impl;

import com.example.order.dto.OrderDTO;
import com.example.order.entity.Order;
import com.example.order.exception.OrderNotFoundException;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository=orderRepository;
    }

    @Override
    public List<OrderDTO> getOrderHistory(int customerId) {
        List<Order> orders=orderRepository.findByCustomerId(customerId);

        if(orders.isEmpty()) {
            throw new OrderNotFoundException("No orders found for this customer");
        }
        return orders.stream().map(order-> new OrderDTO(
                order.getOrderId(),
                order.getProductId(),
                order.getSellerId(),
                order.getCustomerId(),
                order.getOrderDate()
        )).toList();
    }
    @Override
    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

}
