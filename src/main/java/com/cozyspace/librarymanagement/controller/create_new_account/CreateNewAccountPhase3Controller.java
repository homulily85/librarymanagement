package com.cozyspace.librarymanagement.controller.create_new_account;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.controller.AccountRelatedController;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CreateNewAccountPhase3Controller extends AccountRelatedController {
    @FXML
    private Label usernameExist;
    @FXML
    private SplitPane createNewAccountPhase3;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField repeatPasswordField;
    @FXML
    private Button continueButton;
    @FXML
    private Label inputPrompt;
    @FXML
    private Label usernameFailPrompt;
    @FXML
    private Label passwordFailPrompt;
    @FXML
    private Label resetPasswordFailPrompt;

    public void initialize() {
        modifyMainButtonStyle(continueButton);
        Tooltip userNameTooltip = new Tooltip();
        userNameTooltip.setText("Tên đăng nhập có độ dài từ 6 đến 15 kí tự và không chứa kí tự đặc biệt.");
        usernameField.setTooltip(userNameTooltip);

        Tooltip passwordTooltip = new Tooltip();
        passwordTooltip.setText("Mật khẩu có tối thiểu 6 kí tự, chứa ít nhất một chữ cái in hoa và chứa ít nhất một chữ số");
        passwordField.setTooltip(passwordTooltip);

    }

    public void checkAndContinue() {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            inputPrompt.setVisible(true);
            usernameExist.setVisible(false);
            usernameFailPrompt.setVisible(false);
            passwordFailPrompt.setVisible(false);
            resetPasswordFailPrompt.setVisible(false);
            return;
        }

        if (usernameField.getText().length() < 6) {
            inputPrompt.setVisible(false);
            usernameExist.setVisible(false);
            usernameFailPrompt.setVisible(true);
            passwordFailPrompt.setVisible(false);
            resetPasswordFailPrompt.setVisible(false);
            return;
        }
        if (UserManager.isUserNameExist(usernameField.getText())) {
            inputPrompt.setVisible(false);
            usernameExist.setVisible(true);
            usernameFailPrompt.setVisible(false);
            passwordFailPrompt.setVisible(false);
            resetPasswordFailPrompt.setVisible(false);
            return;
        }
        if (!passwordField.getText().matches("^(?=.*?[A-Z])(?=.*?[0-9]).{6,}$")) {
            inputPrompt.setVisible(false);
            usernameExist.setVisible(false);
            usernameFailPrompt.setVisible(false);
            passwordFailPrompt.setVisible(true);
            resetPasswordFailPrompt.setVisible(false);
            return;
        }

        if (!repeatPasswordField.getText().equals(passwordField.getText())) {
            inputPrompt.setVisible(false);
            usernameExist.setVisible(false);
            usernameFailPrompt.setVisible(false);
            passwordFailPrompt.setVisible(false);
            resetPasswordFailPrompt.setVisible(true);
            return;
        }

        DataTransfer.getInstance().getDataMap().put("newUsername", usernameField.getText());
        DataTransfer.getInstance().getDataMap().put("newUserPassword", passwordField.getText());

        fadeTransition(createNewAccountPhase3, "fxml/create_new_account/create_new_account_phase_4.fxml", 500);
    }

    public void back() {
        fadeTransition(createNewAccountPhase3, "fxml/create_new_account/create_new_account_phase_2.fxml", 500);
    }
}
