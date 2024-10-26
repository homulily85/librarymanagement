package com.cozyspace.librarymanagement.controller;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.user.User;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SearchScreenController {
    @FXML
    private TableColumn<Document, String> isbnColumn;
    @FXML
    private TableColumn<Document, String> typeColumn;
    @FXML
    private TableColumn<Document, String> authorColumn;
    @FXML
    private TableColumn<Document, String> titleColumn;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private ComboBox<String> searchType;
    @FXML
    private Label documentNotFound;
    @FXML
    private TableView<Document> table;

    public void initialize() {
        searchButton.disableProperty()
                .bind(Bindings.isEmpty(searchField.textProperty()));
        table.setRowFactory(_ -> {
            TableRow<Document> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    row.setOnMouseClicked(_ -> {
                        Stage newStage = new Stage();

                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(Objects.requireNonNull(Main.class.getResource("fxml/document_info.fxml")));

                        Scene scene = null;
                        try {
                            scene = new Scene(fxmlLoader.load(), 900, 600);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                        newStage.setTitle("Thông tin tài liệu");
                        newStage.setScene(scene);
                        DocumentInfoController controller = fxmlLoader.getController();
                        controller.setInfo(table.getSelectionModel().getSelectedItem());
                        newStage.setResizable(false);
                        newStage.requestFocus();
                        newStage.initOwner(table.getScene().getWindow());
                        newStage.initModality(Modality.WINDOW_MODAL);
                        newStage.show();

                    });
                }
            });
            return row;
        });
    }

    public void search() {
        documentNotFound.setVisible(false);
        table.setVisible(false);
        String query = searchField.getText();
        ObservableList<Document> result = null;
        switch (searchType.getSelectionModel().getSelectedIndex()) {
            case 0 -> result = User.getInstance().searchDocumentByTitle(query);
            case 1 -> result = User.getInstance().searchDocumentByAuthor(query);
            case 2 -> result = User.getInstance().searchByISBN(query);
        }
        if (result == null || result.isEmpty()) {
            documentNotFound.setVisible(true);
        } else {
            table.getItems().setAll(result);
            isbnColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getISBN()));
            titleColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getTitle()));
            authorColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getAuthor()));
            typeColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getType()));
            table.setVisible(true);
        }

    }
}
