package com.ucb.amae.vault.views;

import com.ucb.amae.vault.services.PasswordManagementService;
import com.ucb.amae.vault.services.models.PasswordStrength;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class NewVaultPasswordDialogController {

    @FXML
    private Label vaultPathLabel;
    @FXML
    private PasswordField masterPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button createVaultButton;
    @FXML
    private Button cancelButton;

    private String masterPassword;
    private boolean confirmed = false;

    public void setVaultPath(String path) {
        vaultPathLabel.setText(path);
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    @FXML
    private void handleCreateVault() {
        String password = masterPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Por favor, introduce y confirma la contraseña.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Las contraseñas no coinciden.");
            return;
        }

        // Validate password strength
        if (PasswordManagementService.evaluatePasswordStrength(password) != PasswordStrength.MUY_FUERTE) {
            statusLabel.setText("La contraseña debe ser MUY FUERTE. Incluye mayúsculas, minúsculas, números y símbolos, y al menos 12 caracteres.");
            return;
        }

        this.masterPassword = password;
        this.confirmed = true;
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        this.confirmed = false;
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
