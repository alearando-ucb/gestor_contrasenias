package com.ucb.amae.vault.services.exceptions;

public class CipherException extends RuntimeException {
    public CipherException(String message) {
        super(message);
    }

    public CipherException(String message, Throwable cause) {
        super(message, cause);
    }

}
