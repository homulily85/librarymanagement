package com.cozyspace.librarymanagement.controller.librarian.document;

import com.cozyspace.librarymanagement.DataTransfer;
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
        if (!(document.getCoverPageLocation() == null)) {
            coverPage.setImage(new Image(Objects.requireNonNull(
                    Main.class.getResource("book_cover/" + document.getCoverPageLocation())).toString()));
            //TODO: Only enable this line when running the jar file
//            coverPage.setImage(new Image(DataTransfer.getInstance().getDataMap().get("jarPath") + "/book_cover/"
//                                         + document.getCoverPageLocation()));
        }
    }
}
