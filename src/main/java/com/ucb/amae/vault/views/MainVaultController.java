package com.ucb.amae.vault.views;

import com.ucb.amae.vault.App;
import com.ucb.amae.vault.services.VaultManagementService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainVaultController {

    @FXML
    private Label vaultNameLabel;

    @FXML
    public void initialize() {
        if (VaultManagementService.getCurrentVaultFileName() != null) {
            vaultNameLabel.setText("Bóveda: " + VaultManagementService.getCurrentVaultFileName());
        } else {
            vaultNameLabel.setText("Bóveda: [No cargada]"); // Fallback
        }
    }
}
