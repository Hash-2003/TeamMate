package com.iit.cm2601.teammate;

import com.iit.cm2601.teammate.exceptions.InvalidParticipantDataException;
import com.iit.cm2601.teammate.exceptions.ParticipantFileException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.io.File;
import java.util.List;

public class AdminDashboardController {

    @FXML
    private Button importCsvButton;

    @FXML
    private Button logoutButton;

    @FXML
    private TableView<Participant> participantTable;

    @FXML
    private TableColumn<Participant, String> colId;

    @FXML
    private TableColumn<Participant, String> colName;

    @FXML
    private TableColumn<Participant, GameType> colGame;

    @FXML
    private TableColumn<Participant, RoleType> colRole;

    @FXML
    private TableColumn<Participant, Integer> colSkill;

    @FXML
    private TableColumn<Participant, PersonalityType> colPersonality;

    private final ParticipantCSVHandler csvHandler = new ParticipantCSVHandler();
    private final ObservableList<Participant> participantData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Setup table columns → must match Participant getters
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGame.setCellValueFactory(new PropertyValueFactory<>("preferredGame"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("preferredRole"));
        colSkill.setCellValueFactory(new PropertyValueFactory<>("skillLevel"));
        colPersonality.setCellValueFactory(new PropertyValueFactory<>("personalityType"));

        participantTable.setItems(participantData);
    }

    @FXML
    private void onImportCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Participants CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        Stage stage = (Stage) importCsvButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file == null) return; // user cancelled

        loadParticipants(file.getAbsolutePath());
    }

    private void loadParticipants(String path) {
        try {
            List<Participant> participants = csvHandler.loadParticipants(path);
            participantData.setAll(participants);

            showInfo("Loaded Participants",
                    "Successfully loaded " + participants.size() + " participants.");

        } catch (ParticipantFileException | InvalidParticipantDataException e) {
            showError("Error Loading CSV", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onFormTeams() {
        // team formation algorithm to be implemented
        showInfo("Not implemented", "Team formation algorithm will be added later.");
    }

    @FXML
    private void onExportTeams() {
        // to be implemented
        showInfo("Not implemented", "Team export will be implemented later.");
    }

    @FXML
    private void onLogout() {
        // Go back to login-view
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/iit/cm2601/teammate/login-view.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("TeamMate - Sports Team Builder");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            showError("Error", "Unable to return to Login screen.");
            e.printStackTrace();
        }
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
        alert.setTitle("Success");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
