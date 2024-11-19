package com.cozyspace.librarymanagement.controller.librarian;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.user.Librarian;
import com.cozyspace.librarymanagement.user.Member;
import com.cozyspace.librarymanagement.user.SearchBook;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoadingScreenController {
    @FXML
    private AnchorPane pane;

    public void initialize() {
        new LoadingScreen().start();
    }

    class LoadingScreen extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                Platform.runLater(() -> {
                    Parent root = null;

                    if (UserManager.getUserInstance() instanceof Member) {
                        try {
                            root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/member/member_main_screen.fxml")));
                            DataTransfer.getInstance().getDataMap().put("searchMode", Integer.toString(SearchBook.SEARCH_ALL_AVAILABLE_DOCUMENT));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (UserManager.getUserInstance() instanceof Librarian) {
                        try {
                            root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/librarian/librarian_main_screen.fxml")));
                            DataTransfer.getInstance().getDataMap().put("searchMode", Integer.toString(SearchBook.SEARCH_ALL_DOCUMENT));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.getIcons().add(new Image(String.valueOf(Main.class.getResource("icon/program_icon.png"))));
                    stage.setTitle("Library Management System");

                    stage.show();

                    pane.getScene().getWindow().hide();

                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
