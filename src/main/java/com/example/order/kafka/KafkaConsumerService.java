package com.example.order.kafka;

import com.example.order.entity.Order;
import com.example.order.events.CartCheckoutEvent;
import com.example.order.repository.OrderRepository;
import com.example.order.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final OrderRepository orderRepository;
    private final EmailService emailService;

    @KafkaListener(topics = "cart-checkout", groupId="order-group")
    public void consumeCartCheckout(CartCheckoutEvent event) {
        System.out.println("Received CartCheckoutEvent: " + event);

        for (CartCheckoutEvent.CartItemDto item : event.getItems()) {
            Order order = new Order();
            order.setCustomerId(event.getCustomerId());
            order.setProductId(item.getProductId());
            order.setSellerId(item.getSellerId());
            order.setOrderDate(LocalDateTime.now());
            order.setCustomerEmail(event.getCustomerEmail());
            orderRepository.save(order);
        }

        System.out.println("Orders saved for customerId: " + event.getCustomerId());

        try {
            emailService.sendOrderConfirmationEmail(
                    event.getCustomerEmail(),
                    "Order Confirmation",
                    "Thank you! Your order has been placed successfully."
            );
            System.out.println("Confirmation email sent to: " + event.getCustomerEmail());
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
