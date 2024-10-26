package com.cozyspace.librarymanagement.controller;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.user.Librarian;
import com.cozyspace.librarymanagement.user.Member;
import com.cozyspace.librarymanagement.user.User;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    private SplitPane loginScreen;
    private String lastUserNameInput;
    private String lastPasswordInput;
    @FXML
    public Button loginButton;
    @FXML
    public PasswordField passwordField;
    @FXML
    private Label loginFailedPrompt;
    @FXML
    private Label inputPrompt;
    @FXML
    private TextField usernameField;

    public void login() {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            loginFailedPrompt.setVisible(false);
            inputPrompt.setVisible(true);
            return;
        }
        if (usernameField.getText().equals(lastUserNameInput) && passwordField.getText().equals(lastPasswordInput)) {
            return;
        }
        boolean loginSuccess = User.login(usernameField.getText(), passwordField.getText());
        if (!loginSuccess) {
            lastUserNameInput = usernameField.getText();
            lastPasswordInput = passwordField.getText();
            inputPrompt.setVisible(false);
            loginFailedPrompt.setVisible(true);
            return;
        }

        lastPasswordInput = null;
        lastUserNameInput = null;

        Parent root = null;
        Stage stage = (Stage) loginButton.getScene().getWindow();

        if (User.getInstance() instanceof Member) {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/member_main_screen.fxml")));

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else if (User.getInstance() instanceof Librarian) {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/librarian_main_screen.fxml")));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        assert root != null;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public void changeToCreateNewAccount() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(loginScreen);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(_ -> {
            Parent root;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/create_new_account.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            assert root != null;
            Scene scene = new Scene(root);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });
    }
}