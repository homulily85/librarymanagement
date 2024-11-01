package com.cozyspace.librarymanagement.controller.librarian;

import com.cozyspace.librarymanagement.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LibrarianMainScreenController {
    @FXML
    private BorderPane documentMainScreen;

    public void initialize() {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/librarian/document/document_main_screen.fxml"));
        Pane temp = null;
        try {
            temp = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        documentMainScreen.setCenter(temp);
    }
}
