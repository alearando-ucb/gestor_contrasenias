package com.ucb.amae.vault.services;

import com.ucb.amae.vault.services.models.PasswordStrength;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordManagementServiceTest {

    // --- Tests for evaluatePasswordStrength ---

    @Test
    void testEvaluatePasswordStrength_NullInput_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordManagementService.evaluatePasswordStrength(null);
        });
    }

    @Test
    void testEvaluatePasswordStrength_EmptyInput_ThrowsException() {
        String password = "";

        assertThrows(IllegalArgumentException.class, () -> {
            PasswordManagementService.evaluatePasswordStrength(password);
        });
    }

    @Test
    void testEvaluatePasswordStrength_WhitespaceInput_ThrowsException() {
        String password = "   ";

        assertThrows(IllegalArgumentException.class, () -> {
            PasswordManagementService.evaluatePasswordStrength(password);
        });
    }

    @Test
    void testEvaluatePasswordStrength_MuyDebil_ReturnsCorrectStrength() {
        String password = "12345";
        PasswordStrength expected = PasswordStrength.MUY_DEBIL;

        PasswordStrength actual = PasswordManagementService.evaluatePasswordStrength(password);

        assertEquals(expected, actual);
    }

    @Test
    void testEvaluatePasswordStrength_Debil_ReturnsCorrectStrength() {
        String password = "password123";
        PasswordStrength expected = PasswordStrength.DEBIL;

        PasswordStrength actual = PasswordManagementService.evaluatePasswordStrength(password);

        assertEquals(expected, actual);
    }

    @Test
    void testEvaluatePasswordStrength_Moderada_ReturnsCorrectStrength() {
        String password = "Password123";
        PasswordStrength expected = PasswordStrength.MODERADA;

        PasswordStrength actual = PasswordManagementService.evaluatePasswordStrength(password);

        assertEquals(expected, actual);
    }

    @Test
    void testEvaluatePasswordStrength_Fuerte_ReturnsCorrectStrength() {
        String password = "P@ssword123";
        PasswordStrength expected = PasswordStrength.FUERTE;

        PasswordStrength actual = PasswordManagementService.evaluatePasswordStrength(password);

        assertEquals(expected, actual);
    }

    @Test
    void testEvaluatePasswordStrength_MuyFuerte_ReturnsCorrectStrength() {
        String password = "MiP@ssw0rdM4sLargo!";
        PasswordStrength expected = PasswordStrength.MUY_FUERTE;

        PasswordStrength actual = PasswordManagementService.evaluatePasswordStrength(password);

        assertEquals(expected, actual);
    }

    @Test
    void testEvaluatePasswordStrength_Boundary8Chars_ReturnsCorrectStrength() {
        String password = "Abcde12!";
        PasswordStrength expected = PasswordStrength.FUERTE;

        PasswordStrength actual = PasswordManagementService.evaluatePasswordStrength(password);

        assertEquals(expected, actual);
    }

    @Test
    void testEvaluatePasswordStrength_Boundary12Chars_ReturnsCorrectStrength() {
        String password = "Abcdefg1234!";
        PasswordStrength expected = PasswordStrength.MUY_FUERTE;

        PasswordStrength actual = PasswordManagementService.evaluatePasswordStrength(password);

        assertEquals(expected, actual);
    }

    @Test
    void testEvaluatePasswordStrength_MissingSpecialChar_ReturnsCorrectStrength() {
        String password = "PasswordLarga123";
        PasswordStrength expected = PasswordStrength.FUERTE;

        PasswordStrength actual = PasswordManagementService.evaluatePasswordStrength(password);

        assertEquals(expected, actual);
    }

    // --- Tests for generateSecurePassword ---

    @Test
    void testGenerateSecurePassword_Success_ContainsAllCharacterTypes() {
        String password = PasswordManagementService.generateSecurePassword();

        assertTrue(Pattern.compile("[a-z]").matcher(password).find(), "Password should contain a lowercase letter");
        assertTrue(Pattern.compile("[A-Z]").matcher(password).find(), "Password should contain an uppercase letter");
        assertTrue(Pattern.compile("[0-9]").matcher(password).find(), "Password should contain a digit");
        assertTrue(Pattern.compile("[^a-zA-Z0-9]").matcher(password).find(), "Password should contain a special character");
    }

    @Test
    void testGenerateSecurePassword_MultipleCalls_ReturnDifferentPasswords() {
        String pass1 = PasswordManagementService.generateSecurePassword();
        String pass2 = PasswordManagementService.generateSecurePassword();

        assertNotEquals(pass1, pass2, "Two generated passwords should not be equal");
    }

    @Test
    void testGenerateSecurePassword_Success_HasLengthWithinRange() {
        String password = PasswordManagementService.generateSecurePassword();

        assertTrue(password.length() >= 12 && password.length() <= 20, "Password length should be between 12 and 20");
    }

    @Test
    void testGenerateSecurePassword_Success_EvaluatesAsMuyFuerte() {
        String password = PasswordManagementService.generateSecurePassword();

        PasswordStrength actualStrength = PasswordManagementService.evaluatePasswordStrength(password);

        assertEquals(PasswordStrength.MUY_FUERTE, actualStrength, "Generated password should be evaluated as MUY_FUERTE");
    }
}
