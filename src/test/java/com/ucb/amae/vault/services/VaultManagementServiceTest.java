package com.ucb.amae.vault.services;

import com.ucb.amae.vault.model.Vault;
import com.ucb.amae.vault.model.VaultEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class VaultManagementServiceTest {

    private VaultManagementService vaultManagementService;

    @BeforeEach
    void setUp() {
        vaultManagementService = new VaultManagementService();
    }

    @Test
    void testFullVaultCycle_NewAddSaveLoad(@TempDir Path tempDir) throws IOException {
        // Arrange
        Path vaultPath = tempDir.resolve("mytest.vault");
        String password = "Super-Secret-Password1";
        VaultEntry newEntry = new VaultEntry("Gmail", "test@gmail.com", "password123", "https://mail.google.com");

        // --- 1. Create a new, empty vault ---
        vaultManagementService.newVault(password, vaultPath);
        Vault initialVault = VaultManagementService.getCurrentVault();
        assertNotNull(initialVault, "Vault should be created in memory.");
        assertTrue(initialVault.getEntries().isEmpty(), "Newly created vault should have no entries.");

        // Store original crypto attributes for later comparison
        byte[] originalSalt = Arrays.copyOf(initialVault.getSalt(), initialVault.getSalt().length);
        byte[] originalKeyIV = Arrays.copyOf(initialVault.getKeyIV(), initialVault.getKeyIV().length);
        byte[] originalDataIV = Arrays.copyOf(initialVault.getDataIV(), initialVault.getDataIV().length);

        // --- 2. Add an entry and save the vault ---
        vaultManagementService.addEntryAndSave(newEntry);

        // --- 3. Load the vault from disk ---
        vaultManagementService.loadVault(password, vaultPath);

        // --- 4. Verify the loaded vault's state ---
        Vault loadedVault = VaultManagementService.getCurrentVault();
        assertNotNull(loadedVault, "Loaded vault should not be null.");

        // Verify crypto attributes haven't changed
        assertArrayEquals(originalSalt, loadedVault.getSalt(), "Salt should remain the same after save/load.");
        assertArrayEquals(originalKeyIV, loadedVault.getKeyIV(), "KeyIV should remain the same after save/load.");
        assertArrayEquals(originalDataIV, loadedVault.getDataIV(), "DataIV should remain the same after save/load.");

        // Verify the entry was loaded correctly
        assertEquals(1, loadedVault.getEntries().size(), "Loaded vault should contain one entry.");
        assertEquals(newEntry, loadedVault.getEntries().get(0), "The loaded entry should match the one that was saved.");
    }
}
