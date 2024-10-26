package com.cozyspace.librarymanagement.controller;

import com.cozyspace.librarymanagement.datasource.Document;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DocumentInfoController {
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
        documentDescription.setText(document.getDescription());
    }
}
