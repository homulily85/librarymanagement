package com.cozyspace.librarymanagement.controller.login;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.controller.AccountRelatedController;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginPhase1Controller extends AccountRelatedController {
    @FXML
    private Button forgotButton;
    @FXML
    private SplitPane loginPhase1Screen;
    private String lastUserNameInput;
    @FXML
    public Button signInButton;
    @FXML
    private Label loginFailedPrompt;
    @FXML
    private Label inputPrompt;
    @FXML
    private TextField usernameField;

    public void initialize() {
        modifyMainButtonStyle(signInButton);
        modifySubButtonStyle(forgotButton);
    }

    public void checkAndContinue() {
        if (usernameField.getText().isEmpty()) {
            loginFailedPrompt.setVisible(false);
            inputPrompt.setVisible(true);
            return;
        }
        if (usernameField.getText().equals(lastUserNameInput)) {
            return;
        }
        if (!UserManager.isUserNameExist(usernameField.getText())) {
            lastUserNameInput = usernameField.getText();
            inputPrompt.setVisible(false);
            loginFailedPrompt.setVisible(true);
            return;
        }

        DataTransfer.getInstance().getDataMap().put("loginUsername", usernameField.getText());

        fadeTransition(loginPhase1Screen, "fxml/login/login_phase_2.fxml", 500);

    }

    public void changeToCreateNewAccount() {
        fadeTransition(loginPhase1Screen, "fxml/create_new_account/create_new_account_phase_1.fxml", 500);

    }
}