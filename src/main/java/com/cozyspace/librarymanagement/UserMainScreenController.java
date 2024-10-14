package com.cozyspace.librarymanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class UserMainScreenController {
    @FXML
    private Label home;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Label search;

    /**
     * Xử lí khi người dùng ấn vào nút "Tìm kiếm"
     */
    public void search() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("search_screen.fxml"));
        Pane searchView = null;
        try {
            searchView = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        BorderPane homePane = (BorderPane) home.getScene().getRoot();
        homePane.setCenter(searchView);
    }
}
