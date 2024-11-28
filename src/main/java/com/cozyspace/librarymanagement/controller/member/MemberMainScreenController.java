package com.cozyspace.librarymanagement.controller.member;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.user.UserManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MemberMainScreenController {
    @FXML
    private StackPane stackPane;
    @FXML
    private BorderPane main;

    public void initialize() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/member/home.fxml"));
        Pane homeView = null;
        try {
            homeView = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        main.setCenter(homeView);
    }

    public void loadHome() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/member/home.fxml"));
        Pane homeView = null;
        try {
            homeView = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        main.setCenter(homeView);
    }

    public void loadDocument() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/member/document/document_main_screen.fxml"));
        Pane documentView = null;
        try {
            documentView = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        main.setCenter(documentView);
    }

    public void loadAccount() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/member/account_info.fxml"));
        Pane accountView = null;
        try {
            accountView = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        main.setCenter(accountView);
    }

    public void logout() {
        JFXDialogLayout content = new JFXDialogLayout();
        var heading = new Label("Đăng xuất");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        content.setHeading(heading);
        content.setBody(new Label("Bạn có chắc chắn muốn đăng xuất?"));
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

        JFXButton okButton = new JFXButton("Xác nhận");
        String css = Main.class.getResource("css/button_type_2.css").toExternalForm();
        okButton.getStylesheets().add(css);

        JFXButton cancelButton = new JFXButton("Hủy");
        cancelButton.getStylesheets().add(css);

        cancelButton.setOnAction(_ -> dialog.close());

        okButton.setOnAction(_ -> {
            UserManager.removeUserInstance();
            dialog.close();
            Parent root = null;
            Stage stage = (Stage) stackPane.getScene().getWindow();

            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/login/login_phase_1.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        });

        content.setActions(okButton, cancelButton);
        dialog.show();
    }

    public void loadHistory() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource
                ("fxml/member/request_history.fxml"));
        Pane requestHistoryView = null;
        try {
            requestHistoryView = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        main.setCenter(requestHistoryView);
    }
}
