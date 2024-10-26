package com.cozyspace.librarymanagement.controller;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class UserMainScreenController {
    @FXML
    private Label home;

    /**
     * Xử lí khi người dùng ấn vào nút "Tìm kiếm"
     */
    public void search() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("search_screen.fxml"));
        Pane searchView = null;
        try {
            searchView = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        BorderPane homePane = (BorderPane) home.getScene().getRoot();
        homePane.setCenter(searchView);
    }

    public void showAllAvailableDocument() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/available_document_screen.fxml"));
        Pane view = null;
        try {
            view = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        BorderPane homePane = (BorderPane) home.getScene().getRoot();
        homePane.setCenter(view);
    }

    public void logout() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(home.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(Main.class
                .getResource("fxml/logout_confirmation.fxml")));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        dialog.setTitle("Xác nhận");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        var result = dialog.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {

            User.logout();
            Parent root = null;
            Stage stage = (Stage) home.getScene().getWindow();

            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/login.fxml")));

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            assert root != null;
            Scene scene = new Scene(root, 800, 350);
            stage.setScene(scene);
            stage.show();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        }

    }
}
