package com.cozyspace.librarymanagement.controller.create_new_account;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.controller.AccountRelatedController;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;

public class CreateNewAccountPhase4Controller extends AccountRelatedController {
    @FXML
    private SplitPane createNewAccountPhase4;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private Button continueButton;
    @FXML
    private Label inputPrompt;
    @FXML
    private Label phoneFailedPrompt;

    public void initialize() {
        modifyMainButtonStyle(continueButton);
    }

    public void checkAndContinue() {
        if (nameField.getText().isEmpty() || addressField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            phoneFailedPrompt.setVisible(false);
            inputPrompt.setVisible(true);
            return;
        }

        if (!phoneField.getText().matches("\\d{10}")) {
            inputPrompt.setVisible(false);
            phoneFailedPrompt.setVisible(true);
            return;
        }
        UserManager.createNewUser(DataTransfer.getInstance().getDataMap().get("newUsername"),
                DataTransfer.getInstance().getDataMap().get("newUserPassword"),
                nameField.getText(), addressField.getText(),
                DataTransfer.getInstance().getDataMap().get("newUserEmail"),
                phoneField.getText(), "Member");

        fadeTransition(createNewAccountPhase4,"fxml/create_new_account/create_new_account_phase_5.fxml",500);
    }
}
