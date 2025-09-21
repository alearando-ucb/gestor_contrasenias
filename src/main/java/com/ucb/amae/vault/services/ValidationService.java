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

    public static void validateSaltNotNull(byte[] salt) {
        if (salt == null) {
            throw new IllegalArgumentException("El salt no puede ser nulo.");
        }
    }

    public static void validateSaltNotEmpty(byte[] salt) {
        if (salt.length == 0) {
            throw new IllegalArgumentException("El salt no puede estar vacío.");
        }
    }

    public static void validateSaltLength(byte[] salt, int expectedLength) {
        if (salt.length != expectedLength) {
            throw new IllegalArgumentException("El salt debe tener una longitud de " + expectedLength + " bytes.");
        }
    }

    public static void validateDataNotNull(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos.");
        }
    }

    public static void validateDataNotEmpty(byte[] data) {
        if (data.length == 0) {
            throw new IllegalArgumentException("Los datos no pueden estar vacíos.");
        }
    }

    public static void validateKeyNotNull(byte[] key) {
        if (key == null) {
            throw new IllegalArgumentException("La clave no puede ser nula.");
        }
    }

    public static void validateKeyNotEmpty(byte[] key) {
        if (key.length == 0) {
            throw new IllegalArgumentException("La clave no puede estar vacía.");
        }
    }

    public static void validateKeyLength(byte[] key, int expectedLength) {
        if (key.length != expectedLength) {
            throw new IllegalArgumentException("La clave debe tener una longitud de " + expectedLength + " bytes.");
        }
    }
}
