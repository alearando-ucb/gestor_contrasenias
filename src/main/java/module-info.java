module com.ucb.amae.vault {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.ucb.amae.vault to javafx.fxml;
    exports com.ucb.amae.vault;
}
