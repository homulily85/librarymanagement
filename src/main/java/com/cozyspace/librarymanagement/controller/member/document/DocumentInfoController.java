package com.cozyspace.librarymanagement.controller.member.document;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.BorrowRequestRecord;
import com.cozyspace.librarymanagement.datasource.Comment;
import com.cozyspace.librarymanagement.user.Member;
import com.cozyspace.librarymanagement.user.UserManager;
import com.jfoenix.controls.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Notifications;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DocumentInfoController {
    @FXML
    private StackPane stackPane;
    @FXML
    private Label documentISBN;
    @FXML
    private VBox commentSection;
    @FXML
    private ImageView coverPage;
    @FXML
    private Label documentTitle;
    @FXML
    private Label documentAuthor;
    @FXML
    private Label documentSubject;
    @FXML
    private Label documentType;
    @FXML
    private Label documentQuantity;
    @FXML
    private Label documentDescription;

    public void initialize() {
        documentTitle.setText(DataTransfer.getInstance().getCurrentDocument().getTitle());
        documentAuthor.getText();
        DataTransfer.getInstance();
        documentAuthor.setText(documentAuthor.getText() + (DataTransfer.getInstance().getCurrentDocument().getAuthor() == null ?
                "" : DataTransfer.getInstance().getCurrentDocument().getAuthor()));
        documentISBN.setText(documentISBN.getText() + (DataTransfer.getInstance().getCurrentDocument().getISBN() == null ?
                "" : DataTransfer.getInstance().getCurrentDocument().getISBN()));
        documentSubject.setText(documentSubject.getText() + (DataTransfer.getInstance().getCurrentDocument().getSubject() == null ?
                "" : DataTransfer.getInstance().getCurrentDocument().getSubject()));
        documentType.setText(documentType.getText() + (DataTransfer.getInstance().getCurrentDocument().getType() == null ?
                "" : DataTransfer.getInstance().getCurrentDocument().getType()));
        documentQuantity.setText(documentQuantity.getText() + (DataTransfer.getInstance().getCurrentDocument().getQuantity()));
        documentDescription.setText((documentDescription.getText() + (DataTransfer.getInstance().getCurrentDocument().getDescription() == null ?
                "" : DataTransfer.getInstance().getCurrentDocument().getDescription())));
        if (!(DataTransfer.getInstance().getCurrentDocument().getCoverPageLocation() == null)) {
            coverPage.setImage(new Image(Objects.requireNonNull(Main.class.getResource
                    ("book_cover/" + DataTransfer.getInstance().getCurrentDocument().getCoverPageLocation())).toString()));
        }

        ObservableList<Comment> comments = ((Member) UserManager.getUserInstance())
                .getCommentByDocumentID(DataTransfer.getInstance().getCurrentDocument().getId());
        ObservableList<Comment> sortedComments = comments.sorted((o1, o2) -> {
            LocalDate date1 = LocalDate.parse(o1.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalDate date2 = LocalDate.parse(o2.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            return date2.compareTo(date1);
        });
        commentSection.setSpacing(15);
        for (Comment comment : sortedComments) {
            VBox temp = new VBox();
            Label username = new Label(comment.getUsername());
            username.setStyle("-fx-font-size: 20;-fx-font-weight: bold");
            Label time = new Label("Đã đăng ngày: " + comment.getTime());
            time.setStyle("-fx-font-size: 18");
            Label commentLabel = new Label(comment.getComment());
            commentLabel.setWrapText(true);
            commentLabel.setStyle("-fx-font-size: 18");

            temp.getChildren().setAll(username, time, commentLabel);

            commentSection.getChildren().add(temp);
        }

    }

    public void createNewBorrowRequest() {

        ObservableList<BorrowRequestRecord> borrowRequestRecords = ((Member) UserManager.getUserInstance()).getBorrowRequestRecords();
        if (borrowRequestRecords.stream().anyMatch(i -> i.getDocumentId() == DataTransfer.getInstance().getCurrentDocument().getId()
                                                        && i.getStatus().equals(BorrowRequestRecord.BorrowRequestStatus.PENDING))) {
            Notifications.create().title("Lỗi").text("Bạn đã gửi yêu cầu mượn sách cho tài liệu này!").showError();
            return;
        }

        if (borrowRequestRecords.stream().anyMatch(i -> i.getDocumentId() == DataTransfer.getInstance().getCurrentDocument().getId()
                                                        && i.getStatus().equals(BorrowRequestRecord.BorrowRequestStatus.BORROWED))) {
            Notifications.create().title("Lỗi").text("Bạn đang mượn tài liệu này!").showError();
            return;
        }

        JFXDialogLayout content = new JFXDialogLayout();
        var heading = new Label("Tạo yêu cầu mượn sách");
        heading.setStyle("-fx-font-size: 20;-fx-font-weight: bold");
        content.setHeading(heading);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        var quantity = new Label("Số lượng:");
        gridPane.add(quantity, 0, 0);
        JFXTextField quantityField = new JFXTextField();
        quantityField.setPromptText("Nhập số lượng");
        gridPane.add(quantityField, 1, 0);
        var numberOfBorrowDate = new Label("Số ngày mượn:");
        gridPane.add(numberOfBorrowDate, 0, 1);
        JFXComboBox<Integer> numberOfBorrowDateField = new JFXComboBox<>();
        numberOfBorrowDateField.getItems().addAll(7, 14, 21, 28);
        numberOfBorrowDateField.setPromptText("Chọn số ngày mượn");
        gridPane.add(numberOfBorrowDateField, 1, 1);

        content.setBody(gridPane);

        JFXButton submit = new JFXButton("Gửi yêu cầu");
        String css = Main.class.getResource("css/button_type_2.css").toExternalForm();
        submit.getStylesheets().add(css);
        content.setActions(submit);

        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

        submit.setOnAction(_ -> {
            try {
                if (!quantityField.getText().matches("-?\\d+") || Integer.parseInt(quantityField.getText()) <= 0
                    || Integer.parseInt(quantityField.getText()) > DataTransfer.getInstance().getCurrentDocument().getQuantity()) {
                    Notifications.create().title("Lỗi").text("Số lượng không hợp lệ").showError();
                    return;
                }
            } catch (NumberFormatException ex) {
                Notifications.create().title("Lỗi").text("Số lượng không hợp lệ!").showError();
                return;
            }
            if (numberOfBorrowDateField.getValue() == null) {
                Notifications.create().title("Lỗi").text("Bạn chưa chọn số ngày mượn!").showError();
                return;
            }
            Notifications.create().title("Thành công").text("Yêu cầu mượn sách đã được gửi").showInformation();

            new Thread(() -> ((Member) UserManager.getUserInstance()).createNewBorrowRequest(DataTransfer.getInstance().getCurrentDocument().getId(),
                    Integer.parseInt(quantityField.getText()), numberOfBorrowDateField.getValue())).start();

            dialog.close();
        });

        dialog.show();
    }

    public void createNewComment() {
        JFXDialogLayout content = new JFXDialogLayout();
        var heading = new Label("Tạo nhận xét mới");
        heading.setStyle("-fx-font-size: 20;-fx-font-weight: bold");
        content.setHeading(heading);

        JFXTextArea commentField = new JFXTextArea();
        commentField.setPromptText("Nhập nhận xét của bạn");
        commentField.setWrapText(true);
        commentField.setPrefHeight(100);
        content.setBody(commentField);

        JFXButton submit = new JFXButton("Đăng nhận xét");

        content.setActions(submit);

        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

        submit.setOnAction(_ -> {
            if (commentField.getText().trim().isEmpty()) {
                Notifications.create().title("Lỗi").text("Bạn chưa nhập nhận xét!").showError();
                return;
            }


            Comment comment = ((Member) UserManager.getUserInstance()).createNewComment(DataTransfer.getInstance()
                    .getCurrentDocument().getId(), commentField.getText());


            VBox temp = new VBox();
            Label username = new Label(comment.getUsername());
            username.setStyle("-fx-font-size: 20;-fx-font-weight: bold");
            Label time = new Label("Đã đăng ngày: " + comment.getTime());
            time.setStyle("-fx-font-size: 18");
            Label commentLabel = new Label(comment.getComment());
            commentLabel.setWrapText(true);
            commentLabel.setStyle("-fx-font-size: 18");

            temp.getChildren().setAll(username, time, commentLabel);

            commentSection.getChildren().addFirst(temp);
            Notifications.create().title("Thành công").text("Nhận xét của bạn đã được đăng").showInformation();
            dialog.close();
        });

        dialog.show();
    }

}
