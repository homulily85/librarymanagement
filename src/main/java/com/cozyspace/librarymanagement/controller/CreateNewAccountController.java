package com.cozyspace.librarymanagement.controller;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.email.Email;
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

public class CreateNewAccountController {
    private String lastUserNameInput;
    private String lastPasswordInput;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button nextButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private Label inputPrompt;
    @FXML
    private Label badUsernameOrPassword;
    @FXML
    private Label badEmail;
    @FXML
    private Label usernameExist;
    @FXML
    private SplitPane createNewAccountScreen;
    @FXML
    private Label badPhone;

    public void getInfo() {
        if (usernameField.getText().equals(lastUserNameInput) && passwordField.getText().equals(lastPasswordInput)) {
            return;
        }
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || emailField.getText().isEmpty()
                || nameField.getText().isEmpty() || addressField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            inputPrompt.setVisible(true);
            usernameExist.setVisible(false);
            badUsernameOrPassword.setVisible(false);
            badEmail.setVisible(false);
            badPhone.setVisible(false);
            return;
        }

        if (usernameField.getText().length() < 6 || !passwordField.getText().matches("^(?=.*?[A-Z])(?=.*?[0-9]).{6,}$")) {
            inputPrompt.setVisible(false);
            usernameExist.setVisible(false);
            badUsernameOrPassword.setVisible(true);
            badEmail.setVisible(false);
            badPhone.setVisible(false);
            lastUserNameInput = usernameField.getText();
            lastPasswordInput = passwordField.getText();
            return;
        }
        if (User.isUserNameExist(usernameField.getText())) {
            inputPrompt.setVisible(false);
            usernameExist.setVisible(true);
            badUsernameOrPassword.setVisible(false);
            lastUserNameInput = usernameField.getText();
            lastPasswordInput = passwordField.getText();
            badEmail.setVisible(false);
            badPhone.setVisible(false);
            return;
        }
        if (!emailField.getText().matches("^(.+)@(.+)$")) {
            inputPrompt.setVisible(false);
            usernameExist.setVisible(false);
            badUsernameOrPassword.setVisible(false);
            badEmail.setVisible(true);
            badPhone.setVisible(false);
            return;
        }

        if (!phoneField.getText().matches("\\d{10}")) {
            inputPrompt.setVisible(false);
            usernameExist.setVisible(false);
            badUsernameOrPassword.setVisible(false);
            badEmail.setVisible(false);
            badPhone.setVisible(true);
            return;
        }

        User.setNewUserInfo(usernameField.getText(), passwordField.getText(), nameField.getText(), addressField.getText(),
                emailField.getText(), phoneField.getText());

        lastUserNameInput = null;
        lastPasswordInput = null;

        validate();

        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(createNewAccountScreen);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(_ -> {
            Parent root;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/email_validate.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            assert root != null;
            Scene scene = new Scene(root);
            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });

    }

    private void validate() {
        new Thread(() -> Email.sendValidateEmail(User.getInstance().getEmail())).start();
    }

    public void returnToLogin() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(createNewAccountScreen);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(_ -> {
            Parent root;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/login.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            assert root != null;
            Scene scene = new Scene(root);
            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });
    }

    public void initialize() {
        Tooltip userNameTooltip = new Tooltip();
        userNameTooltip.setText("Tên đăng nhập có độ dài từ 6 đến 15 kí tự và không chứa kí tự đặc biệt.");
        usernameField.setTooltip(userNameTooltip);

        Tooltip passwordTooltip = new Tooltip();
        passwordTooltip.setText("Mật khẩu có tối thiểu 6 kí tự, chứa ít nhất một chữ cái in hoa và chứa ít nhất một chữ số");
        passwordField.setTooltip(passwordTooltip);
    }

}
