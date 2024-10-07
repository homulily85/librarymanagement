package com.cozyspace.librarymanagement;

import com.cozyspace.librarymanagement.user.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MainController {

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField idField;

    Account admin = new Account("admin", "admin");

    public boolean validateAccount() {
        if (idField.getText().equals(admin.getId())
                && passwordField.getText().equals(admin.getPassword())) {
            System.out.println("Correct");
            return true;
        } else {
            System.out.println("Incorrect");
            return false;
        }
    }

}