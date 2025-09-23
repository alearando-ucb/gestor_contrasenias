module com.ucb.amae.vault {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens com.ucb.amae.vault to javafx.fxml;
    opens com.ucb.amae.vault.model to com.fasterxml.jackson.databind;
    exports com.ucb.amae.vault;
}
