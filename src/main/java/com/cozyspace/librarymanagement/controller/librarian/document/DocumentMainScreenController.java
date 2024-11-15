package com.cozyspace.librarymanagement.controller.librarian.document;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.user.SearchBook;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class DocumentMainScreenController {
    @FXML
    private BorderPane documentMainScreen;

    public void initialize() {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/librarian/document/search_screen.fxml"));
        Pane searchView = null;
        try {
            searchView = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        documentMainScreen.setCenter(searchView);

        showAllDocument();

    }

    public void showAllDocument() {
        DataTransfer.getInstance().getDataMap().put("searchMode", Integer.toString(SearchBook.SEARCH_ALL_DOCUMENT));
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/librarian/document/search_screen.fxml"));
        Pane searchView = null;
        try {
            searchView = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        documentMainScreen.setCenter(searchView);
    }

    public void showAllAvailableDocument() {
        DataTransfer.getInstance().getDataMap().put("searchMode", Integer.toString(SearchBook.SEARCH_ALL_AVAILABLE_DOCUMENT));
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/librarian/document/search_screen.fxml"));
        Pane searchView = null;
        try {
            searchView = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        documentMainScreen.setCenter(searchView);

    }

    public void showAllUnavailableDocument() {
        DataTransfer.getInstance().getDataMap().put("searchMode", Integer.toString(SearchBook.SEARCH_ALL_UNAVAILABLE_DOCUMENT));
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/librarian/document/search_screen.fxml"));
        Pane searchView = null;
        try {
            searchView = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        documentMainScreen.setCenter(searchView);
    }
}
