package com.ucb.amae.vault.views;

import com.ucb.amae.vault.model.VaultEntry;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewEntryDialogController {

    @FXML
    private TextField serviceNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button generatePasswordButton;
    @FXML
    private TextField urlField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Stage dialogStage;
    private VaultEntry vaultEntry;
    private boolean confirmed = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setVaultEntry(VaultEntry entry) {
        this.vaultEntry = entry;
        serviceNameField.setText(entry.getServiceName());
        usernameField.setText(entry.getUsername());
        passwordField.setText(entry.getPassword());
        urlField.setText(entry.getUrl());
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public VaultEntry getVaultEntry() {
        return vaultEntry;
    }

    // Placeholder methods for button actions
    @FXML
    private void handleSave() {
        // TODO: Implement validation and save logic
        this.vaultEntry = new VaultEntry(
            serviceNameField.getText(),
            usernameField.getText(),
            passwordField.getText(),
            urlField.getText()
        );
        confirmed = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        confirmed = false;
        dialogStage.close();
    }

    @FXML
    private void handleGeneratePassword() {
        // TODO: Implement password generation
        statusLabel.setText("Generar contraseña (TODO)");
    }
}
