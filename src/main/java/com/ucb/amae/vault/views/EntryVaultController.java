package com.ucb.amae.vault.views;

import com.ucb.amae.vault.App;
import com.ucb.amae.vault.services.VaultManagementService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class EntryVaultController {

    @FXML
    private Label lastVaultNameLabel;
    @FXML
    private PasswordField masterPasswordField;
    @FXML
    private Button changeVaultButton;
    @FXML
    private Button openVaultButton;
    @FXML
    private Button createNewVaultButton;
    @FXML
    private Label statusLabel;

    private File currentVaultFile; // To store the currently selected vault file
    private VaultManagementService vaultManagementService = new VaultManagementService(); // Instantiate service

    @FXML
    public void initialize() {
        lastVaultNameLabel.setText("[Ninguna bóveda seleccionada]");
        openVaultButton.setDisable(true); // Disable until a vault is selected and password entered

        masterPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateOpenVaultButtonState();
        });
    }

    private void updateOpenVaultButtonState() {
        boolean isPasswordEmpty = masterPasswordField.getText().trim().isEmpty();
        openVaultButton.setDisable(currentVaultFile == null || isPasswordEmpty);
    }

    @FXML
    private void handleCreateNewVault() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Nueva Bóveda");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Bóveda", "*.vault"));
        File newVaultFile = fileChooser.showSaveDialog(new Stage());

        if (newVaultFile != null) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("new_vault_password_dialog.fxml"));
                Parent root = loader.load();

                NewVaultPasswordDialogController dialogController = loader.getController();
                dialogController.setVaultPath(newVaultFile.getAbsolutePath());

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Establecer Contraseña Maestra");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(createNewVaultButton.getScene().getWindow());
                dialogStage.setScene(new Scene(root));
                dialogStage.setResizable(false);
                dialogStage.showAndWait();

                if (dialogController.isConfirmed()) {
                    String masterPassword = dialogController.getMasterPassword();
                    try {
                        vaultManagementService.newVault(masterPassword, newVaultFile.toPath());
                        currentVaultFile = newVaultFile;
                        lastVaultNameLabel.setText(newVaultFile.getName());
                        masterPasswordField.clear();
                        updateOpenVaultButtonState();
                        statusLabel.setText("Bóveda '" + newVaultFile.getName() + "' creada con éxito.");
                        App.setRoot("main_vault"); // Transition to main vault view
                    } catch (IllegalArgumentException e) {
                        statusLabel.setText("Error: " + e.getMessage()); // Display password strength error
                    } catch (Exception e) {
                        statusLabel.setText("Error al crear la bóveda: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    statusLabel.setText("Creación de bóveda cancelada.");
                }

            } catch (IOException e) {
                statusLabel.setText("Error al cargar el diálogo de contraseña: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
