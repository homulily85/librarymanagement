package com.cozyspace.librarymanagement.controller.reset_password;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.controller.AccountRelatedController;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class resetPasswordPhase2Controller extends AccountRelatedController {
    @FXML
    private SplitPane resetPasswordPhase2;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private Label inputPrompt;
    @FXML
    private Label inputFail;
    @FXML
    private Label reInputFail;
    @FXML
    private Button continueButton;

    public void initialize() {
        modifyMainButtonStyle(continueButton);
    }


    @FXML
    private void checkAndContinue() {
        if (newPasswordField.getText().isEmpty() || repeatPasswordField.getText().isEmpty()) {
            inputFail.setVisible(false);
            inputPrompt.setVisible(true);
            reInputFail.setVisible(false);
            return;
        }

        if (!newPasswordField.getText().matches("^(?=.*?[A-Z])(?=.*?[0-9]).{6,}$")) {
            inputFail.setVisible(true);
            inputPrompt.setVisible(false);
            reInputFail.setVisible(false);
            return;
        }

        if (!newPasswordField.getText().equals(repeatPasswordField.getText())) {
            inputFail.setVisible(false);
            inputPrompt.setVisible(false);
            reInputFail.setVisible(true);
            return;
        }

        UserManager.updatePassword(DataTransfer.getInstance().getDataMap().get("loginUsername"), newPasswordField.getText());

        fadeTransition(resetPasswordPhase2, "fxml/reset_password/reset_password_phase_3.fxml", 500);

    }
}

