package com.ucb.amae.vault.services;

import com.ucb.amae.vault.services.models.PasswordStrength;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordManagementServiceTest {

    private PasswordManagementService passwordManagementService;

    @BeforeEach
    void setUp() {
        passwordManagementService = new PasswordManagementService();
    }

    // 1. Pruebas de Errores (Error Path)
    @Test
    @DisplayName("Lanza IllegalArgumentException para password nulo")
    void lanzaExcepcionConPasswordNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            passwordManagementService.evaluatePasswordStrength(null);
        });
    }

    @Test
    @DisplayName("Lanza IllegalArgumentException para password vacío")
    void lanzaExcepcionConPasswordVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            passwordManagementService.evaluatePasswordStrength("");
        });
    }

    @Test
    @DisplayName("Lanza IllegalArgumentException para password con solo espacios")
    void lanzaExcepcionConPasswordSoloEspacios() {
        assertThrows(IllegalArgumentException.class, () -> {
            passwordManagementService.evaluatePasswordStrength("   ");
        });
    }

    // 2. Pruebas de Fortaleza (Happy Path)
    @Test
    @DisplayName("Evalúa una contraseña como MUY_DEBIL")
    void evaluaPasswordComoMuyDebil() {
        assertEquals(PasswordStrength.MUY_DEBIL, passwordManagementService.evaluatePasswordStrength("12345"));
    }

    @Test
    @DisplayName("Evalúa una contraseña como DEBIL")
    void evaluaPasswordComoDebil() {
        assertEquals(PasswordStrength.DEBIL, passwordManagementService.evaluatePasswordStrength("password123"));
    }

    @Test
    @DisplayName("Evalúa una contraseña como MODERADA")
    void evaluaPasswordComoModerada() {
        assertEquals(PasswordStrength.MODERADA, passwordManagementService.evaluatePasswordStrength("Password123"));
    }

    @Test
    @DisplayName("Evalúa una contraseña como FUERTE")
    void evaluaPasswordComoFuerte() {
        // Longitud >= 8 (1), Mayus (1), Minus (1), Numeros (1), Especial (1) = 5
        assertEquals(PasswordStrength.FUERTE, passwordManagementService.evaluatePasswordStrength("P@ssword123"));
    }

    @Test
    @DisplayName("Evalúa una contraseña como MUY_FUERTE")
    void evaluaPasswordComoMuyFuerte() {
        // Longitud >= 12 (2), Mayus (1), Minus (1), Numeros (1), Especial (1) = 6
        assertEquals(PasswordStrength.MUY_FUERTE, passwordManagementService.evaluatePasswordStrength("MiP@ssw0rdM4sLargo!"));
    }

    // 3. Pruebas de Límite (Edge Cases)
    @Test
    @DisplayName("Evalúa correctamente una contraseña de exactamente 8 caracteres")
    void evaluaPasswordEnLimiteDe8Chars() {
        // Longitud = 8 (1), Mayus (1), Minus (1), Numeros(1), Especial (1) = 5 (Fuerte)
        assertEquals(PasswordStrength.FUERTE, passwordManagementService.evaluatePasswordStrength("Abcde12!"));
    }

    @Test
    @DisplayName("Evalúa correctamente una contraseña de exactamente 12 caracteres")
    void evaluaPasswordEnLimiteDe12Chars() {
        // Longitud = 12 (2), Mayus (1), Minus (1), Numeros (1), Especial (1) = 6 (Muy Fuerte)
        assertEquals(PasswordStrength.MUY_FUERTE, passwordManagementService.evaluatePasswordStrength("Abcdefg1234!"));
    }

    @Test
    @DisplayName("Evalúa correctamente una contraseña sin caracteres especiales")
    void evaluaPasswordSinUnTipoDeCaracter() {
        // Longitud >= 12 (2), Mayus (1), Minus (1), Numeros (1) = 5 (Fuerte)
        assertEquals(PasswordStrength.FUERTE, passwordManagementService.evaluatePasswordStrength("PasswordLarga123"));
    }
}
