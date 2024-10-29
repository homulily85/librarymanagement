package com.cozyspace.librarymanagement.controller.create_new_account;

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

public class CreateNewAccountPhase3Controller {
    @FXML
    private Label usernameExist;
    @FXML
    private SplitPane createNewAccountPhase3;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField repeatPasswordField;
    @FXML
    private Button continueButton;
    @FXML
    private Label inputPrompt;
    @FXML
    private Label usernameFailPrompt;
    @FXML
    private Label passwordFailPrompt;
    @FXML
    private Label resetPasswordFailPrompt;

    public void initialize() {
        final String IDLE_BUTTON_STYLE = """
                -fx-text-fill: #ffffff;
                -fx-background-color: #0e4ed5;
                -fx-border-radius: 20;
                -fx-background-radius: 20;
                -fx-padding: 5;
                """;
        final String HOVERED_BUTTON_STYLE = """
                -fx-text-fill: #ffffff;
                -fx-background-color: #043ea8;
                -fx-border-radius: 20;
                -fx-background-radius: 20;
                -fx-padding: 5;
                """;

        continueButton.setStyle(IDLE_BUTTON_STYLE);
        continueButton.setOnMouseEntered(_ -> continueButton.setStyle(HOVERED_BUTTON_STYLE));
        continueButton.setOnMouseExited(_ -> continueButton.setStyle(IDLE_BUTTON_STYLE));

        Tooltip userNameTooltip = new Tooltip();
        userNameTooltip.setText("Tên đăng nhập có độ dài từ 6 đến 15 kí tự và không chứa kí tự đặc biệt.");
        usernameField.setTooltip(userNameTooltip);

        Tooltip passwordTooltip = new Tooltip();
        passwordTooltip.setText("Mật khẩu có tối thiểu 6 kí tự, chứa ít nhất một chữ cái in hoa và chứa ít nhất một chữ số");
        passwordField.setTooltip(passwordTooltip);

    }

    public void checkAndContinue() {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            inputPrompt.setVisible(true);
            usernameExist.setVisible(false);
            usernameFailPrompt.setVisible(false);
            passwordFailPrompt.setVisible(false);
            resetPasswordFailPrompt.setVisible(false);
            return;
        }

        if (usernameField.getText().length() < 6) {
            inputPrompt.setVisible(false);
            usernameExist.setVisible(false);
            usernameFailPrompt.setVisible(true);
            passwordFailPrompt.setVisible(false);
            resetPasswordFailPrompt.setVisible(false);
            return;
        }
        if (User.isUserNameExist(usernameField.getText())) {
            inputPrompt.setVisible(false);
            usernameExist.setVisible(true);
            usernameFailPrompt.setVisible(false);
            passwordFailPrompt.setVisible(false);
            resetPasswordFailPrompt.setVisible(false);
            return;
        }
        if (!passwordField.getText().matches("^(?=.*?[A-Z])(?=.*?[0-9]).{6,}$")) {
            inputPrompt.setVisible(false);
            usernameExist.setVisible(false);
            usernameFailPrompt.setVisible(false);
            passwordFailPrompt.setVisible(true);
            resetPasswordFailPrompt.setVisible(false);
            return;
        }

        if (!repeatPasswordField.getText().equals(passwordField.getText())) {
            inputPrompt.setVisible(false);
            usernameExist.setVisible(false);
            usernameFailPrompt.setVisible(false);
            passwordFailPrompt.setVisible(false);
            resetPasswordFailPrompt.setVisible(true);
            return;
        }

        DataTransfer.getInstance().getDataMap().put("newUsername", usernameField.getText());
        DataTransfer.getInstance().getDataMap().put("newUserPassword", passwordField.getText());

        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(createNewAccountPhase3);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(_ -> {
            Parent root;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class
                        .getResource("fxml/create_new_account/create_new_account_phase_4.fxml")));
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
