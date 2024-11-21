package com.cozyspace.librarymanagement.controller.member.document;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.Comment;
import com.cozyspace.librarymanagement.user.Member;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class DocumentInfoController {
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
        commentSection.setSpacing(15);
        for (Comment comment : comments) {
            VBox temp = new VBox();
            Label username = new Label(comment.getUsername());
            username.setStyle("-fx-font-size: 20");
            Label time = new Label("Đã đăng ngày: " + comment.getTime());
            time.setStyle("-fx-font-size: 14");
            Label commentLabel = new Label(comment.getComment());
            commentLabel.setWrapText(true);
            commentLabel.setStyle("-fx-font-size: 14");

            temp.getChildren().setAll(username, time, commentLabel);

            commentSection.getChildren().add(temp);

        }
    }
}
