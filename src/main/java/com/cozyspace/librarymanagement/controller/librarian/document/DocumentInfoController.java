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
    private Label documentISBN;
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
        documentTitle.setText(document.getTitle());
        documentAuthor.setText(documentAuthor.getText() + document.getAuthor());
        documentType.setText(documentType.getText() + document.getType());
        documentQuantity.setText(documentQuantity.getText() + document.getQuantity());
        documentDescription.setText(documentDescription.getText() + document.getDescription());
        documentSubject.setText(documentSubject.getText() + document.getSubject());
        documentISBN.setText(documentISBN.getText() + document.getISBN());
        if (!(document.getCoverPageLocation() == null)) {
            coverPage.setImage(new Image(Objects.requireNonNull(
                    Main.class.getResource("book_cover/" + document.getCoverPageLocation())).toString()));
            //TODO: Only enable this line when running the jar file
//            coverPage.setImage(new Image(DataTransfer.getInstance().getDataMap().get("jarPath") + "/book_cover/"
//                                         + document.getCoverPageLocation()));
        }
    }
}
