package com.ucb.amae.vault.views;

import com.ucb.amae.vault.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class MainVaultController {

    @FXML
    private void switchToEntryVault() throws IOException {
        App.setRoot("entry_vault");
    }
}
