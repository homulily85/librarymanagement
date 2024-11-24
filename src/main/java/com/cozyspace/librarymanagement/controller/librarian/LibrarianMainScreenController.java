package com.cozyspace.librarymanagement.controller.librarian;

import com.cozyspace.librarymanagement.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LibrarianMainScreenController {

    @FXML
    private BorderPane home;
    @FXML
    private TabPane tabPane;
    @FXML
    private BorderPane requestManagement;
    @FXML
    private BorderPane memberManagement;
    @FXML
    private BorderPane documentMainScreen;
    @FXML
    private BorderPane gameScreen;

    public void initialize() {

        FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("fxml/librarian/document/document_main_screen.fxml"));
        Pane temp1 = null;
        try {
            temp1 = loader1.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        documentMainScreen.setCenter(temp1);

        FXMLLoader loader2 = new FXMLLoader(Main.class.getResource("fxml/librarian/member_management.fxml"));
        Pane temp2 = null;
        try {
            temp2 = loader2.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        memberManagement.setCenter(temp2);

        FXMLLoader loader3 = new FXMLLoader(Main.class.getResource("fxml/librarian/borrow_request_management.fxml"));
        Pane temp3 = null;
        try {
            temp3 = loader3.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestManagement.setCenter(temp3);

        FXMLLoader loader4 = new FXMLLoader(Main.class.getResource("fxml/librarian/home.fxml"));
        Pane temp4 = null;
        try {
            temp4 = loader4.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        home.setCenter(temp4);
        FXMLLoader loader5 = new FXMLLoader(Main.class.getResource("fxml/game/game_screen.fxml"));
        Pane temp5 = null;
        try {
            temp5 = loader5.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameScreen.setCenter(temp5);

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (_, _, t1) -> {
                    if (t1.getText().equals("Trang chủ")) {
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/librarian/home.fxml"));
                        Pane temp = null;
                        try {
                            temp = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        home.setCenter(temp);
                    }
                });
    }
}