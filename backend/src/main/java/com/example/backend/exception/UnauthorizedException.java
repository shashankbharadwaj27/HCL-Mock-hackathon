package com.example.backend.exception;

public class UnauthorizedException extends Exception {
    UnauthorizedException(String message) {
        super(message);
    }
}
