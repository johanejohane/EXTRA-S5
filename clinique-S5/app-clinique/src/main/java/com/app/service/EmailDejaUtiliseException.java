package com.app.service;

public class EmailDejaUtiliseException extends RuntimeException {
    public EmailDejaUtiliseException(String email) {
        super("Email déjà utilisé : " + email);
    }
}
