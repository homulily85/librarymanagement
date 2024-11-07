package com.cozyspace.librarymanagement.controller.librarian.document;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.user.SearchBook;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class DocumentMainScreenController {
    @FXML
    private Button showAllDocumentButton;
    @FXML
    private Button showAllAvailableDocumentButton;
    @FXML
    private Button showAllUnavailableDocumentButton;
    @FXML
    private BorderPane documentMainScreen;

    public void initialize() {

        final String IDLE_MAIN_BUTTON_STYLE = """
                -fx-text-fill: #ffffff;
                -fx-background-color: #0e4ed5;
                """;
        final String HOVERED_MAIN_BUTTON_STYLE = """
                -fx-text-fill: #ffffff;
                -fx-background-color: #043ea8;
                """;

        showAllDocumentButton.setStyle(IDLE_MAIN_BUTTON_STYLE);
        showAllDocumentButton.setOnMouseEntered(_ -> showAllDocumentButton.setStyle(HOVERED_MAIN_BUTTON_STYLE));
        showAllDocumentButton.setOnMouseExited(_ -> showAllDocumentButton.setStyle(IDLE_MAIN_BUTTON_STYLE));

        showAllAvailableDocumentButton.setStyle(IDLE_MAIN_BUTTON_STYLE);
        showAllAvailableDocumentButton.setOnMouseEntered(_ -> showAllAvailableDocumentButton.setStyle(HOVERED_MAIN_BUTTON_STYLE));
        showAllAvailableDocumentButton.setOnMouseExited(_ -> showAllAvailableDocumentButton.setStyle(IDLE_MAIN_BUTTON_STYLE));

        showAllUnavailableDocumentButton.setStyle(IDLE_MAIN_BUTTON_STYLE);
        showAllUnavailableDocumentButton.setOnMouseEntered(_ -> showAllUnavailableDocumentButton.setStyle(HOVERED_MAIN_BUTTON_STYLE));
        showAllUnavailableDocumentButton.setOnMouseExited(_ -> showAllUnavailableDocumentButton.setStyle(IDLE_MAIN_BUTTON_STYLE));

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
