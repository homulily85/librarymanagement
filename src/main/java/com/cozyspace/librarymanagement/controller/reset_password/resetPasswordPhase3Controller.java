package com.cozyspace.librarymanagement.controller.reset_password;

import com.cozyspace.librarymanagement.controller.AccountRelatedController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;

public class resetPasswordPhase3Controller extends AccountRelatedController {
    @FXML
    private Button continueButton;
    @FXML
    private SplitPane resetPasswordPhase3;

    public void initialize() {
        modifyMainButtonStyle(continueButton);

    }

    public void next() {
        fadeTransition(resetPasswordPhase3, "fxml/login/login_phase_1.fxml", 500);
    }

    public void back() {
        fadeTransition(resetPasswordPhase3, "fxml/reset_password/reset_password_phase_2.fxml", 500);
    }
}