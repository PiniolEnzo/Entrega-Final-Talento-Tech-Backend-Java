package com.techlab.exception;

public class DuplicateCategory extends RuntimeException {
    public DuplicateCategory(String message) {
        super(message);
    }
}
