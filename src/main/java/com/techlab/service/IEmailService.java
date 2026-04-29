package com.techlab.service;

public interface IEmailService {
    void sendPasswordResetEmail(String to, String token);
}
