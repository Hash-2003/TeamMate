package com.iit.cm2601.teammate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import javafx.concurrent.Task;

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

        // basic validation
        if (q1Spinner.getValue() == null ||
                q2Spinner.getValue() == null ||
                q3Spinner.getValue() == null ||
                q4Spinner.getValue() == null ||
                q5Spinner.getValue() == null) {

            showError("Validation Error", "Please answer all questions (Q1–Q5).");
            return;
        }

        Task<Void> surveyTask = new Task<>() {
            @Override
            protected Void call() {

                int total = q1Spinner.getValue()
                        + q2Spinner.getValue()
                        + q3Spinner.getValue()
                        + q4Spinner.getValue()
                        + q5Spinner.getValue();

                int scaledScore = total * 4;

                PersonalityType type = classifier.classify(scaledScore);

                personalityScore = scaledScore;
                personalityType = type;
                confirmed = true;

                return null;
            }
        };

        surveyTask.setOnSucceeded(e -> {
            Stage stage = (Stage) q1Spinner.getScene().getWindow();
            stage.close();
        });

        surveyTask.setOnFailed(e -> {
            Throwable ex = surveyTask.getException();
            ex.printStackTrace();

            if (ex instanceof IllegalArgumentException) {
                showError("Invalid score",
                        "Personality score out of range: " + ex.getMessage());
            } else {
                showError("Survey Error",
                        "Failed to process survey: " +
                                (ex != null ? ex.getMessage() : "Unknown error"));
            }
        });

        Thread t = new Thread(surveyTask);
        t.setDaemon(true);
        t.start();
    }

    @FXML
    private void onCancelClicked() {
        confirmed = false;
        Stage stage = (Stage) q1Spinner.getScene().getWindow();
        stage.close();
    }

    private void showError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
