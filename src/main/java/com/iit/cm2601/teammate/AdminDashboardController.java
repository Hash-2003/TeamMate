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
import java.util.ArrayList;
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
    private TextArea teamOutputArea;

    @FXML
    private TableColumn<Participant, PersonalityType> colPersonality;

    @FXML
    private Spinner<Integer> teamSizeSpinner;

    private final ParticipantCSVHandler csvHandler = new ParticipantCSVHandler();
    private final ObservableList<Participant> participantData = FXCollections.observableArrayList();

    private List<Participant> participants = new ArrayList<>();
    private List<Team> teams = new ArrayList<>();

    @FXML
    private void initialize() {
        // Setup table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGame.setCellValueFactory(new PropertyValueFactory<>("preferredGame"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("preferredRole"));
        colSkill.setCellValueFactory(new PropertyValueFactory<>("skillLevel"));
        colPersonality.setCellValueFactory(new PropertyValueFactory<>("personalityType"));

        participantTable.setItems(participantData);


        teamSizeSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 10, 5)
        );
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
            participants = csvHandler.loadParticipants(path);
            participantData.setAll(participants);

            showInfo("Loaded Participants",
                    "Successfully loaded " + participants.size() + " participants.");

        } catch (ParticipantFileException | InvalidParticipantDataException e) {
            showError("Error Loading CSV", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onExportTeams() {
        // team formation algorithm to be implemented
        showInfo("Not implemented", "Team exporting algorithm will be added later.");
    }

    @FXML
    private void onFormTeams() {
        if (participants == null || participants.isEmpty()) {
            showError("No Participants",
                    "Please import or register participants before forming teams.");
            return;
        }

        Integer teamSize = teamSizeSpinner.getValue();
        if (teamSize == null || teamSize < 3 || teamSize > 10) {
            showError("Invalid Team Size",
                    "Please choose a team size between 3 and 10.");
            return;
        }

        try {
            TeamBuilder builder = new TeamBuilder(participants, teamSize);
            teams = builder.formTeams();
            
            StringBuilder sb = new StringBuilder();
            sb.append("Teams formed successfully.\n");
            sb.append("Team size target: ").append(teamSize).append("\n");
            sb.append("Total teams: ").append(teams.size()).append("\n\n");

            for (Team t : teams) {
                sb.append("Team ").append(t.getTeamId())
                        .append(" (size=").append(t.getCurrentSize())
                        .append(", avgSkill=").append(String.format("%.2f", t.getAvgSkill()))
                        .append(")\n");

                for (Participant p : t.getMembers()) {
                    sb.append("  - ").append(p.getId())
                            .append(" | ").append(p.getName())
                            .append(" | ").append(p.getPreferredGame())
                            .append(" | ").append(p.getPreferredRole())
                            .append(" | Skill=").append(p.getSkillLevel())
                            .append(" | ").append(p.getPersonalityType())
                            .append("\n");
                }
                sb.append("\n");
            }

            teamOutputArea.setText(sb.toString());

            showInfo("Teams Formed", "Teams have been formed successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Team Formation Error",
                    "An error occurred while forming teams: " + e.getMessage());
        }
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
