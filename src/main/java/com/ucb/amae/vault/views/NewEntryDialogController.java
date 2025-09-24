package com.ucb.amae.vault.views;

import com.ucb.amae.vault.model.VaultEntry;
import com.ucb.amae.vault.services.PasswordManagementService;
import com.ucb.amae.vault.services.VaultManagementService;
import com.ucb.amae.vault.services.models.PasswordStrength;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewEntryDialogController {

    @FXML
    private TextField serviceNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
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
    private VaultEntry resultEntry;
    private boolean confirmed = false;

    @FXML
    private void initialize() {
        generatePasswordButton.setOnAction(event -> handleGeneratePassword());
        saveButton.setOnAction(event -> handleSave());
        cancelButton.setOnAction(event -> handleCancel());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEntryToEdit(VaultEntry entry) {
        serviceNameField.setText(entry.getServiceName());
        usernameField.setText(entry.getUsername());
        passwordField.setText(entry.getPassword());
        urlField.setText(entry.getUrl());
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public VaultEntry getVaultEntry() {
        return resultEntry;
    }

    @FXML
    private void handleSave() {
        String serviceName = serviceNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String url = urlField.getText();

        if (serviceName.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty()) {
            statusLabel.setText("Servicio, usuario y contraseña son requeridos.");
            return;
        }

        PasswordStrength strength = PasswordManagementService.evaluatePasswordStrength(password);
        if (strength != PasswordStrength.FUERTE && strength != PasswordStrength.MUY_FUERTE) {
            statusLabel.setText("La contraseña debe ser FUERTE o MUY_FUERTE.");
            return;
        }

        this.resultEntry = new VaultEntry(serviceName, username, password, url);
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
        String generatedPassword = PasswordManagementService.generateSecurePassword();
        passwordField.setText(generatedPassword);
        statusLabel.setText(""); // Clear any previous status message
    }
}
