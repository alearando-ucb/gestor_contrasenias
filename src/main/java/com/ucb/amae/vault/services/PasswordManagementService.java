package com.ucb.amae.vault.services;

import com.ucb.amae.vault.services.models.PasswordStrength;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordManagementService {

    private static String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String lowercase = "abcdefghijklmnopqrstuvwxyz";
    private static String numbers = "0123456789";
    private static String symbols = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    private static String allCharacters = uppercase + lowercase + numbers + symbols;
    
    private static final SecureRandom random = new SecureRandom();
    private static int minLength = 12;
    private static int maxLength = 20;

    /**
     * Evalúa la fortaleza de una contraseña basándose en un sistema de puntuación.
     *
     * @param password La contraseña a evaluar.
     * @return Un valor de la enumeración PasswordStrength que representa la fortaleza.
     * @throws IllegalArgumentException si la contraseña es nula, vacía o solo contiene espacios en blanco.
     */
    public static PasswordStrength evaluatePasswordStrength(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula, vacía o contener solo espacios en blanco.");
        }

        int score = 0;

        // Criterio 1: Longitud
        if (password.length() >= 12) {
            score += 2;
        } else if (password.length() >= 8) {
            score++;
        }

        // Criterio 2: Variedad de caracteres (se suma 1 por cada tipo presente)
       if (containsAnyChar(password, uppercase)) {
            score++; // Contains uppercase
        }
        
        // Check for lowercase letters
        if (containsAnyChar(password, lowercase)) {
            score++; // Contains lowercase
        }
        
        // Check for numbers
        if (containsAnyChar(password, numbers)) {
            score++; // Contains numbers
        }
        
        // Check for symbols
        if (containsAnyChar(password, symbols)) {
            score++; // Contains special characters
        }

        // Mapeo de puntuación a nivel de fortaleza
        if (score <= 2) {
            return PasswordStrength.MUY_DEBIL;
        } else if (score == 3) {
            return PasswordStrength.DEBIL;
        } else if (score == 4) {
            return PasswordStrength.MODERADA;
        } else if (score == 5) {
            return PasswordStrength.FUERTE;
        } else { // score >= 6
            return PasswordStrength.MUY_FUERTE;
        }
    }

    public static String generateSecurePassword() {

        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        
        List<Character> password = new ArrayList<>();
        
        // Ensure at least one character of each type
        password.add(uppercase.charAt(random.nextInt(uppercase.length())));
        password.add(lowercase.charAt(random.nextInt(lowercase.length())));
        password.add(numbers.charAt(random.nextInt(numbers.length())));
        password.add(symbols.charAt(random.nextInt(symbols.length())));
        
        // Fill the rest with random characters
        for (int i = 4; i < length; i++) {
            password.add(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }
        
        // Shuffle the characters using Collections.shuffle
        Collections.shuffle(password, random);
        
        // Convert List<Character> to String
        StringBuilder result = new StringBuilder(length);
        for (Character c : password) {
            result.append(c);
        }
        
        return result.toString();
    }

    private static boolean containsAnyChar(String text, String charSet) {
        for (char c : text.toCharArray()) {
            if (charSet.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }

}