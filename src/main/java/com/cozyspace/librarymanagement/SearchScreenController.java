package com.cozyspace.librarymanagement;

import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.user.User;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SearchScreenController {
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
    }

    public void search() {
        documentNotFound.setVisible(false);
        table.setVisible(false);
        String query = searchField.getText();
        ObservableList<Document> result = null;
        if (searchType.getSelectionModel().getSelectedIndex() == 0) {
            result = User.searchDocumentByTitle(query);
        } else {
            result = User.searchDocumentByAuthor(query);
        }
        if (result == null || result.isEmpty()) {
            documentNotFound.setVisible(true);
        } else {
            table.getItems().setAll(result);
            titleColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getTitle()));
            authorColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getAuthor()));
            typeColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getType()));
            table.setVisible(true);
        }

    }
}
