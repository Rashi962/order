package com.example.order.kafka;

import com.example.order.entity.Order;
import com.example.order.entity.OrderItem;
import com.example.order.events.CartCheckoutEvent;
import com.example.order.events.EmailFailureEvent;
import com.example.order.repository.OrderRepository;
import com.example.order.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final OrderRepository orderRepository;
    private final EmailService emailService;
    private final KafkaTemplate<String, EmailFailureEvent> kafkaTemplate;

    private boolean isEmailValid(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    /*
    Todo :
     1 : Why is group id mentioned here if it is already mentioned in Kafka Config?
     2 : Instead of System.out.println, use Slf4j for Logging
    */
    @KafkaListener(topics = "cart-checkout", groupId = "order-group")
    public void consumeCartCheckout(CartCheckoutEvent event) {
        System.out.println("🛒 Received CartCheckoutEvent: " + event);

        Order order = new Order();
        order.setCustomerId(event.getCustomerId());
        order.setCustomerEmail(event.getCustomerEmail());
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartCheckoutEvent.CartItemDto item : event.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setProductName(item.getProductName());
            orderItem.setProductPrice(item.getProductPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setSellerId(item.getSellerId());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        System.out.println("✅ Order saved for customerId: " + event.getCustomerId());


        StringBuilder body = new StringBuilder();
        body.append("Dear Customer,<br><br>")
                .append("Thank you for shopping with us!<br>")
                .append("Here are your order details:<br><br>");

        //Todo : In email, insert a redirection link to order history page
        for (OrderItem item : savedOrder.getItems()) {
            body.append("📦 <b>Product Name:</b> ").append(item.getProductName()).append("<br>")
                    .append("🔢 <b>Quantity:</b> ").append(item.getQuantity()).append("<br>")
                    .append("💰 <b>Price:</b> ₹").append(item.getProductPrice()).append("<br><br>");
        }

        body.append("🧾 <b>Total Paid:</b> ₹").append(event.getTotalPrice()).append("<br><br>")
                .append("Happy Shopping! 🛍️<br>")
                .append("<b>Team ClickNCart</b>");

        if (!isEmailValid(event.getCustomerEmail())) {
            System.err.println("⚠️ Invalid email format: " + event.getCustomerEmail());
        }

        try {
            emailService.sendOrderConfirmation(
                    event.getCustomerEmail(),
                    "Order Confirmation - ClickNCart",
                    body.toString()
            );
            System.out.println("📨 Confirmation email sent to: " + event.getCustomerEmail());
        } catch (MessagingException e) {
            System.err.println(" Failed to send email: " + e.getMessage());

            EmailFailureEvent failureEvent = new EmailFailureEvent(
                    event.getCustomerEmail(),
                    "Order Confirmation - ClickNCart",
                    body.toString(),
                    e.getMessage()
            );

            kafkaTemplate.send("email-failed", failureEvent);
        }
    }

    @KafkaListener(topics = "email-failed", groupId = "order-group")
    public void retryFailedEmail(EmailFailureEvent event) {
        try {
            emailService.sendOrderConfirmation(
                    event.getToEmail(),
                    event.getSubject(),
                    event.getHtmlBody()
            );
            System.out.println(">>>>>> Retried email sent successfully to: " + event.getToEmail());
        } catch (MessagingException e) {
            System.err.println("xxxxx Retry failed for email: " + event.getToEmail());

        }
    }
}
