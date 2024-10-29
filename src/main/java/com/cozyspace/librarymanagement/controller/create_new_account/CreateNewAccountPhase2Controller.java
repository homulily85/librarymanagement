package com.cozyspace.librarymanagement.controller.create_new_account;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class CreateNewAccountPhase2Controller {
    @FXML
    private SplitPane createNewAccountPhase2;
    @FXML
    private TextField codeField;
    @FXML
    private Button continueButton;
    @FXML
    private Label inputPrompt;
    @FXML
    private Label codeFailedPrompt;

    public void initialize() {
        final String IDLE_LOGIN_BUTTON_STYLE = """
                -fx-text-fill: #ffffff;
                -fx-background-color: #0e4ed5;
                -fx-border-radius: 20;
                -fx-background-radius: 20;
                -fx-padding: 5;
                """;
        final String HOVERED_LOGIN_BUTTON_STYLE = """
                -fx-text-fill: #ffffff;
                -fx-background-color: #043ea8;
                -fx-border-radius: 20;
                -fx-background-radius: 20;
                -fx-padding: 5;
                """;

        continueButton.setStyle(IDLE_LOGIN_BUTTON_STYLE);
        continueButton.setOnMouseEntered(_ -> continueButton.setStyle(HOVERED_LOGIN_BUTTON_STYLE));
        continueButton.setOnMouseExited(_ -> continueButton.setStyle(IDLE_LOGIN_BUTTON_STYLE));

    }

    public void checkAndContinue() {
        if (codeField.getText().isEmpty()) {
            codeFailedPrompt.setVisible(false);
            inputPrompt.setVisible(true);
            return;
        }

        if (!codeField.getText().equals(DataTransfer.getInstance().getDataMap().get("emailValidationCode"))) {
            inputPrompt.setVisible(false);
            codeFailedPrompt.setVisible(true);
            return;
        }

        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(createNewAccountPhase2);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(_ -> {
            Parent root;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class
                        .getResource("fxml/create_new_account/create_new_account_phase_3.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            assert root != null;
            Scene scene = new Scene(root);
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });
    }
}
