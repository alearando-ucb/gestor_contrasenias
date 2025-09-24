package com.ucb.amae.vault.views.components;

import com.ucb.amae.vault.model.VaultEntry;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Duration;

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
    private ImageView togglePasswordIcon;
    @FXML
    private Button editEntryButton;
    @FXML
    private Button deleteEntryButton;
    @FXML
    private TextField urlField;

    private VaultEntry vaultEntry;
    private boolean passwordVisible = false;

    private final Image eyeOpenIcon = new Image(getClass().getResourceAsStream("/com/ucb/amae/vault/icons/eye_open.png"));
    private final Image eyeClosedIcon = new Image(getClass().getResourceAsStream("/com/ucb/amae/vault/icons/eye_closed.png"));

    @FXML
    private void initialize() {
        copyUsernameButton.setOnAction(event -> copyToClipboardWithTimeout(copyUsernameButton, vaultEntry.getUsername()));
        copyPasswordButton.setOnAction(event -> copyToClipboardWithTimeout(copyPasswordButton, vaultEntry.getPassword()));
        togglePasswordVisibilityButton.setOnAction(event -> togglePasswordVisibility());
    }

    public void setVaultEntry(VaultEntry entry) {
        this.vaultEntry = entry;
        serviceNameLabel.setText(entry.getServiceName());
        usernameField.setText(entry.getUsername());
        urlField.setText(entry.getUrl());
        passwordField.setText("********");
        passwordVisible = false;
        togglePasswordIcon.setImage(eyeClosedIcon);
    }

    private void copyToClipboardWithTimeout(Button button, String text) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);

        final ImageView originalGraphic = (ImageView) button.getGraphic();
        button.setGraphic(null);
        button.setText("¡Copiado!");
        button.setDisable(true);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(20), ae -> {
                if (clipboard.hasString() && text.equals(clipboard.getString())) {
                    clipboard.clear();
                }
                button.setText(null);
                button.setGraphic(originalGraphic);
                button.setDisable(false);
            })
        );
        timeline.play();
    }

    public void togglePasswordVisibility() {
        if (passwordVisible) {
            passwordField.setText("********");
            togglePasswordIcon.setImage(eyeClosedIcon);
        } else {
            passwordField.setText(vaultEntry.getPassword());
            togglePasswordIcon.setImage(eyeOpenIcon);
        }
        passwordVisible = !passwordVisible;
    }

    public VaultEntry getVaultEntry() {
        return vaultEntry;
    }
}
