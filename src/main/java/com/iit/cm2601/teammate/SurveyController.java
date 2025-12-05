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
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 0 )
        );
        q2Spinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 0 )
        );
        q3Spinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 0 )
        );
        q4Spinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 0 )
        );
        q5Spinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 0 )
        );
    }

    @FXML
    private void onCalculateClicked() {

        // basic validation
        if (q1Spinner.getValue() == 0 ||
                q2Spinner.getValue() == 0 ||
                q3Spinner.getValue() == 0 ||
                q4Spinner.getValue() == 0 ||
                q5Spinner.getValue() == 0) {

            showError("Validation Error", "Please answer all questions (Q1–Q5).");
            return;
        }

        int q1 = q1Spinner.getValue();
        int q2 = q2Spinner.getValue();
        int q3 = q3Spinner.getValue();
        int q4 = q4Spinner.getValue();
        int q5 = q5Spinner.getValue();

        Task<Void> surveyTask = new Task<>() {
            @Override
            protected Void call() {

                int total = q1 + q2 + q3 + q4 + q5;

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
