package com.cozyspace.librarymanagement.controller.librarian.document;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.Document;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class DocumentInfoController {
    @FXML
    private ImageView coverPage;
    @FXML
    private Label documentSubject;
    @FXML
    private Label documentTitle;
    @FXML
    private Label documentAuthor;
    @FXML
    private Label documentType;
    @FXML
    private Label documentQuantity;
    @FXML
    private Label documentDescription;

    public void setInfo(Document document) {
        documentTitle.setText(document.title());
        documentAuthor.setText(documentAuthor.getText() + document.author());
        documentType.setText(documentType.getText() + document.type());
        documentQuantity.setText(documentQuantity.getText() + document.quantity());
        documentDescription.setText(document.description());
        documentSubject.setText(documentSubject.getText() + document.subject());
        if (document.coverPageLocation() != null || !document.coverPageLocation().isEmpty()) {
            coverPage.setImage(new Image(Objects.requireNonNull(Main.class.getResource("book_cover/"
                    + document.coverPageLocation())).toString()));
        }
    }
}
