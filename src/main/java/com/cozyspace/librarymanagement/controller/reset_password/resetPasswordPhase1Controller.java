package com.cozyspace.librarymanagement.controller.reset_password;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.controller.AccountRelatedController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;

public class resetPasswordPhase1Controller extends AccountRelatedController {
    @FXML
    private SplitPane resetPasswordPhase1;
    @FXML
    private Label descriptionLabel;
    @FXML
    private TextField codeField;
    @FXML
    private Button continueButton;
    @FXML
    private Label inputPrompt;
    @FXML
    private Label loginFailedPrompt;

    public void initialize() {
        descriptionLabel.setText("""
                Chúng tôi vừa gửi email chứa mã xác minh đến địa chỉ email của bạn là %s. Vui lòng kiểm tra email này để lấy mã và nhập vào bên dưới.
                """.formatted(DataTransfer.getInstance().getDataMap().get("userEmail")));
        modifyMainButtonStyle(continueButton);

    }

    public void checkAndContinue() {
        if (codeField.getText().isEmpty()) {
            loginFailedPrompt.setVisible(false);
            inputPrompt.setVisible(true);
            return;
        }

        if (!codeField.getText().equals(DataTransfer.getInstance().getDataMap().get("resetPasswordCode"))) {
            inputPrompt.setVisible(false);
            loginFailedPrompt.setVisible(true);
            return;
        }

        fadeTransition(resetPasswordPhase1, "fxml/reset_password/reset_password_phase_2.fxml", 500);

    }

}
