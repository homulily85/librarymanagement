package com.cozyspace.librarymanagement.controller.librarian.document;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.user.SearchBook;
import com.cozyspace.librarymanagement.user.UserManager;
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
    private TableColumn<Document, String> idColumn;
    @FXML
    private Label removeQuery;
    @FXML
    private Button addNewDocument;
    @FXML
    private TableColumn<Document, String> subjectColumn;
    @FXML
    private TableColumn<Document, String> quantityColumn;
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

        ContextMenu contextMenu = new ContextMenu();

        MenuItem viewItem = new MenuItem("Xem thông tin");
        MenuItem editItem = new MenuItem("Chỉnh sửa thông tin");
        MenuItem deleteItem = new MenuItem("Xóa tài liệu");
        contextMenu.getItems().addAll(viewItem, editItem, deleteItem);

        table.setContextMenu(contextMenu);

        viewItem.setOnAction(_ -> showDocumentInfo());
        editItem.setOnAction(_ -> updateDocument());

        ObservableList<Document> result = UserManager.getUserInstance().viewDocument(
                Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode")));
        table.getItems().setAll(result);
        idColumn.setCellValueFactory(i -> new SimpleStringProperty(((Integer) i.getValue().getId()).toString()));
        titleColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getTitle()));
        authorColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getAuthor()));
        typeColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getType()));
        isbnColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getISBN()));
        quantityColumn.setCellValueFactory(i -> new SimpleStringProperty(((Integer) i.getValue().getQuantity()).toString()));
        subjectColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getSubject()));
        table.setVisible(true);

        removeQuery.disableProperty().bind(Bindings.isEmpty(searchField.textProperty()));

        removeQuery.setOnMouseClicked(_ -> {
            searchField.clear();
            table.getItems().setAll(result);
            documentNotFound.setVisible(false);
            table.setVisible(true);
            addNewDocument.setVisible(true);

        });

        searchButton.defaultButtonProperty().bind(searchButton.focusedProperty());

        searchButton.disableProperty()
                .bind(Bindings.isEmpty(searchField.textProperty()));
        table.setRowFactory(_ -> {
            TableRow<Document> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    row.setOnMouseClicked(_ -> showDocumentInfo());
                }
            });
            return row;
        });

        if (Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode"))
            == SearchBook.SEARCH_ALL_DOCUMENT) {
            final String IDLE_MAIN_BUTTON_STYLE = """
                    -fx-text-fill: #ffffff;
                    -fx-background-color: #0e4ed5;
                    -fx-border-radius: 20;
                    -fx-background-radius: 20;
                    -fx-padding: 5;
                    """;
            final String HOVERED_MAIN_BUTTON_STYLE = """
                    -fx-text-fill: #ffffff;
                    -fx-background-color: #043ea8;
                    -fx-border-radius: 20;
                    -fx-background-radius: 20;
                    -fx-padding: 5;
                    """;

            addNewDocument.setStyle(IDLE_MAIN_BUTTON_STYLE);
            addNewDocument.setOnMouseEntered(_ -> addNewDocument.setStyle(HOVERED_MAIN_BUTTON_STYLE));
            addNewDocument.setOnMouseExited(_ -> addNewDocument.setStyle(IDLE_MAIN_BUTTON_STYLE));
        } else {
            addNewDocument.setDisable(true);
            addNewDocument.setVisible(false);
        }
    }

    public void search() {
        documentNotFound.setVisible(false);
        table.setVisible(false);
        String query = searchField.getText();
        addNewDocument.setVisible(false);
        ObservableList<Document> result = null;
        switch (searchType.getSelectionModel().getSelectedIndex()) {
            case 0 -> result = UserManager.getUserInstance().searchDocumentByTitle(query,
                    Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode")));
            case 1 -> result = UserManager.getUserInstance().searchDocumentByAuthor(query,
                    Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode")));
            case 2 -> result = UserManager.getUserInstance().searchDocumentByISBN(query,
                    Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode")));
        }
        if (result == null || result.isEmpty()) {
            documentNotFound.setVisible(true);
        } else {
            table.getItems().setAll(result);
            table.setVisible(true);
        }

    }

    public void addNewDocument() {

        Stage newStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(Main.class.getResource
                ("fxml/librarian/document/add_new_document.fxml")));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        newStage.setTitle("Thêm tài liệu");
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.requestFocus();
        newStage.initOwner(table.getScene().getWindow());
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.showAndWait();

        DataTransfer.getInstance().getDataMap().put("searchMode", ((Integer) SearchBook.SEARCH_ALL_DOCUMENT).toString());

        ObservableList<Document> result = UserManager.getUserInstance().viewDocument(
                Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode")));

        table.setVisible(true);
        documentNotFound.setVisible(false);
        table.getItems().setAll(result);
        searchField.clear();

    }

    public void showDocumentInfo() {
        Stage newStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(Main.class.getResource("fxml/librarian/document/document_info.fxml")));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
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

    }

    public void updateDocument() {
        DataTransfer.getInstance().setCurrentDocument(table.getSelectionModel().getSelectedItem());
        Stage newStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(Main.class.getResource("fxml/librarian/document/edit_document_info.fxml")));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        newStage.setTitle("Chỉnh sửa thông tin tài liệu");
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.requestFocus();
        newStage.initOwner(table.getScene().getWindow());
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.showAndWait();

        table.refresh();

    }
}
