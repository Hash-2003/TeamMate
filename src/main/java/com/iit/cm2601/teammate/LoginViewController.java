package com.iit.cm2601.teammate;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {

    @FXML
    private Button adminLoginBtn;

    @FXML
    private Button participantRegisterBtn;

    @FXML
    private Button exitBtn;

    @FXML
    private void onAdminLoginClicked(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/admin-view.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Admin Dashboard");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) adminLoginBtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Unable to open Admin view.",
                    "Please check that admin-view.fxml exists and the path is correct.");
        }
    }

    @FXML
    private void onParticipantRegisterClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/iit/cm2601/teammate/register-participant.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Participant Registration");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) participantRegisterBtn.getScene().getWindow();
            currentStage.close();

    } catch (IOException e) {
        e.printStackTrace();
        showError("Unable to open Admin view.",
                "Please check that admin-view.fxml exists and the path is correct.");
    }
    }

    @FXML
    private void onExitClicked(ActionEvent event) {
        Platform.exit();
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
