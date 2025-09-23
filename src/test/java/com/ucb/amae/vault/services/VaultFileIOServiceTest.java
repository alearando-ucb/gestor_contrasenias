package com.ucb.amae.vault.services;

import com.ucb.amae.vault.model.dto.VaultFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class VaultFileIOServiceTest {

    @Test
    void testWriteAndLoadVaultFile_SuccessfulCycle(@TempDir Path tempDir) throws IOException {
        // Arrange
        VaultFileIOService vaultFileIOService = new VaultFileIOService();
        Path filePath = tempDir.resolve("test.aeav");

        // 1. Create a VaultFile object with known data
        byte[] salt = new byte[16];
        Arrays.fill(salt, (byte) 1);
        byte[] keyIV = new byte[12];
        Arrays.fill(keyIV, (byte) 2);
        byte[] dataIV = new byte[12];
        Arrays.fill(dataIV, (byte) 3);
        byte[] encryptedKey = new byte[48];
        Arrays.fill(encryptedKey, (byte) 4);
        byte[] encryptedData = new byte[64];
        Arrays.fill(encryptedData, (byte) 5);

        VaultFile originalVaultFile = new VaultFile(salt, keyIV, dataIV, encryptedKey, encryptedData);

        // --- Write Phase ---
        // Act
        vaultFileIOService.writeVaultFile(filePath, originalVaultFile);

        // Assert
        assertTrue(Files.exists(filePath), "File should have been written to disk.");

        // --- Load Phase ---
        // Act
        VaultFile loadedVaultFile = vaultFileIOService.loadVaultFile(filePath);

        // Assert
        assertNotNull(loadedVaultFile, "Loaded VaultFile should not be null.");
        assertArrayEquals(originalVaultFile.getSalt(), loadedVaultFile.getSalt(), "Salt should match.");
        assertArrayEquals(originalVaultFile.getKeyIV(), loadedVaultFile.getKeyIV(), "KeyIV should match.");
        assertArrayEquals(originalVaultFile.getDataIV(), loadedVaultFile.getDataIV(), "DataIV should match.");
        assertArrayEquals(originalVaultFile.getEncryptedKey(), loadedVaultFile.getEncryptedKey(), "EncryptedKey should match.");
        assertArrayEquals(originalVaultFile.getEncryptedData(), loadedVaultFile.getEncryptedData(), "EncryptedData should match.");
    }
}
