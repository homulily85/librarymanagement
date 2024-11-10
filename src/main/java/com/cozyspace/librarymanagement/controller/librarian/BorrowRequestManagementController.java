package com.cozyspace.librarymanagement.controller.librarian;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.BorrowRequestRecord;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.user.Librarian;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        table.setRowFactory(_ -> {
            TableRow<BorrowRequestRecord> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem changeStatus = new MenuItem("Thay đổi trạng thái");
            MenuItem extendDueDate = new MenuItem("Gia hạn hạn trả");
            contextMenu.getItems().addAll(changeStatus, extendDueDate);

            ContextMenu contextMenu2 = new ContextMenu();
            table.setContextMenu(contextMenu);

            changeStatus.setOnAction(_ -> changeStatus());
            extendDueDate.setOnAction(_ -> extendDueDate());

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then(contextMenu2)
                            .otherwise(contextMenu));
            return row;
        });

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

        DataTransfer.getInstance().getDataMap().put("isConfirm", Boolean.toString(false));

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

        if (DataTransfer.getInstance().getDataMap().get("isConfirm").equals("false")) {
            return;
        }

        ObservableList<BorrowRequestRecord> result = ((Librarian) UserManager.getUserInstance()).viewAllBorrowRequest();

        table.setVisible(true);
        requestNotFound.setVisible(false);
        table.getItems().setAll(result);
        searchField.clear();

        Notifications.create()
                .text("Yêu cầu mượn tài liệu đã được tạo thành công!")
                .showInformation();
    }

    public void changeStatus() {
        var currentStatus = table.getSelectionModel().getSelectedItem().getStatus();
        if (currentStatus.equals(BorrowRequestRecord.BorrowRequestStatus.RETURNED) ||
            currentStatus.equals(BorrowRequestRecord.BorrowRequestStatus.CANCELLED)) {
            Notifications.create().title("Lỗi").text("Không thể thay đổi trạng thái cho yêu cầu này.").showError();
            return;
        }
        ComboBox<String> statusComboBox = new ComboBox<>();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(table.getScene().getWindow());
        dialog.setTitle("Thay đổi trạng thái");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label statusLabel = new Label("Trạng thái mới: ");

        if (currentStatus.equals(BorrowRequestRecord.BorrowRequestStatus.PENDING)) {
            statusComboBox.getItems().setAll(BorrowRequestRecord.BorrowRequestStatus.BORROWED,
                    BorrowRequestRecord.BorrowRequestStatus.CANCELLED);
        } else if (currentStatus.equals(BorrowRequestRecord.BorrowRequestStatus.BORROWED)) {
            statusComboBox.getItems().setAll(BorrowRequestRecord.BorrowRequestStatus.RETURNED);
        }
        statusComboBox.getSelectionModel().selectFirst();

        gridPane.add(statusLabel, 0, 0);
        gridPane.add(statusComboBox, 1, 0);

        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Xác nhận");
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Hủy");

        var result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ObservableList<Document> document = ((Librarian) UserManager.getUserInstance())
                    .searchDocumentById(table.getSelectionModel().getSelectedItem().getDocumentId());
            BorrowRequestRecord record = table.getSelectionModel().getSelectedItem();
            record.setStatus(statusComboBox.getSelectionModel().getSelectedItem());
            if (statusComboBox.getSelectionModel().getSelectedItem().equals(BorrowRequestRecord.BorrowRequestStatus.RETURNED)) {
                record.setReturnDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                new Thread(() -> {
                    document.getFirst().setQuantity(document.getFirst().getQuantity() + record.getQuantity());
                    ((Librarian) UserManager.getUserInstance()).editDocument(document.getFirst());
                }).start();
            } else if (statusComboBox.getSelectionModel().getSelectedItem().equals(BorrowRequestRecord.BorrowRequestStatus.BORROWED)) {
                record.setBorrowDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                new Thread(() -> {
                    document.getFirst().setQuantity(document.getFirst().getQuantity() - record.getQuantity());
                    ((Librarian) UserManager.getUserInstance()).editDocument(document.getFirst());
                }).start();
            }
            ((Librarian) UserManager.getUserInstance()).updateBorrowRequest(record);
            table.refresh();
        }
    }

    public void extendDueDate() {
        var currentStatus = table.getSelectionModel().getSelectedItem().getStatus();
        if (currentStatus.equals(BorrowRequestRecord.BorrowRequestStatus.RETURNED) ||
            currentStatus.equals(BorrowRequestRecord.BorrowRequestStatus.CANCELLED) ||
            currentStatus.equals(BorrowRequestRecord.BorrowRequestStatus.PENDING)) {
            Notifications.create().title("Lỗi").text("Không thể thay đổi trạng thái cho yêu cầu này.").showError();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(table.getScene().getWindow());
        dialog.setTitle("Gia hạn hạn trả");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label statusLabel = new Label("Hạn trả mới: ");
        DatePicker datePicker = new DatePicker();

        datePicker.setConverter(new StringConverter<>() {
            final String pattern = "dd-MM-yyyy";
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        gridPane.add(statusLabel, 0, 0);
        gridPane.add(datePicker, 1, 0);

        dialog.getDialogPane().setContent(gridPane);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Xác nhận");
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Hủy");

        var result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (datePicker.getValue() == null || datePicker.getValue().isBefore(
                    LocalDate.parse(table.getSelectionModel().getSelectedItem().getDueDate(),
                            DateTimeFormatter.ofPattern("dd-MM-yyyy")))) {
                Notifications.create().title("Lỗi").text("Hạn trả không hợp lệ.").showError();
                return;
            }

            BorrowRequestRecord record = table.getSelectionModel().getSelectedItem();
            record.setDueDate(datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            ((Librarian) UserManager.getUserInstance()).updateBorrowRequest(record);
            table.refresh();
        }


    }

}
