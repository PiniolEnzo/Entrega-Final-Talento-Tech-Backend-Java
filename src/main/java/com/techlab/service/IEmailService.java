package com.techlab.service;

import jakarta.mail.MessagingException;

public interface IEmailService {
    void sendPasswordResetEmail(String to, String token);
}
