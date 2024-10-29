package com.cozyspace.librarymanagement.controller.login;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.user.User;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class LoginPhase1Controller {
    @FXML
    private Button createNewAccountButton;
    @FXML
    private SplitPane loginPhase1Screen;
    private String lastUserNameInput;
    @FXML
    public Button continueButton;
    @FXML
    private Label loginFailedPrompt;
    @FXML
    private Label inputPrompt;
    @FXML
    private TextField usernameField;

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

        final String IDLE_CREATE_NEW_ACCOUNT_BUTTON_STYLE = """
                -fx-text-fill: #043ea8;
                -fx-background-color: transparent;
                -fx-border-radius: 20;
                -fx-background-radius: 20;
                -fx-padding: 5;
                """;
        final String HOVERED_CREATE_NEW_ACCOUNT_LOGIN_BUTTON_STYLE = """
                -fx-text-fill: #043ea8;
                -fx-background-color: #ece9e9;
                -fx-border-radius: 20;
                -fx-background-radius: 20;
                -fx-padding: 5;
                """;

        continueButton.setStyle(IDLE_LOGIN_BUTTON_STYLE);
        continueButton.setOnMouseEntered(_ -> continueButton.setStyle(HOVERED_LOGIN_BUTTON_STYLE));
        continueButton.setOnMouseExited(_ -> continueButton.setStyle(IDLE_LOGIN_BUTTON_STYLE));

        createNewAccountButton.setStyle(IDLE_CREATE_NEW_ACCOUNT_BUTTON_STYLE);
        createNewAccountButton.setOnMouseEntered(_ -> createNewAccountButton.setStyle(HOVERED_CREATE_NEW_ACCOUNT_LOGIN_BUTTON_STYLE));
        createNewAccountButton.setOnMouseExited(_ -> createNewAccountButton.setStyle(IDLE_CREATE_NEW_ACCOUNT_BUTTON_STYLE));
    }

    public void checkAndContinue() {
        if (usernameField.getText().isEmpty()) {
            loginFailedPrompt.setVisible(false);
            inputPrompt.setVisible(true);
            return;
        }
        if (usernameField.getText().equals(lastUserNameInput)) {
            return;
        }
        if (!User.isUserNameExist(usernameField.getText())) {
            lastUserNameInput = usernameField.getText();
            inputPrompt.setVisible(false);
            loginFailedPrompt.setVisible(true);
            return;
        }

        DataTransfer.getInstance().getDataMap().put("loginUsername", usernameField.getText());

        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(loginPhase1Screen);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(_ -> {
            Parent root;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/login/login_phase_2.fxml")));
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

    public void changeToCreateNewAccount() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(loginPhase1Screen);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(_ -> {
            Parent root;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class.
                        getResource("fxml/create_new_account/create_new_account_phase_1.fxml")));
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