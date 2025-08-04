package com.example.order.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendOrderConfirmationEmail(String to, String subject, String body) throws MessagingException;
}
