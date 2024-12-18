package com.cozyspace.librarymanagement.controller.login;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.controller.AccountRelatedController;
import com.cozyspace.librarymanagement.datasource.Datasource;
import com.cozyspace.librarymanagement.email.Email;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginPhase2Controller extends AccountRelatedController {
    @FXML
    private SplitPane loginPhase2;
    private String lastPasswordNameInput;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button passwordResetButton;
    @FXML
    private Label inputPrompt;
    @FXML
    private Label loginFailedPrompt;

    public void initialize() {
        modifyMainButtonStyle(loginButton);
        modifySubButtonStyle(passwordResetButton);
    }

    public void login() {
        if (passwordField.getText().isEmpty()) {
            loginFailedPrompt.setVisible(false);
            inputPrompt.setVisible(true);
            return;
        }
        if (passwordField.getText().equals(lastPasswordNameInput)) {
            return;
        }

        if (!UserManager.authenticate(DataTransfer.getInstance().getDataMap().get("loginUsername"), passwordField.getText())) {
            lastPasswordNameInput = passwordField.getText();
            inputPrompt.setVisible(false);
            loginFailedPrompt.setVisible(true);
            return;
        }

        UserManager.createNewUserInstance(DataTransfer.getInstance().getDataMap().get("loginUsername"));

        Parent root = null;
        Stage stage = (Stage) loginButton.getScene().getWindow();

        try {
            root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/loading_screen.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }


    public void resetPassword() {

        String email = Objects.requireNonNull(Datasource.getAccountInfo(DataTransfer.getInstance().getDataMap().get("loginUsername"))).
                get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_EMAIL - 1);

        DataTransfer.getInstance().getDataMap().put("userEmail", email);

        new Thread(() -> Email.sendResetPasswordConfirmationEmail(email)).start();

        fadeTransition(loginPhase2, "fxml/reset_password/reset_password_phase_1.fxml", 500);

    }

    public void back() {
        fadeTransition(loginPhase2, "fxml/login/login_phase_1.fxml", 500);

    }
}
