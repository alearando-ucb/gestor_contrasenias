package com.ucb.amae.vault.services;

import com.ucb.amae.vault.services.models.PasswordStrength;
import java.util.regex.Pattern;

public class PasswordManagementService {

    /**
     * Evalúa la fortaleza de una contraseña basándose en un sistema de puntuación.
     *
     * @param password La contraseña a evaluar.
     * @return Un valor de la enumeración PasswordStrength que representa la fortaleza.
     * @throws IllegalArgumentException si la contraseña es nula, vacía o solo contiene espacios en blanco.
     */
    public PasswordStrength evaluatePasswordStrength(String password) {
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
        if (Pattern.compile("[A-Z]").matcher(password).find()) {
            score++; // Contiene mayúsculas
        }
        if (Pattern.compile("[a-z]").matcher(password).find()) {
            score++; // Contiene minúsculas
        }
        if (Pattern.compile("[0-9]").matcher(password).find()) {
            score++; // Contiene números
        }
        if (Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
            score++; // Contiene caracteres especiales
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
}