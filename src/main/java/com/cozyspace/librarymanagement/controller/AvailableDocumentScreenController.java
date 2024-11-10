package com.cozyspace.librarymanagement.controller;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.controller.librarian.document.DocumentInfoController;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AvailableDocumentScreenController {
    @FXML
    private TableColumn<Document, String> isbnColumn;
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
        ObservableList<Document> result = UserManager.getUserInstance().viewDocument(
                 Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode")));
        table.getItems().setAll(result);
        titleColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getTitle()));
        authorColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getAuthor()));
        typeColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getType()));
        isbnColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getISBN()));
        quantityColumn.setCellValueFactory(i -> new SimpleStringProperty(((Integer) i.getValue().getQuantity()).toString()));
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
}
