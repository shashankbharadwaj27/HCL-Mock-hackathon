package com.example.backend.exception;

public class ResourceNotFoundException extends Exception {
    ResourceNotFoundException(String message) {
        super(message);
    }
}
