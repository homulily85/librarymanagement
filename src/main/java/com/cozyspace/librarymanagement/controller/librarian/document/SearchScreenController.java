package com.cozyspace.librarymanagement.controller.librarian.document;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.user.Librarian;
import com.cozyspace.librarymanagement.user.SearchBook;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.Objects;

public class SearchScreenController {
    @FXML
    private Text title;
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
    private Label documentNotFound;
    @FXML
    private TableView<Document> table;

    public void initialize() {

        title.setText(switch (Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode"))) {
            case SearchBook.SEARCH_ALL_DOCUMENT -> "Tất cả tài liệu";
            case SearchBook.SEARCH_ALL_AVAILABLE_DOCUMENT -> "Tài liệu hiện có";
            case SearchBook.SEARCH_ALL_UNAVAILABLE_DOCUMENT -> "Tài liệu đã hết";
            default -> "";
        });

        table.setRowFactory(_ -> {
            TableRow<Document> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem viewItem = new MenuItem("Xem thông tin");
            MenuItem editItem = new MenuItem("Chỉnh sửa thông tin");
            MenuItem deleteItem = new MenuItem("Xóa tài liệu");
            contextMenu.getItems().addAll(viewItem, editItem, deleteItem);

            ContextMenu contextMenu1 = new ContextMenu();
            table.setContextMenu(contextMenu);

            viewItem.setOnAction(_ -> showDocumentInfo());
            editItem.setOnAction(_ -> updateDocument());
            deleteItem.setOnAction(_ -> removeDocument());

            table.setContextMenu(contextMenu);

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then(contextMenu1)
                            .otherwise(contextMenu));
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    row.setOnMouseClicked(_ -> showDocumentInfo());
                }
            });

            return row;
        });

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
            ObservableList<Document> t = UserManager.getUserInstance().viewDocument(
                    Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode")));
            searchField.clear();
            table.getItems().setAll(t);
            documentNotFound.setVisible(false);
            table.setVisible(true);
            addNewDocument.setVisible(true);
            table.refresh();
        });

        searchButton.defaultButtonProperty().bind(searchButton.focusedProperty());

        searchButton.disableProperty()
                .bind(Bindings.isEmpty(searchField.textProperty()));

        if (Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode")) != SearchBook.SEARCH_ALL_DOCUMENT) {
            addNewDocument.setDisable(true);
            addNewDocument.setVisible(false);
        }
    }

    public void search() {
        documentNotFound.setVisible(false);
        table.setVisible(false);
        String query = searchField.getText();
        addNewDocument.setVisible(false);
        ObservableList<Document> result = UserManager.getUserInstance().searchDocument(query.trim().toLowerCase(),
                Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode")));
        if (result == null || result.isEmpty()) {
            documentNotFound.setVisible(true);
        } else {
            table.getItems().setAll(result);
            table.setVisible(true);
        }

    }

    public void addNewDocument() {

        Stage newStage = new Stage();

        DataTransfer.getInstance().getDataMap().put("isConfirm", "false");

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
        newStage.getIcons().add(new Image(String.valueOf(Main.class.getResource("icon/program_icon.png"))));

        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.requestFocus();
        newStage.initOwner(table.getScene().getWindow());
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.showAndWait();

        DataTransfer.getInstance().getDataMap().put("searchMode", ((Integer) SearchBook.SEARCH_ALL_DOCUMENT).toString());

        ObservableList<Document> result = UserManager.getUserInstance().viewDocument(
                Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode")));

        if (DataTransfer.getInstance().getDataMap().get("isConfirm").equals("false")) {
            return;
        }

        Notifications.create().title("Thêm tài liệu thành công")
                .text("Tài liệu đã được thêm vào cơ sở dữ liệu")
                .showInformation();

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
        newStage.getIcons().add(new Image(String.valueOf(Main.class.getResource("icon/program_icon.png"))));
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
        DataTransfer.getInstance().getDataMap().put("isConfirm", "false");
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
        newStage.getIcons().add(new Image(String.valueOf(Main.class.getResource("icon/program_icon.png"))));
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.requestFocus();
        newStage.initOwner(table.getScene().getWindow());
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.showAndWait();

        if (DataTransfer.getInstance().getDataMap().get("isConfirm").equals("false")) {
            return;
        }

        if (Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode"))
            == SearchBook.SEARCH_ALL_UNAVAILABLE_DOCUMENT && DataTransfer.getInstance().getCurrentDocument().getQuantity() > 0) {
            table.getItems().remove(DataTransfer.getInstance().getCurrentDocument());
        } else if (Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode"))
                   == SearchBook.SEARCH_ALL_AVAILABLE_DOCUMENT && DataTransfer.getInstance().getCurrentDocument().getQuantity() == 0) {
            table.getItems().remove(DataTransfer.getInstance().getCurrentDocument());
        }

        table.refresh();
        Notifications.create().title("Chỉnh sửa thông tin tài liệu thành công")
                .text("Thông tin tài liệu đã được cập nhật")
                .showInformation();


    }

    public void removeDocument() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(table.getScene().getWindow());
        dialog.setTitle("Xác nhận xóa tài liệu");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        ImageView imageView = new ImageView();
        imageView.setImage(new Image(Objects.requireNonNull(Main.class.getResource("icon/risk-icon.png")).toString()));
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        gridPane.add(imageView, 0, 0);
        gridPane.add(new Label("""
                Xóa tài liệu này cũng sẽ xóa các yêu cầu mượn sách có liên quan.
                Bạn có chắc chắn muốn xóa tài liệu này không?
                """), 1, 0);

        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Xác nhận");
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setDefaultButton(false);
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Hủy");
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setDefaultButton(true);

        var result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ((Librarian) UserManager.getUserInstance()).removeDocument(table.getSelectionModel().
                    getSelectedItem().getId());
            table.getItems().remove(table.getSelectionModel().getSelectedItem());
            table.refresh();
            Notifications.create().title("Xóa tài liệu thành công")
                    .text("Tài liệu đã được xóa khỏi cơ sở dữ liệu")
                    .showInformation();
        }
    }
}
