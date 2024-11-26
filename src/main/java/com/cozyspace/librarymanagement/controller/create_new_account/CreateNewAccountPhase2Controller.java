package com.cozyspace.librarymanagement.controller.create_new_account;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.controller.AccountRelatedController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;

public class CreateNewAccountPhase2Controller extends AccountRelatedController {
    @FXML
    private SplitPane createNewAccountPhase2;
    @FXML
    private TextField codeField;
    @FXML
    private Button continueButton;
    @FXML
    private Label inputPrompt;
    @FXML
    private Label codeFailedPrompt;

    public void initialize() {
        modifyMainButtonStyle(continueButton);
    }

    public void checkAndContinue() {
        if (codeField.getText().isEmpty()) {
            codeFailedPrompt.setVisible(false);
            inputPrompt.setVisible(true);
            return;
        }

        if (!codeField.getText().equals(DataTransfer.getInstance().getDataMap().get("emailValidationCode"))) {
            inputPrompt.setVisible(false);
            codeFailedPrompt.setVisible(true);
            return;
        }

        fadeTransition(createNewAccountPhase2, "fxml/create_new_account/create_new_account_phase_3.fxml", 500);
    }

    public void back() {
        fadeTransition(createNewAccountPhase2, "fxml/create_new_account/create_new_account_phase_1.fxml", 500);
    }
}
