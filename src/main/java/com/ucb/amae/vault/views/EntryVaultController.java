package com.ucb.amae.vault.views;

import com.ucb.amae.vault.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class EntryVaultController {

    @FXML
    private void switchToMainVault() throws IOException {
        App.setRoot("main_vault");
    }
}
