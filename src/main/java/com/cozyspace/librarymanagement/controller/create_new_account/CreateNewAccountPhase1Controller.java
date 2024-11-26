package com.cozyspace.librarymanagement.controller.create_new_account;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.controller.AccountRelatedController;
import com.cozyspace.librarymanagement.email.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;

public class CreateNewAccountPhase1Controller extends AccountRelatedController {
    @FXML
    private SplitPane createNewAccountPhase1;
    @FXML
    private TextField emailField;
    @FXML
    private Button continueButton;
    @FXML
    private Label inputPrompt;
    @FXML
    private Label emailFailedPrompt;

    public void initialize() {
        modifyMainButtonStyle(continueButton);

    }

    public void checkAndContinue() {
        if (emailField.getText().isEmpty()) {
            emailFailedPrompt.setVisible(false);
            inputPrompt.setVisible(true);
            return;
        }

        if (!emailField.getText().matches("^(.+)@(.+)$")) {
            inputPrompt.setVisible(false);
            emailFailedPrompt.setVisible(true);
            return;
        }

        DataTransfer.getInstance().getDataMap().put("newUserEmail", emailField.getText());
        new Thread(() -> Email.sendEmailValidationCode(emailField.getText())).start();

        fadeTransition(createNewAccountPhase1, "fxml/create_new_account/create_new_account_phase_2.fxml", 500);
    }

    public void back() {
        fadeTransition(createNewAccountPhase1, "fxml/login/login_phase_1.fxml", 500);
    }
}
