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

    // --- Validation Tests for deriveKey(String password, byte[] salt) --- 

    @Test
    public void testDeriveKey_NullPassword_ThrowsException() {
        byte[] salt = cipherService.generateSalt();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.deriveKey(null, salt);
        }, "Debería lanzar IllegalArgumentException para contraseña nula.");
    }

    @Test
    public void testDeriveKey_EmptyPassword_ThrowsException() {
        byte[] salt = cipherService.generateSalt();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.deriveKey("", salt);
        }, "Debería lanzar IllegalArgumentException para contraseña vacía.");
    }

    @Test
    public void testDeriveKey_NullSalt_ThrowsException() {
        String password = "testPassword";
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.deriveKey(password, null);
        }, "Debería lanzar IllegalArgumentException para salt nulo.");
    }

    @Test
    public void testDeriveKey_EmptySalt_ThrowsException() {
        String password = "testPassword";
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.deriveKey(password, new byte[0]);
        }, "Debería lanzar IllegalArgumentException para salt vacío.");
    }

    @Test
    public void testDeriveKey_SaltTooShort_ThrowsException() {
        String password = "testPassword";
        byte[] shortSalt = new byte[7]; // Assuming 8 bytes is minimum, 16 is ideal
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.deriveKey(password, shortSalt);
        }, "Debería lanzar IllegalArgumentException para salt demasiado corto.");
    }

    @Test
    public void testDeriveKey_SaltTooLong_ThrowsException() {
        String password = "testPassword";
        byte[] longSalt = new byte[33]; // Assuming 32 bytes is max, 16 is ideal
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.deriveKey(password, longSalt);
        }, "Debería lanzar IllegalArgumentException para salt demasiado largo.");
    }

    @Test
    public void testDeriveKey_PasswordOnlyWhitespace_ThrowsException() {
        byte[] salt = cipherService.generateSalt();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.deriveKey("   ", salt); // Password with only spaces
        }, "Debería lanzar IllegalArgumentException para contraseña con solo espacios en blanco.");
    }

    // --- New Happy Path Tests for deriveKey(String password, byte[] salt) --- 

    @Test
    public void testDeriveKey_Success_ReturnsValidKey() {
        String password = "testPassword";
        byte[] salt = cipherService.generateSalt(); // Use existing salt generation

        // This will fail until deriveKey is fully implemented
        byte[] derivedKey = cipherService.deriveKey(password, salt);

        assertNotNull(derivedKey, "La clave derivada no debería ser nula.");
        assertEquals(32, derivedKey.length, "La clave derivada debería tener una longitud de 32 bytes (AES-256).");
    }

    @Test
    public void testDeriveKey_SamePasswordSalt_ReturnsConsistentKey() {
        String password = "testPassword";
        byte[] salt = cipherService.generateSalt();

        byte[] derivedKey1 = cipherService.deriveKey(password, salt);
        byte[] derivedKey2 = cipherService.deriveKey(password, salt);

        assertNotNull(derivedKey1);
        assertNotNull(derivedKey2);
        assertTrue(Arrays.equals(derivedKey1, derivedKey2), "La clave derivada debería ser consistente para la misma contraseña y salt.");
    }

    @Test
    public void testDeriveKey_DifferentPassword_ReturnsDifferentKey() {
        String passwordA = "passwordA";
        String passwordB = "passwordB";
        byte[] salt = cipherService.generateSalt();

        byte[] derivedKeyA = cipherService.deriveKey(passwordA, salt);
        byte[] derivedKeyB = cipherService.deriveKey(passwordB, salt);

        assertNotNull(derivedKeyA);
        assertNotNull(derivedKeyB);
        assertFalse(Arrays.equals(derivedKeyA, derivedKeyB), "Claves derivadas diferentes para contraseñas diferentes.");
    }

    @Test
    public void testDeriveKey_DifferentSalt_ReturnsDifferentKey() {
        String password = "testPassword";
        byte[] salt1 = cipherService.generateSalt();
        byte[] salt2 = cipherService.generateSalt();

        byte[] derivedKey1 = cipherService.deriveKey(password, salt1);
        byte[] derivedKey2 = cipherService.deriveKey(password, salt2);

        assertNotNull(derivedKey1);
        assertNotNull(derivedKey2);
        assertFalse(Arrays.equals(derivedKey1, derivedKey2), "Claves derivadas diferentes para salts diferentes.");
    }

    // --- Validation Tests for encrypt(byte[] data, byte[] key, byte[] iv) ---

    @Test
    public void testEncrypt_NullData_ThrowsException() {
        byte[] key = cipherService.generateMasterKey();
        byte[] iv = cipherService.generateIV();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.encrypt(null, key, iv);
        }, "Debería lanzar IllegalArgumentException para datos nulos.");
    }

    @Test
    public void testEncrypt_EmptyData_ThrowsException() {
        byte[] key = cipherService.generateMasterKey();
        byte[] iv = cipherService.generateIV();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.encrypt(new byte[0], key, iv);
        }, "Debería lanzar IllegalArgumentException para datos vacíos.");
    }

    @Test
    public void testEncrypt_NullKey_ThrowsException() {
        byte[] data = "test data".getBytes();
        byte[] iv = cipherService.generateIV();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.encrypt(data, null, iv);
        }, "Debería lanzar IllegalArgumentException para clave nula.");
    }

    @Test
    public void testEncrypt_EmptyKey_ThrowsException() {
        byte[] data = "test data".getBytes();
        byte[] iv = cipherService.generateIV();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.encrypt(data, new byte[0], iv);
        }, "Debería lanzar IllegalArgumentException para clave vacía.");
    }

    @Test
    public void testEncrypt_KeyTooShort_ThrowsException() {
        byte[] data = "test data".getBytes();
        byte[] shortKey = new byte[CipherService.MASTER_KEY_LENGTH - 1];
        byte[] iv = cipherService.generateIV();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.encrypt(data, shortKey, iv);
        }, "Debería lanzar IllegalArgumentException para clave demasiado corta.");
    }

    @Test
    public void testEncrypt_KeyTooLong_ThrowsException() {
        byte[] data = "test data".getBytes();
        byte[] longKey = new byte[CipherService.MASTER_KEY_LENGTH + 1];
        byte[] iv = cipherService.generateIV();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.encrypt(data, longKey, iv);
        }, "Debería lanzar IllegalArgumentException para clave demasiado larga.");
    }

    @Test
    public void testEncrypt_NullIV_ThrowsException() {
        byte[] data = "test data".getBytes();
        byte[] key = cipherService.generateMasterKey();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.encrypt(data, key, null);
        }, "Debería lanzar IllegalArgumentException para IV nulo.");
    }

    @Test
    public void testEncrypt_EmptyIV_ThrowsException() {
        byte[] data = "test data".getBytes();
        byte[] key = cipherService.generateMasterKey();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.encrypt(data, key, new byte[0]);
        }, "Debería lanzar IllegalArgumentException para IV vacío.");
    }

    @Test
    public void testEncrypt_IVTooShort_ThrowsException() {
        byte[] data = "test data".getBytes();
        byte[] key = cipherService.generateMasterKey();
        byte[] shortIV = new byte[CipherService.IV_LENGTH - 1];
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.encrypt(data, key, shortIV);
        }, "Debería lanzar IllegalArgumentException para IV demasiado corto.");
    }

    @Test
    public void testEncrypt_IVTooLong_ThrowsException() {
        byte[] data = "test data".getBytes();
        byte[] key = cipherService.generateMasterKey();
        byte[] longIV = new byte[CipherService.IV_LENGTH + 1];
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.encrypt(data, key, longIV);
        }, "Debería lanzar IllegalArgumentException para IV demasiado largo.");
    }

    @Test
    public void testEncrypt_Success_ReturnsNonNullData() {
        byte[] data = "This is a secret message.".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] key = cipherService.generateMasterKey();
        byte[] iv = cipherService.generateIV();

        byte[] encryptedData = cipherService.encrypt(data, key, iv);

        assertNotNull(encryptedData, "Los datos cifrados no deberían ser nulos.");
    }

    @Test
    public void testEncrypt_Success_ReturnsNonEmptyData() {
        byte[] data = "This is a secret message.".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] key = cipherService.generateMasterKey();
        byte[] iv = cipherService.generateIV();

        byte[] encryptedData = cipherService.encrypt(data, key, iv);

        assertTrue(encryptedData.length > 0, "Los datos cifrados no deberían estar vacíos.");
    }

    @Test
    public void testEncrypt_Success_ReturnsDifferentDataFromOriginal() {
        byte[] data = "This is a secret message.".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] key = cipherService.generateMasterKey();
        byte[] iv = cipherService.generateIV();

        byte[] encryptedData = cipherService.encrypt(data, key, iv);

        assertFalse(Arrays.equals(data, encryptedData), "Los datos cifrados deberían ser diferentes de los datos originales.");
    }

    @Test
    public void testEncrypt_SameInputs_ReturnsConsistentOutput() {
        byte[] data = "Another secret message.".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] key = cipherService.generateMasterKey();
        byte[] iv = cipherService.generateIV();

        byte[] encryptedData1 = cipherService.encrypt(data, key, iv);
        byte[] encryptedData2 = cipherService.encrypt(data, key, iv);

        assertNotNull(encryptedData1);
        assertNotNull(encryptedData2);
        assertTrue(Arrays.equals(encryptedData1, encryptedData2), "Cifrar con los mismos inputs debería producir el mismo output.");
    }

    @Test
    public void testEncrypt_DifferentIV_ReturnsDifferentOutput() {
        byte[] data = "Yet another secret message.".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] key = cipherService.generateMasterKey();
        byte[] iv1 = cipherService.generateIV();
        byte[] iv2 = cipherService.generateIV();

        byte[] encryptedData1 = cipherService.encrypt(data, key, iv1);
        byte[] encryptedData2 = cipherService.encrypt(data, key, iv2);

        assertNotNull(encryptedData1);
        assertNotNull(encryptedData2);
        assertFalse(Arrays.equals(encryptedData1, encryptedData2), "Cifrar con IVs diferentes debería producir outputs diferentes.");
    }

    @Test
    public void testEncrypt_DifferentKey_ReturnsDifferentOutput() {
        byte[] data = "Yet another secret message.".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] iv = cipherService.generateIV();

        byte[] key1 = cipherService.generateMasterKey();
        byte[] key2 = cipherService.generateMasterKey();

        byte[] encryptedData1 = cipherService.encrypt(data, key1, iv);
        byte[] encryptedData2 = cipherService.encrypt(data, key2, iv);

        assertNotNull(encryptedData1);
        assertNotNull(encryptedData2);
        assertFalse(Arrays.equals(encryptedData1, encryptedData2), "Cifrar con claves diferentes debería producir outputs diferentes.");
    }

    // --- Validation Tests for decrypt(byte[] encryptedData, byte[] key, byte[] iv) ---

    @Test
    public void testDecrypt_NullEncryptedData_ThrowsException() {
        byte[] key = cipherService.generateMasterKey();
        byte[] iv = cipherService.generateIV();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.decrypt(null, key, iv);
        }, "Debería lanzar IllegalArgumentException para datos cifrados nulos.");
    }

    @Test
    public void testDecrypt_EmptyEncryptedData_ThrowsException() {
        byte[] key = cipherService.generateMasterKey();
        byte[] iv = cipherService.generateIV();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.decrypt(new byte[0], key, iv);
        }, "Debería lanzar IllegalArgumentException para datos cifrados vacíos.");
    }

    @Test
    public void testDecrypt_NullKey_ThrowsException() {
        byte[] encryptedData = "test data".getBytes(); // Placeholder
        byte[] iv = cipherService.generateIV();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.decrypt(encryptedData, null, iv);
        }, "Debería lanzar IllegalArgumentException para clave nula en descifrado.");
    }

    @Test
    public void testDecrypt_EmptyKey_ThrowsException() {
        byte[] encryptedData = "test data".getBytes(); // Placeholder
        byte[] iv = cipherService.generateIV();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.decrypt(encryptedData, new byte[0], iv);
        }, "Debería lanzar IllegalArgumentException para clave vacía en descifrado.");
    }

    @Test
    public void testDecrypt_KeyTooShort_ThrowsException() {
        byte[] encryptedData = "test data".getBytes(); // Placeholder
        byte[] shortKey = new byte[CipherService.MASTER_KEY_LENGTH - 1];
        byte[] iv = cipherService.generateIV();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.decrypt(encryptedData, shortKey, iv);
        }, "Debería lanzar IllegalArgumentException para clave demasiado corta en descifrado.");
    }

    @Test
    public void testDecrypt_KeyTooLong_ThrowsException() {
        byte[] encryptedData = "test data".getBytes(); // Placeholder
        byte[] longKey = new byte[CipherService.MASTER_KEY_LENGTH + 1];
        byte[] iv = cipherService.generateIV();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.decrypt(encryptedData, longKey, iv);
        }, "Debería lanzar IllegalArgumentException para clave demasiado larga en descifrado.");
    }

    @Test
    public void testDecrypt_NullIV_ThrowsException() {
        byte[] encryptedData = "test data".getBytes(); // Placeholder
        byte[] key = cipherService.generateMasterKey();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.decrypt(encryptedData, key, null);
        }, "Debería lanzar IllegalArgumentException para IV nulo en descifrado.");
    }

    @Test
    public void testDecrypt_EmptyIV_ThrowsException() {
        byte[] encryptedData = "test data".getBytes(); // Placeholder
        byte[] key = cipherService.generateMasterKey();
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.decrypt(encryptedData, key,new byte[0]);
        }, "Debería lanzar IllegalArgumentException para IV vacío en descifrado.");
    }

    @Test
    public void testDecrypt_IVTooShort_ThrowsException() {
        byte[] encryptedData = "test data".getBytes(); // Placeholder
        byte[] key = cipherService.generateMasterKey();
        byte[] shortIV = new byte[CipherService.IV_LENGTH - 1];
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.decrypt(encryptedData, key, shortIV);
        }, "Debería lanzar IllegalArgumentException para IV demasiado corto en descifrado.");
    }

    @Test
    public void testDecrypt_IVTooLong_ThrowsException() {
        byte[] encryptedData = "test data".getBytes(); // Placeholder
        byte[] key = cipherService.generateMasterKey();
        byte[] longIV = new byte[CipherService.IV_LENGTH + 1];
        assertThrows(IllegalArgumentException.class, () -> {
            cipherService.decrypt(encryptedData, key, longIV);
        }, "Debería lanzar IllegalArgumentException para IV demasiado largo en descifrado.");
    }

    @Test
    public void testDecrypt_Success_ReturnsNonNullData() {
        byte[] originalData = "Data to be decrypted.".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] key = cipherService.generateMasterKey();
        byte[] iv = cipherService.generateIV();

        byte[] encryptedData = cipherService.encrypt(originalData, key, iv);
        byte[] decryptedData = cipherService.decrypt(encryptedData, key, iv);

        assertNotNull(decryptedData, "Los datos descifrados no deberían ser nulos.");
    }

    @Test
    public void testDecrypt_Success_ReturnsNonEmptyData() {
        byte[] originalData = "Data to be decrypted.".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] key = cipherService.generateMasterKey();
        byte[] iv = cipherService.generateIV();

        byte[] encryptedData = cipherService.encrypt(originalData, key, iv);
        byte[] decryptedData = cipherService.decrypt(encryptedData, key, iv);

        assertTrue(decryptedData.length > 0, "Los datos descifrados no deberían estar vacíos.");
    }

    @Test
    public void testDecrypt_Success_ReturnsSameDataAsOriginal() {
        byte[] originalData = "Data to be decrypted.".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] key = cipherService.generateMasterKey();
        byte[] iv = cipherService.generateIV();

        byte[] encryptedData = cipherService.encrypt(originalData, key, iv);
        byte[] decryptedData = cipherService.decrypt(encryptedData, key, iv);

        assertEquals(originalData, decryptedData, "Los datos descifrados no deberían coincidir con los datos originales.");
    }

        @Test
    public void testDecrypt_SameInputs_ReturnsConsistentOutput() {
        byte[] data = "Data to be decrypted.".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] key = cipherService.generateMasterKey();
        byte[] iv = cipherService.generateIV();

        byte[] decryptedData1 = cipherService.decrypt(data, key, iv);
        byte[] decryptedData2 = cipherService.decrypt(data, key, iv);

        assertNotNull(decryptedData1);
        assertNotNull(decryptedData2);
        assertTrue(Arrays.equals(decryptedData1, decryptedData2), "Decifrar con los mismos inputs debería producir el mismo output.");
    }

    @Test
    public void testDecrypt_DifferentIV_ReturnsDifferentOutput() {
        byte[] data = "Data to be decrypted.".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] key = cipherService.generateMasterKey();
        byte[] iv1 = cipherService.generateIV();
        byte[] iv2 = cipherService.generateIV();

        byte[] decryptedData1 = cipherService.decrypt(data, key, iv1);
        byte[] decryptedData2 = cipherService.decrypt(data, key, iv2);

        assertNotNull(decryptedData1);
        assertNotNull(decryptedData2);
        assertFalse(Arrays.equals(decryptedData1, decryptedData2), "Decifrar con IVs diferentes debería producir outputs diferentes.");
    }

    @Test
    public void testDecrypt_DifferentKey_ReturnsDifferentOutput() {
        byte[] data = "Data to be decrypted.".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] iv = cipherService.generateIV();

        byte[] key1 = cipherService.generateMasterKey();
        byte[] key2 = cipherService.generateMasterKey();

        byte[] decryptedData1 = cipherService.decrypt(data, key1, iv);
        byte[] decryptedData2 = cipherService.decrypt(data, key2, iv);

        assertNotNull(decryptedData1);
        assertNotNull(decryptedData2);
        assertFalse(Arrays.equals(decryptedData1, decryptedData2), "Decifrar con claves diferentes debería producir outputs diferentes.");
    }
}


