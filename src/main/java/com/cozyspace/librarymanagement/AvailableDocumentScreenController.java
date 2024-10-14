package com.cozyspace.librarymanagement;

import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.user.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AvailableDocumentScreenController {
    @FXML
    private TableColumn<Document, String> titleColumn;
    @FXML
    private TableColumn<Document, String> authorColumn;
    @FXML
    private TableColumn<Document, String> typeColumn;
    @FXML
    private TableColumn<Document, String> quantityColumn;
    @FXML
    private TableView<Document> table;

    public void initialize() {
        ObservableList<Document> result = User.viewAllAvailableDocument();
        table.getItems().setAll(result);
        titleColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getTitle()));
        authorColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getAuthor()));
        typeColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getType()));
        quantityColumn.setCellValueFactory(i -> new SimpleStringProperty(((Integer) i.getValue().getQuantity()).toString()));
    }
}
