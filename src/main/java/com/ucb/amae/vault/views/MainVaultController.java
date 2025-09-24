package com.ucb.amae.vault.views;

import com.ucb.amae.vault.App;
import com.ucb.amae.vault.model.VaultEntry;
import com.ucb.amae.vault.services.VaultManagementService;
import com.ucb.amae.vault.views.components.VaultEntryCellController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
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
    private ListView<VaultEntry> vaultEntriesListView;

    private ObservableList<VaultEntry> observableVaultEntries;
    private VaultManagementService vaultManagementService;

    @FXML
    public void initialize() {
        if (VaultManagementService.getCurrentVault() != null) {
            vaultNameLabel.setText("Bóveda: " + VaultManagementService.getCurrentVaultFileName());

            observableVaultEntries = FXCollections.observableArrayList(VaultManagementService.getCurrentVault().getEntries());
            vaultEntriesListView.setItems(observableVaultEntries);

            vaultEntriesListView.setCellFactory(param -> new ListCell<VaultEntry>() {
                private FXMLLoader loader;
                private GridPane cellRoot;
                private VaultEntryCellController controller;

                @Override
                protected void updateItem(VaultEntry entry, boolean empty) {
                    super.updateItem(entry, empty);
                    if (empty || entry == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        try {
                            loader = new FXMLLoader(getClass().getResource("/com/ucb/amae/vault/vault_entry_cell.fxml"));
                            cellRoot = loader.load();
                            controller = loader.getController();
                            controller.setMainController(MainVaultController.this);
                            controller.setVaultEntry(entry);
                            setGraphic(cellRoot);
                        } catch (IOException e) {
                            e.printStackTrace();
                            setText("Error al cargar la celda");
                            setGraphic(null);
                        }
                    }
                }
            });

        } else {
            vaultNameLabel.setText("Bóveda: [No cargada]"); // Fallback
        }

        this.vaultManagementService = new VaultManagementService();
        logoutButton.setOnAction(event -> handleLogout());
        addEntryButton.setOnAction(event -> handleAddEntry());
    }

    public void deleteEntry(VaultEntry entry) {
        if (entry == null) {
            return;
        }
        try {
            vaultManagementService.deleteEntryAndSave(entry);
            observableVaultEntries.remove(entry);
            showStatusMessage("Entrada '" + entry.getServiceName() + "' eliminada.", false);
        } catch (Exception e) {
            showStatusMessage("Error al eliminar la entrada: " + e.getMessage(), true);
            e.printStackTrace();
        }
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
            dialogStage.initOwner(addEntryButton.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            dialogController.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (dialogController.isConfirmed()) {
                VaultEntry newEntry = dialogController.getVaultEntry();
                observableVaultEntries.add(newEntry);
                showStatusMessage("Nueva entrada añadida: " + newEntry.getServiceName(), false);
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
