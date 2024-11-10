package com.cozyspace.librarymanagement.controller.librarian;

import com.cozyspace.librarymanagement.datasource.MemberRecord;
import com.cozyspace.librarymanagement.user.Librarian;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MemberManagementController {
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private Label removeQuery;
    @FXML
    private Label success;
    @FXML
    private Label titleLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private Label phoneFailed;
    @FXML
    private Label emailFailed;
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<String> searchType;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<MemberRecord> table;
    @FXML
    private TableColumn<MemberRecord, String> nameColumn;
    @FXML
    private TableColumn<MemberRecord, String> addressColumn;
    @FXML
    private TableColumn<MemberRecord, String> emailColumn;
    @FXML
    private TableColumn<MemberRecord, String> phoneColumn;
    @FXML
    private Label memberNotFound;


    public void initialize() {
        ObservableList<MemberRecord> result = ((Librarian) UserManager.getUserInstance()).viewAllMember();
        table.getItems().setAll(result);
        nameColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getName()));
        addressColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getAddress()));
        phoneColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getPhone()));
        emailColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getEmail()));
        table.setVisible(true);

        searchButton.disableProperty()
                .bind(Bindings.isEmpty(searchField.textProperty()));

        removeQuery.disableProperty().bind(Bindings.isEmpty(searchField.textProperty()));

        usernameField.setDisable(true);

        removeQuery.setOnMouseClicked(_ -> {
            searchField.clear();
            table.getItems().setAll(result);
            memberNotFound.setVisible(false);
            table.setVisible(true);
            nameLabel.setVisible(true);
            nameField.setVisible(true);
            addressLabel.setVisible(true);
            addressField.setVisible(true);
            phoneLabel.setVisible(true);
            phoneField.setVisible(true);
            emailField.setVisible(true);
            emailLabel.setVisible(true);
            saveButton.setVisible(true);
            titleLabel.setVisible(true);
            usernameField.setVisible(true);
            usernameLabel.setVisible(true);
        });

        table.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                MemberRecord item = table.getSelectionModel().getSelectedItem();
                nameField.setText(item.getName());
                addressField.setText(item.getAddress());
                emailField.setText(item.getEmail());
                phoneField.setText(item.getPhone());
                emailFailed.setVisible(false);
                success.setVisible(false);
                phoneFailed.setVisible(false);
                usernameField.setText(item.getUsername());
            }
        });

        searchButton.defaultButtonProperty().bind(searchButton.focusedProperty());

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

        saveButton.setStyle(IDLE_MAIN_BUTTON_STYLE);
        saveButton.setOnMouseEntered(_ -> saveButton.setStyle(HOVERED_MAIN_BUTTON_STYLE));
        saveButton.setOnMouseExited(_ -> saveButton.setStyle(IDLE_MAIN_BUTTON_STYLE));

    }

    public void search() {
        success.setVisible(false);
        titleLabel.setVisible(false);
        memberNotFound.setVisible(false);
        phoneField.setVisible(false);
        emailFailed.setVisible(false);
        table.setVisible(false);
        nameLabel.setVisible(false);
        nameField.clear();
        nameField.setVisible(false);
        addressLabel.setVisible(false);
        addressField.clear();
        addressField.setVisible(false);
        emailLabel.setVisible(false);
        emailField.clear();
        emailField.setVisible(false);
        phoneLabel.setVisible(false);
        phoneField.clear();
        phoneField.setVisible(false);
        saveButton.setVisible(false);
        usernameField.setVisible(false);
        usernameLabel.setVisible(false);
        ObservableList<MemberRecord> result = null;
        switch (searchType.getSelectionModel().getSelectedIndex()) {
            case 0 -> result = ((Librarian) UserManager.getUserInstance()).searchMemberByName(searchField.getText());
            case 1 -> result = ((Librarian) UserManager.getUserInstance()).searchMemberByPhone(searchField.getText());
            case 2 -> result = ((Librarian) UserManager.getUserInstance()).searchMemberByEmail(searchField.getText());
        }
        if (result == null || result.isEmpty()) {
            memberNotFound.setVisible(true);
        } else {
            table.getItems().setAll(result);
            table.setVisible(true);
            titleLabel.setVisible(true);
            nameLabel.setVisible(true);
            nameField.setVisible(true);
            addressLabel.setVisible(true);
            addressField.setVisible(true);
            phoneLabel.setVisible(true);
            phoneField.setVisible(true);
            emailField.setVisible(true);
            emailLabel.setVisible(true);
            saveButton.setVisible(true);
            memberNotFound.setVisible(false);
            usernameField.setVisible(true);
            usernameLabel.setVisible(true);
        }

    }

    public void updateMemberInfo() {

        if (!phoneField.getText().matches("\\d{10}")) {

            emailFailed.setVisible(false);
            phoneFailed.setVisible(true);
            return;
        }

        if (!emailField.getText().matches("^(.+)@(.+)$")) {
            phoneFailed.setVisible(false);
            emailFailed.setVisible(true);
            return;
        }

        phoneFailed.setVisible(false);
        emailFailed.setVisible(false);
        success.setVisible(true);

        MemberRecord t = table.getSelectionModel().getSelectedItem();
        t.setAddress(addressField.getText());
        t.setEmail(emailField.getText());
        t.setPhone(phoneField.getText());
        t.setName(nameField.getText());

        table.refresh();

        new Thread(() -> ((Librarian) UserManager.getUserInstance()).updateMemberInfo(t)).start();

    }
}
