package com.cozyspace.librarymanagement.controller;

import com.cozyspace.librarymanagement.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CreateAccountSuccessController {
    @FXML
    private SplitPane splitPane;

    public void next() {
        Parent root = null;
        Stage stage = (Stage) splitPane.getScene().getWindow();
        try {
            root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/member_main_screen.fxml")));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assert root != null;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }
}
