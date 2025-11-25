package com.iit.cm2601.teammate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminLoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button cancelButton;

    private static final String AdminUsername = "adminsports@uni.edu";

    private static final String AdminSalt = "KieBLac30YTJ34RKOfJKCg==";
    private static final String AdminHash = "Y5HwePRwV91vHsdvPSfEhhUzwrbV/RKmULipfPgOoa4=";

    @FXML
    private void onLoginClicked() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Error", "Please enter both username and password.");
            return;
        }

        if (!AdminUsername.equals(username)) {
            showError("Login Failed", "Invalid username or password.");
            clearPassword();
            return;
        }

        boolean ok = PasswordVerifier.verifyPassword(password, AdminSalt, AdminHash);

        if (!ok) {
            showError("Login Failed", "Invalid username or password.");
            clearPassword();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/iit/cm2601/teammate/admin-dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Admin Dashboard");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            showError("Error", "Unable to open Admin Dashboard.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancelClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/iit/cm2601/teammate/login-view.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("TeamMate - Sports Team Builder");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) cancelButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            showError("Error", "Unable to open Login screen.");
            e.printStackTrace();
        }
    }

    private void clearPassword() {
        passwordField.clear();
    }

    private void showError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
