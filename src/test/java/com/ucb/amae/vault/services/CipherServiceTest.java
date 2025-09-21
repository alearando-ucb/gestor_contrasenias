package com.ucb.amae.vault.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CipherServiceTest {

    private CipherService cipherService;

    @BeforeEach
    public void setUp() {
        cipherService = new CipherService();
    }

    @Test
    public void testGenerateSaltReturnsValidSalt() {
        // When
        byte[] salt = cipherService.generateSalt();

        // Then
        assertNotNull(salt, "El salt no debería ser nulo.");
        assertEquals(16, salt.length, "El salt debería tener una longitud de 16 bytes.");
    }

    @Test
    public void testGenerateSaltValidateUniqueSalts() {
        // When
        byte[] salt1 = cipherService.generateSalt();
        byte[] salt2 = cipherService.generateSalt();

        // Then
        assertNotNull(salt1);
        assertNotNull(salt2);
        assertFalse(java.util.Arrays.equals(salt1, salt2), "Dos salts generados no deberían ser iguales.");
    }
}
