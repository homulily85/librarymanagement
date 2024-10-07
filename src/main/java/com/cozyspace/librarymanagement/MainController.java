package com.cozyspace.librarymanagement;

import com.cozyspace.librarymanagement.user.Account;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class MainController {
    @FXML
    private Button loginButton;
    @FXML
    private VBox loginVbox;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField idField;

    Account admin = new Account("admin", "admin");

    public void initialize() {
        // Tắt nút đăng nhăp khi trường tên đăng nhập và mật khẩu rỗng
        loginButton.disableProperty()
                .bind(Bindings.isEmpty(idField.textProperty()).or(passwordField.textProperty().isEmpty()));
    }

    /**
     * Kiểm tra thông tin đăng nhập của người dùng.
     * Phương thức được gọi khi người dúng ấn vào nút "Đăng nhập".
     *
     * @return true nếu thông tin khớp với một tài khoản trong CSDL
     */
    private boolean validateAccount() {
        return idField.getText().equals(admin.getId())
                && passwordField.getText().equals(admin.getPassword());
    }

    public void handleLoginAction() {
        if (!validateAccount()) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(loginVbox.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(getClass().getResource("authenticity_fail_dialog.fxml")));
            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            dialog.setTitle("Lỗi");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.showAndWait();
        }
    }

}