package com.cozyspace.librarymanagement.controller;

import com.cozyspace.librarymanagement.Main;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class AccountRelatedController {
    protected final void modifyMainButtonStyle(Button mainButton) {
        final String IDLE_MAIN_BUTTON_STYLE = """
                -fx-text-fill: #ffffff;
                -fx-background-color: #0e4ed5;
                -fx-border-radius: 20;
                -fx-background-radius: 20;
                -fx-padding: 5;
                """;
        final String HOVERED_MAIN_BUTTON_STYLE = """
                -fx-text-fill: #ffffff;
                -fx-background-color: #043ea8;
                -fx-border-radius: 20;
                -fx-background-radius: 20;
                -fx-padding: 5;
                """;

        mainButton.setStyle(IDLE_MAIN_BUTTON_STYLE);
        mainButton.setOnMouseEntered(_ -> mainButton.setStyle(HOVERED_MAIN_BUTTON_STYLE));
        mainButton.setOnMouseExited(_ -> mainButton.setStyle(IDLE_MAIN_BUTTON_STYLE));

    }

    protected final void modifySubButtonStyle(Button subButton) {
        final String IDLE_SUB_BUTTON_STYLE = """
                -fx-text-fill: #043ea8;
                -fx-background-color: transparent;
                -fx-border-radius: 20;
                -fx-background-radius: 20;
                -fx-padding: 5;
                """;
        final String HOVERED_SUB_BUTTON_STYLE = """
                -fx-text-fill: #043ea8;
                -fx-background-color: #ece9e9;
                -fx-border-radius: 20;
                -fx-background-radius: 20;
                -fx-padding: 5;
                """;
        subButton.setStyle(IDLE_SUB_BUTTON_STYLE);
        subButton.setOnMouseEntered(_ -> subButton.setStyle(HOVERED_SUB_BUTTON_STYLE));
        subButton.setOnMouseExited(_ -> subButton.setStyle(IDLE_SUB_BUTTON_STYLE));
    }

    protected final void fadeTransition(Node from, String to, int duration) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(duration));
        fadeTransition.setNode(from);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(_ -> {
            Parent root;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(to)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            assert root != null;
            Scene scene = new Scene(root);
            Stage stage = (Stage) from.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });
    }

}
