package com.ucb.amae.vault.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays; // Import Arrays for byte[] comparison

public class CipherServiceTest {

    private CipherService cipherService;

    @BeforeEach
    public void setUp() {
        cipherService = new CipherService();
    }

    @Test
    public void testGenerateSalt_Success_Returns16ByteSalt() {
        // When
        byte[] salt = cipherService.generateSalt();

        // Then
        assertNotNull(salt, "El salt no debería ser nulo.");
        assertEquals(16, salt.length, "El salt debería tener una longitud de 16 bytes.");
    }

    @Test
    public void testGenerateSalt_MultipleCalls_ReturnsUniqueSalts() {
        // When
        byte[] salt1 = cipherService.generateSalt();
        byte[] salt2 = cipherService.generateSalt();

        // Then
        assertNotNull(salt1);
        assertNotNull(salt2);
        assertFalse(java.util.Arrays.equals(salt1, salt2), "Dos salts generados no deberían ser iguales.");
    }

    @Test
    public void testGenerateIV_Success_Returns12ByteIV() {
        // When
        byte[] iv = cipherService.generateIV();

        // Then
        assertNotNull(iv, "El IV no debería ser nulo.");
        assertEquals(12, iv.length, "El IV debería tener una longitud de 12 bytes para AES-GCM.");
    }

    @Test
    public void testGenerateIV_MultipleCalls_ReturnsUniqueIVs() {
        // When
        byte[] iv1 = cipherService.generateIV();
        byte[] iv2 = cipherService.generateIV();

        // Then
        assertNotNull(iv1);
        assertNotNull(iv2);
        assertFalse(java.util.Arrays.equals(iv1, iv2), "Dos IVs generados no deberían ser iguales.");
    }

    // --- Tests for Master Key --- 

    @Test
    public void testGenerateMasterKey_Success_Returns32ByteKey() {
        // When
        byte[] masterKey = cipherService.generateMasterKey();

        // Then
        assertNotNull(masterKey, "La clave maestra no debería ser nula.");
        assertEquals(32, masterKey.length, "La clave maestra debería tener una longitud de 32 bytes (AES-256).");
    }

    @Test
    public void testGenerateMasterKey_MultipleCalls_ReturnsUniqueKeys() {
        // When
        byte[] masterKey1 = cipherService.generateMasterKey();
        byte[] masterKey2 = cipherService.generateMasterKey();

        // Then
        assertNotNull(masterKey1);
        assertNotNull(masterKey2);
        assertFalse(Arrays.equals(masterKey1, masterKey2), "Dos claves maestras generadas aleatoriamente no deberían ser iguales.");
    }
}

