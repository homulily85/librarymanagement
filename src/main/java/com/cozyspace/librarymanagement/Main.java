package com.cozyspace.librarymanagement;

import com.cozyspace.librarymanagement.datasource.AccountDatasource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        AccountDatasource.openConection();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        AccountDatasource.closeConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}