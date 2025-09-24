package com.ucb.amae.vault.views;

import com.ucb.amae.vault.App;
import com.ucb.amae.vault.model.VaultEntry;
import com.ucb.amae.vault.services.VaultManagementService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainVaultController {

    @FXML
    private Label vaultNameLabel;
    @FXML
    private Button addEntryButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        if (VaultManagementService.getCurrentVaultFileName() != null) {
            vaultNameLabel.setText("Bóveda: " + VaultManagementService.getCurrentVaultFileName());
        } else {
            vaultNameLabel.setText("Bóveda: [No cargada]"); // Fallback
        }

        // Connect buttons to handlers
        logoutButton.setOnAction(event -> handleLogout());
        addEntryButton.setOnAction(event -> handleAddEntry());
        // searchButton.setOnAction(event -> handleSearch()); // searchButton is not injected yet
    }

    @FXML
    private void handleLogout() {
        try {
            App.setRoot("entry_vault");
        } catch (IOException e) {
            showStatusMessage("Error al cerrar la bóveda: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddEntry() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucb/amae/vault/new_entry_dialog.fxml"));
            Parent root = loader.load();

            NewEntryDialogController dialogController = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nueva Entrada de Bóveda");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(addEntryButton.getScene().getWindow()); // Set owner to main window
            dialogStage.setScene(new Scene(root));

            dialogController.setDialogStage(dialogStage);

            dialogStage.showAndWait(); // Show dialog and wait for it to close

            if (dialogController.isConfirmed()) {
                VaultEntry newEntry = dialogController.getVaultEntry();
                // For now, just display the new entry's service name
                showStatusMessage("Nueva entrada añadida: " + newEntry.getServiceName(), false);
                // TODO: Add newEntry to VaultManagementService and save
            } else {
                showStatusMessage("Creación de entrada cancelada.", false);
            }

        } catch (IOException e) {
            showStatusMessage("Error al abrir el diálogo de nueva entrada: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void showStatusMessage(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setTextFill(isError ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.BLACK);
    }
}