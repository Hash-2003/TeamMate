package com.iit.cm2601.teammate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class SurveyController {

    @FXML
    private Spinner<Integer> q1Spinner;

    @FXML
    private Spinner<Integer> q2Spinner;

    @FXML
    private Spinner<Integer> q3Spinner;

    @FXML
    private Spinner<Integer> q4Spinner;

    @FXML
    private Spinner<Integer> q5Spinner;

    private final PersonalityClassifier classifier = new PersonalityClassifier();

    private int personalityScore;
    private PersonalityType personalityType;
    private boolean confirmed = false;

    public int getPersonalityScore() {
        return personalityScore;
    }

    public PersonalityType getPersonalityType() {
        return personalityType;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    @FXML
    private void initialize() {

        q1Spinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3)
        );
        q2Spinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3)
        );
        q3Spinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3)
        );
        q4Spinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3)
        );
        q5Spinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3)
        );
    }

    @FXML
    private void onCalculateClicked() {
        int total = q1Spinner.getValue()
                + q2Spinner.getValue()
                + q3Spinner.getValue()
                + q4Spinner.getValue()
                + q5Spinner.getValue();

        int scaledScore = total * 4;

        try {
            personalityType = classifier.classify(scaledScore);
            personalityScore = scaledScore;
            confirmed = true;

            // Close window
            Stage stage = (Stage) q1Spinner.getScene().getWindow();
            stage.close();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid score");
            alert.setHeaderText("Personality score out of range");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onCancelClicked() {
        confirmed = false;
        Stage stage = (Stage) q1Spinner.getScene().getWindow();
        stage.close();
    }
}
