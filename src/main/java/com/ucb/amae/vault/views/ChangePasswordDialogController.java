package com.ucb.amae.vault.views;

import com.ucb.amae.vault.services.PasswordManagementService;
import com.ucb.amae.vault.services.VaultManagementService;
import com.ucb.amae.vault.services.exceptions.DecryptionException;
import com.ucb.amae.vault.services.models.PasswordStrength;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class ChangePasswordDialogController {

    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Stage dialogStage;
    private boolean confirmed = false;
    private String currentPassword;
    private String newPassword;
    private VaultManagementService vaultManagementService;

    @FXML
    private void initialize() {
        saveButton.setOnAction(event -> handleSave());
        cancelButton.setOnAction(event -> handleCancel());
        this.vaultManagementService = new VaultManagementService();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    private void handleSave() {
        String currentPass = currentPasswordField.getText();
        String newPass = newPasswordField.getText();
        String confirmPass = confirmPasswordField.getText();

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            statusLabel.setText("Todos los campos son obligatorios.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            statusLabel.setText("La nueva contraseña no coincide con la confirmación.");
            return;
        }

        if (currentPass.equals(newPass)) {
            statusLabel.setText("La nueva contraseña no puede ser igual a la actual.");
            return;
        }

        PasswordStrength strength = PasswordManagementService.evaluatePasswordStrength(newPass);
        if (strength != PasswordStrength.FUERTE && strength != PasswordStrength.MUY_FUERTE) {
            statusLabel.setText("La nueva contraseña debe ser FUERTE o MUY_FUERTE.");
            return;
        }

        // Validate current password by attempting to load the vault
        try {
            vaultManagementService.loadVault(currentPass, VaultManagementService.getCurrentVaultPath());
        } catch (DecryptionException e) {
            statusLabel.setText("Contraseña actual incorrecta.");
            return;
        } catch (IllegalStateException e) {
            statusLabel.setText("Error de estado al verificar la contraseña actual: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        this.currentPassword = currentPass;
        this.newPassword = newPass;
        this.confirmed = true;
        dialogStage.close();
    }

    private void handleCancel() {
        dialogStage.close();
    }
}
