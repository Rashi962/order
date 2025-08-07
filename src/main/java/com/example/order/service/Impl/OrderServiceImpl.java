package com.example.order.service.Impl;

import com.example.order.dto.OrderDTO;
import com.example.order.dto.OrderItemDTO;
import com.example.order.entity.Order;
import com.example.order.entity.OrderItem;
import com.example.order.exception.OrderNotFoundException;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderDTO> getOrderHistory(int customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId, Sort.by(Sort.Direction.DESC,"orderDate"));

        if (orders.isEmpty()) {
            throw new OrderNotFoundException("No orders found for this customer");
        }

        return orders.stream().map(order -> {
            List<OrderItemDTO> itemDTOs = order.getItems().stream().map(item ->
                    new OrderItemDTO(
                            item.getProductId(),
                            item.getProductName(),
                            item.getQuantity(),
                            item.getProductPrice(),
                            item.getTotalPrice(),
                            item.getSellerId()
                    )
            ).toList();

            //Todo : store total price in order table, don't calculate here
            double totalPrice=order.getItems().stream()
                    .mapToDouble(OrderItem::getTotalPrice)
                    .sum();

            return new OrderDTO(
                    order.getOrderId(),
                    order.getCustomerId(),
                    order.getCustomerEmail(),
                    order.getOrderDate(),
                    itemDTOs,
                    totalPrice
            );
        }).toList();
    }

}
