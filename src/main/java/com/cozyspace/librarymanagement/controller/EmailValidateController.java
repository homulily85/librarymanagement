package com.cozyspace.librarymanagement.controller;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class EmailValidateController {
    @FXML
    private Label inputPrompt;
    @FXML
    private TextField codeField;
    @FXML
    private Label wrongCode;
    @FXML
    private SplitPane emailValidate;

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
