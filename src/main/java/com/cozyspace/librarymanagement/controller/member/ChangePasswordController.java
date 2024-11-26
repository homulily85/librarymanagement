package com.cozyspace.librarymanagement.controller.member;

import com.cozyspace.librarymanagement.user.UserManager;
import com.jfoenix.controls.JFXPasswordField;
import com.password4j.Password;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

public class ChangePasswordController {
    @FXML
    private JFXPasswordField oldPasswordField;
    @FXML
    private JFXPasswordField newPasswordField;
    @FXML
    private JFXPasswordField newPasswordRepeatField;
    @FXML
    private Label inputPrompt;
    @FXML
    private Label oldPasswordFail;
    @FXML
    private Label inputFail;
    @FXML
    private Label reInputFail;

    public void initialize() {
        Tooltip passwordTooltip = new Tooltip();
        passwordTooltip.setText("Mật khẩu có tối thiểu 6 kí tự, chứa ít nhất một chữ cái in hoa và chứa ít nhất một chữ số");
        newPasswordField.setTooltip(passwordTooltip);
    }

    public void changePassword() {
        if (newPasswordField.getText().isEmpty() || oldPasswordField.getText().isEmpty() || newPasswordRepeatField.getText().isEmpty()) {
            inputFail.setVisible(false);
            inputPrompt.setVisible(true);
            oldPasswordFail.setVisible(false);
            reInputFail.setVisible(false);
            return;
        }

        if (!Password.hash(oldPasswordField.getText()).addSalt("1").withArgon2().getResult()
                .equals(UserManager.getUserInstance().getInfo().getPassword())) {
            inputFail.setVisible(false);
            inputPrompt.setVisible(false);
            reInputFail.setVisible(false);
            oldPasswordFail.setVisible(true);
            return;
        }

        if (!newPasswordField.getText().matches("^(?=.*?[A-Z])(?=.*?[0-9]).{6,}$")) {
            inputFail.setVisible(true);
            inputPrompt.setVisible(false);
            reInputFail.setVisible(false);
            oldPasswordFail.setVisible(false);
            return;
        }

        if (!newPasswordField.getText().equals(newPasswordRepeatField.getText())) {
            inputFail.setVisible(false);
            inputPrompt.setVisible(false);
            reInputFail.setVisible(true);
            oldPasswordFail.setVisible(false);
            return;
        }


        new Thread(() -> {
            UserManager.updatePassword(UserManager.getUserInstance().getInfo().getUsername(), newPasswordField.getText());
        }).start();

        Stage stage = (Stage) oldPasswordField.getScene().getWindow();
        stage.close();

        Notifications.create().title("Thành công").text("Đổi mật khẩu thành công").showInformation();

    }
}
