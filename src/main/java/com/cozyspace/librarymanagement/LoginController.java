package com.cozyspace.librarymanagement;

import com.cozyspace.librarymanagement.user.Librarian;
import com.cozyspace.librarymanagement.user.Member;
import com.cozyspace.librarymanagement.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
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
                root = FXMLLoader.load(Objects.requireNonNull(getClass().
                        getResource("member_main_screen.fxml")));

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else if (User.getInstance() instanceof Librarian) {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass()
                        .getResource("librarian_main_screen.fxml")));
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

}