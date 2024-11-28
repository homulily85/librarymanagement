package com.cozyspace.librarymanagement.controller.librarian;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.datasource.MemberRecord;
import com.cozyspace.librarymanagement.user.Librarian;
import com.cozyspace.librarymanagement.user.SearchBook;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreateNewBorrowRequestController {
    @FXML
    private Button finishButton;
    @FXML
    private TextField documentSearchField;
    @FXML
    private TableView<Document> documentTable;
    @FXML
    private TextField memberSearchField;
    @FXML
    private TableView<MemberRecord> memberTable;
    @FXML
    private TextField quantityField;
    @FXML
    private DatePicker dueDateField;
    @FXML
    private Label documentFail;
    @FXML
    private Label memberFail;
    @FXML
    private Label quantityFail;
    @FXML
    private Label deadlineFail;
    @FXML
    private TableColumn<Document, String> documentId;
    @FXML
    private TableColumn<Document, String> documentTitle;
    @FXML
    private TableColumn<Document, String> documentAuthor;
    @FXML
    private TableColumn<Document, String> documentQuantity;
    @FXML
    private TableColumn<MemberRecord, String> memberName;
    @FXML
    private TableColumn<MemberRecord, String> memberPhone;
    @FXML
    private TableColumn<MemberRecord, String> memberEmail;

    public void initialize() {
        dueDateField.setConverter(new StringConverter<>() {
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
    }

    public void handleDocumentSearchButton() {
        ObservableList<Document> documents = ((Librarian) UserManager.getUserInstance())
                .searchDocument(documentSearchField.getText().trim().toLowerCase(), SearchBook.SEARCH_ALL_DOCUMENT);

        documentTable.getItems().setAll(documents);
        documentId.setCellValueFactory(i -> new SimpleStringProperty(String.valueOf(i.getValue().getId())));
        documentTitle.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getTitle()));
        documentAuthor.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getAuthor()));
        documentQuantity.setCellValueFactory(i -> new SimpleStringProperty(String.valueOf(i.getValue().getQuantity())));
    }

    public void handleMemberSearchButton() {
        ObservableList<MemberRecord> members = ((Librarian) UserManager.getUserInstance())
                .searchMember(memberSearchField.getText().trim().toLowerCase());

        memberTable.getItems().setAll(members);
        memberName.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getName()));
        memberPhone.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getPhone()));
        memberEmail.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getEmail()));
    }

    public void handleFinishButton() {

        if (documentTable.getSelectionModel().getSelectedItem() == null) {
            documentFail.setVisible(true);
            memberFail.setVisible(false);
            quantityFail.setVisible(false);
            deadlineFail.setVisible(false);
            return;
        }

        if (memberTable.getSelectionModel().getSelectedItem() == null) {
            documentFail.setVisible(false);
            memberFail.setVisible(true);
            quantityFail.setVisible(false);
            deadlineFail.setVisible(false);
            return;
        }

        try {
            if (!quantityField.getText().matches("-?\\d+") || Integer.parseInt(quantityField.getText()) <= 0
                || Integer.parseInt(quantityField.getText()) > documentTable.getSelectionModel().getSelectedItem().getQuantity()) {
                documentFail.setVisible(false);
                memberFail.setVisible(false);
                quantityFail.setVisible(true);
                deadlineFail.setVisible(false);
                return;
            }
        } catch (NumberFormatException e) {
            documentFail.setVisible(false);
            memberFail.setVisible(false);
            quantityFail.setVisible(true);
            deadlineFail.setVisible(false);
            return;
        }

        if (dueDateField.getValue() == null || dueDateField.getValue().isBefore(LocalDate.now())) {
            documentFail.setVisible(false);
            memberFail.setVisible(false);
            quantityFail.setVisible(false);
            deadlineFail.setVisible(true);
            return;
        }

        DataTransfer.getInstance().setCurrentDocument(documentTable.getSelectionModel().getSelectedItem());
        DataTransfer.getInstance().getCurrentDocument().setQuantity
                (DataTransfer.getInstance().getCurrentDocument().getQuantity() - Integer.parseInt(quantityField.getText()));

        UserManager.getUserInstance().createNewBorrowRequest(
                memberTable.getSelectionModel().getSelectedItem().getUsername(),
                documentTable.getSelectionModel().getSelectedItem().getId(),
                Integer.parseInt(quantityField.getText()),
                dueDateField.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        );

        new Thread(() -> ((Librarian) UserManager.getUserInstance()).editDocument(DataTransfer.getInstance()
                .getCurrentDocument())).start();

        Stage stage = (Stage) finishButton.getScene().getWindow();
        stage.close();

        DataTransfer.getInstance().getDataMap().put("isConfirm", Boolean.toString(true));

    }
}

