package com.cozyspace.librarymanagement.controller.game;

import com.cozyspace.librarymanagement.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class ChooseGameController {
    @FXML
    private BorderPane main;

    public void loadGameGuessBook() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/game/game_screen.fxml"));
        Pane gameView = null;
        try {
            gameView = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        main.setCenter(gameView);
    }

}
