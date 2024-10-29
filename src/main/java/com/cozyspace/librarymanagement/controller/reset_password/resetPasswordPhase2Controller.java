package com.cozyspace.librarymanagement.controller.reset_password;

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

public class resetPasswordPhase2Controller {
    @FXML
    private SplitPane reset_password_phase_2;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private Label inputPrompt;
    @FXML
    private Label inputFail;
    @FXML
    private Label reInputFail;
    @FXML
    private Button continueButton;

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

        Tooltip passwordTooltip = new Tooltip();
        passwordTooltip.setText("Mật khẩu có tối thiểu 6 kí tự, chứa ít nhất một chữ cái in hoa và chứa ít nhất một chữ số");
        newPasswordField.setTooltip(passwordTooltip);

    }


    @FXML
    private void checkAndContinue() {
        if (newPasswordField.getText().isEmpty() || repeatPasswordField.getText().isEmpty()) {
            inputFail.setVisible(false);
            inputPrompt.setVisible(true);
            reInputFail.setVisible(false);
            return;
        }

        if (!newPasswordField.getText().matches("^(?=.*?[A-Z])(?=.*?[0-9]).{6,}$")) {
            inputFail.setVisible(true);
            inputPrompt.setVisible(false);
            reInputFail.setVisible(false);
            return;
        }

        if (!newPasswordField.getText().equals(repeatPasswordField.getText())) {
            inputFail.setVisible(false);
            inputPrompt.setVisible(false);
            reInputFail.setVisible(true);
            return;
        }

        User.updatePassword(DataTransfer.getInstance().getDataMap().get("loginUsername"), newPasswordField.getText());

        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(reset_password_phase_2);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(_ -> {
            Parent root;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/reset_password/reset_password_phase_3.fxml")));
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

