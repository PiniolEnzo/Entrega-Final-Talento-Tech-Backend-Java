package com.techlab.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String productName) {
        super("Stock insuficiente para: " + productName);
    }
}
