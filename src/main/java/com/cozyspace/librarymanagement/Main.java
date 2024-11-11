package com.cozyspace.librarymanagement;

import com.cozyspace.librarymanagement.datasource.Datasource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/login/login_phase_1.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(String.valueOf(Main.class.getResource("book.png"))));
        stage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI());
        //TODO: Only enable this line when running the jar file
        DataTransfer.getInstance().getDataMap().put("jarPath", f.getParent());
        System.out.println("Jar Path: " + f.getParent());
        Datasource.openConnection();
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