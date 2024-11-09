package com.cozyspace.librarymanagement.controller.librarian;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.BorrowRequestRecord;
import com.cozyspace.librarymanagement.user.Librarian;
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

public class BorrowRequestManagementController {
    @FXML
    private TableColumn<BorrowRequestRecord, String> quantityColumn;
    @FXML
    private Label requestNotFound;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<BorrowRequestRecord> table;
    @FXML
    private TableColumn<BorrowRequestRecord, String> requestIdColumn;
    @FXML
    private TableColumn<BorrowRequestRecord, String> nameColumn;
    @FXML
    private TableColumn<BorrowRequestRecord, String> documentTittleColumn;
    @FXML
    private TableColumn<BorrowRequestRecord, String> requestDateColumn;
    @FXML
    private TableColumn<BorrowRequestRecord, String> borrowDateColumn;
    @FXML
    private TableColumn<BorrowRequestRecord, String> returnDate;
    @FXML
    private TableColumn<BorrowRequestRecord, String> dueDateColumn;
    @FXML
    private TableColumn<BorrowRequestRecord, String> status;
    @FXML
    private ComboBox<String> searchType;
    @FXML
    private TextField searchField;
    @FXML
    private Button createNewRequest;
    @FXML
    private Label removeQuery;

    public void initialize() {
        ObservableList<BorrowRequestRecord> result = ((Librarian) UserManager.getUserInstance()).viewAllBorrowRequest();
        table.getItems().setAll(result);
        requestIdColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getRequestId()));
        nameColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getName()));
        documentTittleColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getDocumentTittle()));
        requestDateColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getRequestDate()));
        borrowDateColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getBorrowDate()));
        returnDate.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getReturnDate()));
        dueDateColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getDueDate()));
        status.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getStatus()));
        quantityColumn.setCellValueFactory(i -> new SimpleStringProperty(((Integer) i.getValue().getQuantity()).toString()));

        table.setVisible(true);

        removeQuery.disableProperty().bind(Bindings.isEmpty(searchField.textProperty()));

        removeQuery.setOnMouseClicked(_ -> {
            searchField.clear();
            table.getItems().setAll(result);
            requestNotFound.setVisible(false);
            table.setVisible(true);
            createNewRequest.setVisible(true);
        });

        searchButton.defaultButtonProperty().bind(searchButton.focusedProperty());

        searchButton.disableProperty()
                .bind(Bindings.isEmpty(searchField.textProperty()));

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
        createNewRequest.setStyle(IDLE_MAIN_BUTTON_STYLE);
        createNewRequest.setOnMouseEntered(_ -> createNewRequest.setStyle(HOVERED_MAIN_BUTTON_STYLE));
        createNewRequest.setOnMouseExited(_ -> createNewRequest.setStyle(IDLE_MAIN_BUTTON_STYLE));
    }

    public void search() {
        requestNotFound.setVisible(false);
        table.setVisible(false);
        String query = searchField.getText();
        ObservableList<BorrowRequestRecord> result = null;
        switch (searchType.getSelectionModel().getSelectedIndex()) {
            case 0 -> result = ((Librarian) UserManager.getUserInstance()).searchBorrowRequestByRequestID(query);
            case 1 -> result = ((Librarian) UserManager.getUserInstance()).searchBorrowRequestByMemberName(query);
            case 2 -> result = ((Librarian) UserManager.getUserInstance()).searchBorrowRequestByDocumentTittle(query);
        }
        if (result == null || result.isEmpty()) {
            requestNotFound.setVisible(true);
            createNewRequest.setVisible(false);
        } else {
            table.getItems().setAll(result);
            table.setVisible(true);
        }
    }

    public void createNewRequest() {
        Stage newStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(Main.class.getResource
                ("fxml/librarian/create_new_borrow_request.fxml")));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 700);
        } catch (IOException e) {
            e.printStackTrace();
        }
        newStage.setTitle("Thêm yêu cầu mượn sách");
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.requestFocus();
        newStage.initOwner(table.getScene().getWindow());
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.showAndWait();

        ObservableList<BorrowRequestRecord> result = ((Librarian) UserManager.getUserInstance()).viewAllBorrowRequest();

        table.setVisible(true);
        requestNotFound.setVisible(false);
        table.getItems().setAll(result);
        searchField.clear();

    }

}
