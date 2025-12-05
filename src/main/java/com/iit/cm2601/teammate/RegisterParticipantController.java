package com.iit.cm2601.teammate;

import com.iit.cm2601.teammate.exceptions.InvalidParticipantDataException;
import com.iit.cm2601.teammate.exceptions.ParticipantFileException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RegisterParticipantController {

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<GameType> gameCombo;

    @FXML
    private Spinner<Integer> skillSpinner;

    @FXML
    private ComboBox<RoleType> roleCombo;

    @FXML
    private TextField personalityScoreField;

    @FXML
    private TextField personalityTypeField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;


    private final ParticipantCSVHandler csvHandler = new ParticipantCSVHandler();

    private int personalityScore = 0;
    private PersonalityType personalityType = null;

    private static final String PARTICIPANT_CSV_PATH =
            "src/main/resources/com/iit/cm2601/teammate/data/participant_details.csv";

    @FXML
    private void initialize() {
        gameCombo.getItems().setAll(GameType.values());
        roleCombo.getItems().setAll(RoleType.values());


        skillSpinner.setValueFactory(
                new IntegerSpinnerValueFactory(0, 10)
        );

        if (personalityScoreField != null) {
            personalityScoreField.setEditable(false);
        }
        if (personalityTypeField != null) {
            personalityTypeField.setEditable(false);
        }
    }

    @FXML
    private void onFillSurveyClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/iit/cm2601/teammate/survey-view.fxml"));
            Parent root = loader.load();

            SurveyController surveyController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Personality Survey");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(saveButton.getScene().getWindow());
            stage.showAndWait();

            if (surveyController.isConfirmed()) {
                personalityScore = surveyController.getPersonalityScore();
                personalityType = surveyController.getPersonalityType();

                if (personalityScoreField != null) {
                    personalityScoreField.setText(String.valueOf(personalityScore));
                }
                if (personalityTypeField != null && personalityType != null) {
                    personalityTypeField.setText(personalityType.name());
                }
            }

        } catch (IOException e) {
            showError("Error", "Unable to open survey window.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveClicked() {
        try {
            // 1. Basic validation
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            GameType game = gameCombo.getValue();
            RoleType role = roleCombo.getValue();
            Integer skill = skillSpinner.getValue();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty()
                    || game == null || role == null || skill == 0) {
                showError("Validation Error", "Please fill in all required fields.");
                return;
            }

            if (!email.contains("@")) {
                showError("Validation Error", "Please enter a valid email address.");
                return;
            }

            if (personalityType == null) {
                showError("Validation Error", "Please complete the personality survey.");
                return;
            }

            //Load existing participants
            List<Participant> participants =
                    csvHandler.loadParticipants(PARTICIPANT_CSV_PATH);

            //Duplicate ID check
            boolean idExists = participants.stream()
                    .anyMatch(p -> p.getId().equalsIgnoreCase(id));

            if (idExists) {
                showError("Duplicate ID",
                        "A participant with ID '" + id + "' already exists.");
                return;
            }

            //Create new Participant object
            Participant newParticipant = new Participant(
                    id,
                    name,
                    email,
                    game,
                    skill,
                    role,
                    personalityScore,
                    personalityType
            );

            //Add and save list
            participants.add(newParticipant);
            csvHandler.saveParticipantsToCsv(participants, PARTICIPANT_CSV_PATH);

            showInfo("Success",newParticipant + " added successfully!");
            clearForm();

        } catch (ParticipantFileException | InvalidParticipantDataException e) {
            showError("File Error", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showError("Unexpected Error", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancelClicked() {
        Stage currentStage = (Stage) cancelButton.getScene().getWindow();
        currentStage.close();

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/iit/cm2601/teammate/login-view.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("TeamMate - Sports Team Builder");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showError("Error", "Unable to return to Login screen.");
            e.printStackTrace();
        }
    }

    private void clearForm() {
        idField.clear();
        nameField.clear();
        emailField.clear();
        gameCombo.getSelectionModel().clearSelection();
        roleCombo.getSelectionModel().clearSelection();
        skillSpinner.getValueFactory().setValue(5);

        personalityScore = 0;
        personalityType = null;

        if (personalityScoreField != null) {
            personalityScoreField.clear();
        }
        if (personalityTypeField != null) {
            personalityTypeField.clear();
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
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
