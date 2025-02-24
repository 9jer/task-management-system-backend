package com.stasevich.taskmanagementsystembackend.user.util;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
