package com.cozyspace.librarymanagement.controller.member;

import com.cozyspace.librarymanagement.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class MemberMainScreenController {
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
}
