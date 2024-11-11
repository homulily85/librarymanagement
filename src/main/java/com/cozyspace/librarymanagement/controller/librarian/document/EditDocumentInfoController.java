package com.cozyspace.librarymanagement.controller.librarian.document;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class EditDocumentInfoController {
    @FXML
    private ImageView coverPage;
    @FXML
    private TextField documentTitleField;
    @FXML
    private TextField documentAuthorField;
    @FXML
    private TextField documentISBNField;
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
    private Label ISBNFailed;
    @FXML
    private Label imageFailed;
    @FXML
    private Button addNewDocument;
    @FXML
    private Button chooseCoverArt;

    private boolean isCoverArtChosen;


    public void initialize() {
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
                            public void updateItem(String item, boolean empty) {
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
        documentTitleField.setText(DataTransfer.getInstance().getCurrentDocument().getTitle());
        documentAuthorField.setText(DataTransfer.getInstance().getCurrentDocument().getAuthor());
        documentISBNField.setText(DataTransfer.getInstance().getCurrentDocument().getISBN());
        documentSubjectField.setText(DataTransfer.getInstance().getCurrentDocument().getSubject());
        documentTypeComboBox.setValue(DataTransfer.getInstance().getCurrentDocument().getType());
        documentQuantityField.setText(String.valueOf(DataTransfer.getInstance().getCurrentDocument().getQuantity()));
        documentDescriptionField.setText(DataTransfer.getInstance().getCurrentDocument().getDescription());
        if (!(DataTransfer.getInstance().getCurrentDocument().getCoverPageLocation() == null)) {
            coverPage.setImage(new Image(Objects.requireNonNull(Main.class.getResource
                    ("book_cover/" + DataTransfer.getInstance().getCurrentDocument().getCoverPageLocation())).toString()));
        }

    }

    public void handleFinishButton() {
        if (documentTitleField.getText() == null || documentTitleField.getText().isEmpty() ||
            documentSubjectField.getText() == null || documentSubjectField.getText().isEmpty() ||
            documentQuantityField.getText() == null || documentQuantityField.getText().isEmpty() ||
            documentTypeComboBox.getSelectionModel().getSelectedIndex() < 0) {
            inputPrompt.setVisible(true);
            quantityFailed.setVisible(false);
            ISBNFailed.setVisible(false);
            imageFailed.setVisible(false);
            return;
        }

        if (!(documentISBNField.getText() == null) && !documentISBNField.getText().isEmpty() &&
            !documentISBNField.getText().matches("\\d+")) {
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

        String fileName = System.currentTimeMillis() + getFileExtension(coverPage.getImage().getUrl());

        new Thread(() -> {
            if (!isCoverArtChosen) {
                return;
            }
            String sour = coverPage.getImage().getUrl().replace("/", "\\");
            URL url = Main.class.getResource("book_cover/");
            String des = null;
            try {
                des = Paths.get(url.toURI()) + "/" + fileName;
                Files.copy(Path.of(sour), Path.of(des));
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }).start();

        //TODO: Only enable this line when running the jar file
//        new Thread(() -> {
//            if (!isCoverArtChosen) {
//                return;
//            }
//            String sour = coverPage.getImage().getUrl().replace("/", "\\");
//            String des = DataTransfer.getInstance().getDataMap().get("jarPath") + "/book_cover/" + fileName;
//            try {
//                Files.copy(Path.of(sour), Path.of(des));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();

        DataTransfer.getInstance().getCurrentDocument().setTitle(documentTitleField.getText());
        DataTransfer.getInstance().getCurrentDocument().setAuthor(documentAuthorField.getText());
        DataTransfer.getInstance().getCurrentDocument().setISBN(documentISBNField.getText());
        DataTransfer.getInstance().getCurrentDocument().setSubject(documentSubjectField.getText());
        DataTransfer.getInstance().getCurrentDocument().setType(documentTypeComboBox.getSelectionModel().getSelectedItem());
        DataTransfer.getInstance().getCurrentDocument().setQuantity(Integer.parseInt(documentQuantityField.getText()));
        DataTransfer.getInstance().getCurrentDocument().setDescription(documentDescriptionField.getText());
        if (isCoverArtChosen) {
            DataTransfer.getInstance().getCurrentDocument().setCoverPageLocation(fileName);
        }

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(Main.class.getResource("fxml/librarian/document/search_screen.fxml")));

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        new Thread(() -> ((Librarian) UserManager.getUserInstance()).editDocument(DataTransfer.getInstance()
                .getCurrentDocument())).start();

        Stage stage = (Stage) addNewDocument.getScene().getWindow();
        stage.close();
        DataTransfer.getInstance().getDataMap().put("isConfirm", "true");

    }

    public void handleChooseCoverArtButton() {
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

    private String getFileExtension(String file) {
        int lastIndexOf = file.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return file.substring(lastIndexOf);
    }

}
