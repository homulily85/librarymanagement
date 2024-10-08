package com.cozyspace.librarymanagement;

import com.cozyspace.librarymanagement.datasource.AccountDatasource;
import com.cozyspace.librarymanagement.user.Account;
import com.cozyspace.librarymanagement.user.Librarian;
import com.cozyspace.librarymanagement.user.Member;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private VBox loginVbox;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField idField;

    public void initialize() {
        // Tắt nút đăng nhăp khi trường tên đăng nhập và mật khẩu rỗng
        loginButton.disableProperty()
                .bind(Bindings.isEmpty(idField.textProperty()).or(passwordField.textProperty().isEmpty()));
    }

    /**
     * Xử lí màn hình sau khi người dùng ấn vào nút "Đăng nhập"
     */
    public void handleLoginAction() {
        List<String> userInfor = AccountDatasource.getAccountInfo(idField.getText(), passwordField.getText());
        if (userInfor==null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(loginVbox.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(getClass().getResource("authenticate_fail_dialog.fxml")));
            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            dialog.setTitle("Lỗi");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.showAndWait();
        } else {

            if (userInfor.get(AccountDatasource.INDEX_COLUMN_ROLE - 1).equals("Member")) {
                Member.getInstance().setInfo(userInfor);
            } else {
                Librarian.getInstance().setInfo(userInfor);
            }
            Stage stage = (Stage) loginButton.getScene().getWindow();

            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main_screen.fxml")));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }
    }

}