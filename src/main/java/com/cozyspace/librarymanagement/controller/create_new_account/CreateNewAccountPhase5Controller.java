package com.cozyspace.librarymanagement.controller.create_new_account;

import com.cozyspace.librarymanagement.controller.AccountRelatedController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;

public class CreateNewAccountPhase5Controller extends AccountRelatedController {
    @FXML
    private SplitPane createNewAccountPhase5;
    @FXML
    private Button continueButton;

    public void initialize() {
        modifyMainButtonStyle(continueButton);
    }

    public void next() {
        fadeTransition(createNewAccountPhase5, "fxml/login/login_phase_1.fxml", 500);
    }
}
