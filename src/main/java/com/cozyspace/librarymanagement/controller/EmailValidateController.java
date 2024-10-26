package com.cozyspace.librarymanagement.controller;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class EmailValidateController {
    @FXML
    private Button nextButton;
    @FXML
    private Label inputPrompt;
    @FXML
    private TextField codeField;
    @FXML
    private Label wrongCode;
    @FXML
    private SplitPane emailValidate;

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

        nextButton.setStyle(IDLE_BUTTON_STYLE);
        nextButton.setOnMouseEntered(_ -> nextButton.setStyle(HOVERED_BUTTON_STYLE));
        nextButton.setOnMouseExited(_ -> nextButton.setStyle(IDLE_BUTTON_STYLE));
    }

    public void codeCheck() {
        if (codeField.getText().isEmpty()) {
            inputPrompt.setVisible(true);
            wrongCode.setVisible(false);
            return;
        }

        if (Integer.parseInt(codeField.getText()) != User.getCode()) {
            inputPrompt.setVisible(false);
            wrongCode.setVisible(true);
            return;
        }

        User.addNewMember();

        Parent root = null;
        Stage stage = (Stage) emailValidate.getScene().getWindow();
        try {
            root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/create_account_success.fxml")));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assert root != null;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
