package com.cozyspace.librarymanagement.controller.member;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.BorrowRequestRecord;
import com.cozyspace.librarymanagement.user.UserManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.Notifications;

public class RequestHistoryController {
    @FXML
    private StackPane stackPane;
    @FXML
    private TableColumn<BorrowRequestRecord, String> quantityColumn;
    @FXML
    private Label requestNotFound;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<BorrowRequestRecord> table;
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
    private TextField searchField;
    @FXML
    private Label removeQuery;

    public void initialize() {

        table.setRowFactory(_ -> {
            TableRow<BorrowRequestRecord> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem changeStatus = new MenuItem("Hủy yêu cầu");
            contextMenu.getItems().addAll(changeStatus);

            ContextMenu contextMenu2 = new ContextMenu();
            table.setContextMenu(contextMenu);

            changeStatus.setOnAction(_ -> cancelRequest());
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then(contextMenu2)
                            .otherwise(contextMenu));
            return row;
        });

        ObservableList<BorrowRequestRecord> result = UserManager.getUserInstance().viewAllBorrowRequestRecords();
        table.getItems().setAll(result);
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
            ObservableList<BorrowRequestRecord> t = UserManager.getUserInstance().viewAllBorrowRequestRecords();
            searchField.clear();
            table.getItems().setAll(t);
            requestNotFound.setVisible(false);
            table.setVisible(true);
        });

        searchButton.defaultButtonProperty().bind(searchButton.focusedProperty());

        searchButton.disableProperty()
                .bind(Bindings.isEmpty(searchField.textProperty()));

    }

    public void search() {
        requestNotFound.setVisible(false);
        table.setVisible(false);
        String query = searchField.getText();
        ObservableList<BorrowRequestRecord> result = UserManager.getUserInstance().viewAllBorrowRequestRecords().filtered(
                record -> record.getDocumentTittle().toLowerCase().contains(query.toLowerCase()));
        if (result == null || result.isEmpty()) {
            requestNotFound.setVisible(true);
        } else {
            table.getItems().setAll(result);
            table.setVisible(true);
        }
    }

    private void cancelRequest() {
        var currentStatus = table.getSelectionModel().getSelectedItem().getStatus();
        if (!currentStatus.equals(BorrowRequestRecord.BorrowRequestStatus.PENDING)) {
            Notifications.create().title("Lỗi").text("Không thể hủy yêu cầu này").showError();
            return;
        }

        JFXDialogLayout content = new JFXDialogLayout();
        var heading = new Label("Hủy yêu cầu");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        content.setHeading(heading);
        content.setBody(new Label("Bạn có chắc chắn muốn hủy yêu cầu này?"));
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

        JFXButton okButton = new JFXButton("Xác nhận");
        String css = Main.class.getResource("css/button_type_2.css").toExternalForm();
        okButton.getStylesheets().add(css);

        JFXButton cancelButton = new JFXButton("Hủy");
        cancelButton.getStylesheets().add(css);

        cancelButton.setOnAction(_ -> dialog.close());

        okButton.setOnAction(_ -> {
            BorrowRequestRecord record = table.getSelectionModel().getSelectedItem();
            record.setStatus(BorrowRequestRecord.BorrowRequestStatus.CANCELLED);
            UserManager.getUserInstance().updateBorrowRequest(record);
            table.refresh();
            Notifications.create().title("Thành công").text("Yêu cầu đã được hủy").showInformation();
            dialog.close();
        });

        content.setActions(okButton, cancelButton);
        dialog.show();
    }

}
