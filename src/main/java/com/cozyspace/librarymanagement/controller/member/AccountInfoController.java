package com.cozyspace.librarymanagement.controller.member;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.BorrowRequestRecord;
import com.cozyspace.librarymanagement.user.Member;
import com.cozyspace.librarymanagement.user.UserManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class AccountInfoController {
    @FXML
    private ImageView avatar2;
    @FXML
    private StackPane root;
    @FXML
    private Text nameLabel;
    @FXML
    private ImageView avatar;
    @FXML
    private Label name;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label phone;
    @FXML
    private TableView<BorrowRequestRecord> table;
    @FXML
    private TableColumn<BorrowRequestRecord, String> requestIdColumn;
    @FXML
    private TableColumn<BorrowRequestRecord, String> documentTittleColumn;
    @FXML
    private TableColumn<BorrowRequestRecord, String> quantityColumn;
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

    public void initialize() {
        table.setRowFactory(_ -> {
            TableRow<BorrowRequestRecord> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem cancel = new MenuItem("Hủy yêu cầu");
            contextMenu.getItems().addAll(cancel);

            ContextMenu contextMenu2 = new ContextMenu();
            table.setContextMenu(contextMenu);

            cancel.setOnAction(_ -> cancelRequest());

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then(contextMenu2)
                            .otherwise(contextMenu));
            return row;
        });

        nameLabel.setText(UserManager.getUserInstance().getInfo().getName());
        if (UserManager.getUserInstance().getInfo().getAvatar() != null) {
            avatar.setImage(new Image(Objects.requireNonNull(Main.class.getResource
                    ("avatar/" + UserManager.getUserInstance().getInfo().getAvatar())).toString()));
            avatar2.setImage(new Image(Objects.requireNonNull(Main.class.getResource
                    ("avatar/" + UserManager.getUserInstance().getInfo().getAvatar())).toString()));
        }

        name.setText(UserManager.getUserInstance().getInfo().getName());
        address.setText(address.getText() + UserManager.getUserInstance().getInfo().getAddress());
        email.setText(email.getText() + UserManager.getUserInstance().getInfo().getEmail());
        phone.setText(phone.getText() + UserManager.getUserInstance().getInfo().getPhone());

        ObservableList<BorrowRequestRecord> data = UserManager.getUserInstance().viewAllBorrowRequestRecords()
                .sorted((o1, o2) -> {
                    if (o1.getRequestDate() == null || o2.getRequestDate() == null) {
                        return 0;
                    }
                    return o2.getRequestDate().compareTo(o1.getRequestDate());
                });
        table.getItems().addAll(data);

        requestIdColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getRequestId()));
        documentTittleColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getDocumentTittle()));
        requestDateColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getRequestDate()));
        borrowDateColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getBorrowDate()));
        returnDate.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getReturnDate()));
        dueDateColumn.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getDueDate()));
        status.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getStatus()));
        quantityColumn.setCellValueFactory(i -> new SimpleStringProperty(((Integer) i.getValue().getQuantity()).toString()));
    }

    private void cancelRequest() {
        var currentStatus = table.getSelectionModel().getSelectedItem().getStatus();
        if (!currentStatus.equals(BorrowRequestRecord.BorrowRequestStatus.PENDING)) {
            Notifications.create().title("Lỗi").text("Không thể hủy yêu cầu này").showError();
            return;
        }

        JFXDialogLayout content = new JFXDialogLayout();
        var heading = new Label("Thay đổi trạng thái");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        content.setHeading(heading);
        content.setBody(new Label("Bạn có chắc chắn muốn hủy yêu cầu này?"));
        JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);

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

    public void chooseAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh");
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (file == null) {
            return;
        }

        String t = getFileExtension(file);

        if (!(t.equals(".png") || t.equals(".jpg"))) {
            Notifications.create().title("Lỗi").text("Định dạng ảnh là .png hoặc .jpg").showError();
            return;
        }
        avatar.setImage(new Image(file.toString()));
        avatar2.setImage(new Image(file.toString()));

        String fileName = System.currentTimeMillis() + getFileExtension(avatar.getImage().getUrl());

        new Thread(() -> {
            String sour = avatar.getImage().getUrl().replace("/", "\\");
            URL url = Main.class.getResource("avatar/");
            String des;
            try {
                des = Paths.get(url.toURI()) + "/" + fileName;
                Files.copy(Path.of(sour), Path.of(des));
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            UserManager.getUserInstance().getInfo().setAvatar(fileName);
            ((Member) UserManager.getUserInstance()).updateAvatar(fileName);
        }).start();

        //TODO: Only enable this line when running the jar file
//        new Thread(() -> {
//
//            String sour = avatar.getImage().getUrl().replace("/", "\\");
//            String des = DataTransfer.getInstance().getDataMap().get("jarPath") + "/avatar/" + fileName;
//            try {
//                Files.copy(Path.of(sour), Path.of(des));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();

    }

    private String getFileExtension(String file) {
        int lastIndexOf = file.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return file.substring(lastIndexOf);
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    public void changePassword() {
        Stage newStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(Main.class.getResource
                ("fxml/member/change_password.fxml")));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        newStage.setTitle("Thay đổi mật khẩu");
        newStage.getIcons().add(new Image(String.valueOf(Main.class.getResource("icon/program_icon.png"))));

        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.requestFocus();
        newStage.initOwner(table.getScene().getWindow());
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.showAndWait();

    }
}