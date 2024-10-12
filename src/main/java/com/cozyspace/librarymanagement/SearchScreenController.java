package com.cozyspace.librarymanagement;

import com.cozyspace.librarymanagement.datasource.Document;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SearchScreenController {
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

    public void search(){

    }
}
