package com.ucb.amae.vault.views.components;

import com.ucb.amae.vault.model.VaultEntry;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class VaultEntryCellController {

    @FXML
    private Label serviceNameLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private Button copyUsernameButton;
    @FXML
    private TextField passwordField;
    @FXML
    private Button copyPasswordButton;
    @FXML
    private Button togglePasswordVisibilityButton;
    @FXML
    private Button editEntryButton;
    @FXML
    private Button deleteEntryButton;
    @FXML
    private TextField urlField;

    private VaultEntry vaultEntry;
    private boolean passwordVisible = false;

    public void setVaultEntry(VaultEntry entry) {
        this.vaultEntry = entry;
        serviceNameLabel.setText(entry.getServiceName());
        usernameField.setText(entry.getUsername());
        urlField.setText(entry.getUrl());
        passwordField.setText("********"); // Mask password initially
        passwordVisible = false;
        togglePasswordVisibilityButton.setText("Mostrar");
    }

    // Handlers for buttons will be added later in MainVaultController or via callbacks
    // For now, these methods are just placeholders or will be implemented as part of the cell factory

    // Example of how a toggle might work (will be connected via MainVaultController)
    public void togglePasswordVisibility() {
        if (passwordVisible) {
            passwordField.setText("********");
            togglePasswordVisibilityButton.setText("Mostrar");
        } else {
            passwordField.setText(vaultEntry.getPassword());
            togglePasswordVisibilityButton.setText("Ocultar");
        }
        passwordVisible = !passwordVisible;
    }

    public VaultEntry getVaultEntry() {
        return vaultEntry;
    }
}