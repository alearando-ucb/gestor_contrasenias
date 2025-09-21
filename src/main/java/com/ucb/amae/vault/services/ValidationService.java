package com.ucb.amae.vault.services;

public class ValidationService {

    public static void validatePasswordNotNull(String password) {
        if (password == null) {
            throw new IllegalArgumentException("La contraseña no puede ser nula.");
        }
    }

    public static void validatePasswordNotEmpty(String password) {
        if (password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía o contener solo espacios en blanco.");
        }
    }
}
