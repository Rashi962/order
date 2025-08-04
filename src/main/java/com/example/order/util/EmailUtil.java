package com.example.order.util;

import com.example.order.entity.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender mailSender;

    public void sendOrderConfirmation(String toEmail, List<Order> orders) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setFrom("rashrashi2004@gmail.com");
            helper.setSubject("🛒 Order Placed Successfully!");

            String body = """
                    Dear Customer,

                    ✅ Thank you for shopping with us! Your order has been placed successfully.

                    You can track the status of your order anytime through our app.

                    Regards,
                    E-Commerce Team  
                    ClickNCart
                    """;

            helper.setText(body);

            mailSender.send(message);
            System.out.println("Order confirmation email sent to: " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send order confirmation email");
        }
    }
}
