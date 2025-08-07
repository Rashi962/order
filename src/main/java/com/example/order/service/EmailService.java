package com.example.order.service;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendOrderConfirmation(String toEmail, String subject, String htmlBody) throws MessagingException;

}
