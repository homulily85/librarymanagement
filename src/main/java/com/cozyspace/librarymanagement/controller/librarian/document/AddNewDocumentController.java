package com.cozyspace.librarymanagement.controller.librarian.document;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.user.Librarian;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class AddNewDocumentController {
    @FXML
    private Label imageFailed;
    @FXML
    private Button chooseCoverArt;
    @FXML
    private Label ISBNFailed;
    @FXML
    private TextField documentISBNField;
    @FXML
    private ImageView coverPage;
    @FXML
    private TextField documentTitleField;
    @FXML
    private TextField documentAuthorField;
    @FXML
    private TextField documentSubjectField;
    @FXML
    private ComboBox<String> documentTypeComboBox;
    @FXML
    private TextField documentQuantityField;
    @FXML
    private TextArea documentDescriptionField;
    @FXML
    private Label inputPrompt;
    @FXML
    private Label quantityFailed;
    @FXML
    private Button addNewDocument;
    private boolean isCoverArtChosen;

    public void initialize() {
        isCoverArtChosen = false;
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

        addNewDocument.setStyle(IDLE_MAIN_BUTTON_STYLE);
        addNewDocument.setOnMouseEntered(_ -> addNewDocument.setStyle(HOVERED_MAIN_BUTTON_STYLE));
        addNewDocument.setOnMouseExited(_ -> addNewDocument.setStyle(IDLE_MAIN_BUTTON_STYLE));

        chooseCoverArt.setStyle(IDLE_MAIN_BUTTON_STYLE);
        chooseCoverArt.setOnMouseEntered(_ -> chooseCoverArt.setStyle(HOVERED_MAIN_BUTTON_STYLE));
        chooseCoverArt.setOnMouseExited(_ -> chooseCoverArt.setStyle(IDLE_MAIN_BUTTON_STYLE));

        documentTypeComboBox.setStyle("-fx-font: 20px \"\";");

        documentTypeComboBox.setCellFactory(
                new Callback<>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        return new ListCell<>() {
                            @Override
                            public void updateItem(String item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    this.getFont();
                                    setFont(Font.font(this.getFont().getName(), 20.0));
                                } else {
                                    setText(null);
                                }
                            }
                        };
                    }

                });
    }

    public void addNewDocument() {
        if (documentTitleField.getText().isEmpty() || documentSubjectField.getText().isEmpty()
                || documentQuantityField.getText().isEmpty() || documentTypeComboBox.getSelectionModel().getSelectedIndex() < 0) {
            inputPrompt.setVisible(true);
            quantityFailed.setVisible(false);
            ISBNFailed.setVisible(false);
            imageFailed.setVisible(false);
            return;
        }

        if (!documentISBNField.getText().isEmpty() && !documentISBNField.getText().matches("\\d+")) {
            inputPrompt.setVisible(false);
            quantityFailed.setVisible(false);
            ISBNFailed.setVisible(true);
            imageFailed.setVisible(false);
            return;
        }

        try {
            if (!documentQuantityField.getText().matches("-?\\d+") || Integer.parseInt(documentQuantityField.getText()) < 0
                    || Integer.parseInt(documentQuantityField.getText()) > 1000) {
                inputPrompt.setVisible(false);
                quantityFailed.setVisible(true);
                ISBNFailed.setVisible(false);
                imageFailed.setVisible(false);
                return;
            }
        } catch (NumberFormatException e) {
            inputPrompt.setVisible(false);
            ISBNFailed.setVisible(false);
            quantityFailed.setVisible(true);
            imageFailed.setVisible(false);
        }

        new Thread(() -> {
            if (!isCoverArtChosen) {
                return;
            }
            String sour = coverPage.getImage().getUrl().replace("/", "\\");
            String des = Objects.requireNonNull(Main.class.getResource("book_cover/")).getPath()
                    .replace("/", "\\").substring(1) +
                    Path.of(coverPage.getImage().getUrl().replace("/", "\\"))
                            .getFileName().toString();
            try {
                Files.copy(Path.of(sour), Path.of(des));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Document newDoc = new Document(documentISBNField.getText(), documentTitleField.getText(),
                documentAuthorField.getText(), documentDescriptionField.getText(),
                documentTypeComboBox.getSelectionModel().getSelectedItem(), Integer.parseInt(documentQuantityField.getText()),
                documentSubjectField.getText(), !isCoverArtChosen ? null : Path.of(coverPage.getImage().getUrl().replace("/", "\\"))
                .getFileName().toString());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(Main.class.getResource("fxml/librarian/document/search_screen.fxml")));

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        ((Librarian) UserManager.getUserInstance()).addDocument(newDoc);

        Stage stage = (Stage) addNewDocument.getScene().getWindow();
        stage.close();

    }

    public void chooseCoverArt() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh");
        File coverArt = fileChooser.showOpenDialog(addNewDocument.getScene().getWindow());
        if (coverArt == null) {
            return;
        }

        String t = getFileExtension(coverArt);

        if (!(t.equals(".png") || t.equals(".jpg"))) {
            inputPrompt.setVisible(false);
            ISBNFailed.setVisible(false);
            quantityFailed.setVisible(false);
            imageFailed.setVisible(true);
            return;
        }
        coverPage.setImage(new Image(coverArt.toString()));
        isCoverArtChosen = true;
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

}