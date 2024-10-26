package com.cozyspace.librarymanagement;

import com.cozyspace.librarymanagement.datasource.Datasource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800,500);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(String.valueOf(Main.class.getResource("book.png"))));
        stage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        Datasource.openConection();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Datasource.closeConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}