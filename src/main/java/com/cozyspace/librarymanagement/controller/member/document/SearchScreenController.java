package com.cozyspace.librarymanagement.controller.member.document;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.datasource.GoogleBooksAPI;
import com.cozyspace.librarymanagement.user.SearchBook;
import com.cozyspace.librarymanagement.user.UserManager;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchScreenController {
    @FXML
    private StackPane mainStackPane;
    @FXML
    private Button searchButton;
    @FXML
    private ImageView searchImage;
    @FXML
    private Text nameLabel;
    @FXML
    private ImageView avatar;
    @FXML
    private JFXTextField searchField;
    @FXML
    private VBox searchResult;

    public void initialize() {

        searchButton.disableProperty().bind(searchField.textProperty().isEmpty());
        searchImage.disableProperty().bind(searchField.textProperty().isEmpty());

        nameLabel.setText(UserManager.getUserInstance().getInfo().getName());

        if (UserManager.getUserInstance().getInfo().getAvatar() != null) {
            avatar.setImage(new Image(Objects.requireNonNull(Main.class.getResource
                    ("avatar/" + UserManager.getUserInstance().getInfo().getAvatar())).toString()));
        }
        searchField.setText(DataTransfer.getInstance().getDataMap().get("keyword"));

        search();

    }

    private void search() {
        searchResult.getChildren().clear();
        DataTransfer.getInstance().getDataMap().put("keyword", searchField.getText());
        searchField.setText(DataTransfer.getInstance().getDataMap().get("keyword"));

        ObservableList<Document> data = UserManager.getUserInstance().viewDocument(SearchBook.SEARCH_ALL_DOCUMENT);

        ObservableList<Document> result = data.stream()
                .filter(document -> document.getTitle().toLowerCase().contains(DataTransfer.getInstance().getDataMap().get("keyword").toLowerCase()) ||
                                    document.getAuthor().toLowerCase().contains(DataTransfer.getInstance().getDataMap().get("keyword").toLowerCase()) ||
                                    (document.getISBN() != null && document.getISBN().equals(DataTransfer.getInstance().getDataMap().get("keyword"))))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        if (result.isEmpty()) {
            try {
                result = GoogleBooksAPI.query(DataTransfer.getInstance().getDataMap().get("keyword"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Document document : result) {
            HBox documentCard = new HBox();
            documentCard.setSpacing(20);
            if (document.getCoverPageLocation() == null) {
                documentCard.getChildren().add(new ImageView(new Image(Objects.requireNonNull(Main.class.getResource
                        ("icon/coverart.png")).toString())));
            } else {
                ImageView cover = new ImageView(new Image(Objects.requireNonNull(Main.class.getResource
                        ("book_cover/" + document.getCoverPageLocation())).toString()));
                cover.setFitWidth(150);
                cover.setFitHeight(185);
                documentCard.getChildren().add(cover);
            }
            VBox temp = new VBox();
            temp.setSpacing(10);
            Label title = new Label(document.getTitle());
            title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            temp.getChildren().add(title);
            Label author = new Label("Tác giả: " + (document.getAuthor() == null ?
                    "" : document.getAuthor()));
            author.setStyle("-fx-font-size: 16px;");
            temp.getChildren().add(author);
            Label ISBN = new Label("ISBN: " + (document.getISBN() == null ?
                    "" : document.getISBN()));
            ISBN.setStyle("-fx-font-size: 16px;");
            temp.getChildren().add(ISBN);
            Label type = new Label("Thể loại: " + (document.getType() == null ?
                    "" : document.getType()));
            type.setStyle("-fx-font-size: 16px;");
            temp.getChildren().add(type);
            Label quantity = new Label("Số lượng hiện có: " + document.getQuantity());
            quantity.setStyle("-fx-font-size: 16px;");
            temp.getChildren().add(quantity);

            documentCard.getChildren().add(temp);

            searchResult.getChildren().add(documentCard);

            documentCard.setOnMouseClicked(_ -> {
                DataTransfer.getInstance().setCurrentDocument(document);
                Stage newStage = new Stage();

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Objects.requireNonNull(Main.class.getResource
                        ("fxml/member/document/document_info.fxml")));

                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 900, 600);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                newStage.setTitle("Thông tin tài liệu");
                newStage.getIcons().add(new Image(String.valueOf(Main.class.getResource("icon/program_icon.png"))));

                newStage.setScene(scene);
                newStage.setResizable(false);
                newStage.requestFocus();
                newStage.initOwner(documentCard.getScene().getWindow());
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.showAndWait();
            });

            documentCard.hoverProperty().addListener((_, _, newValue) -> {
                if (newValue) {
                    documentCard.setStyle("-fx-background-color: #b6b4b4");
                } else {
                    documentCard.setStyle("-fx-background-color: transparent");
                }
            });
        }
    }

    public void searchButtonClicked() {
        DataTransfer.getInstance().getDataMap().put("keyword", searchField.getText());
        JFXDialogLayout content = new JFXDialogLayout();
        var heading = new Label("Đang tìm kiếm...");
        heading.setStyle("-fx-font-size: 20;-fx-font-weight: bold");
        content.setHeading(heading);

        JFXDialog dialog = new JFXDialog(mainStackPane, content, JFXDialog.DialogTransition.CENTER);
        dialog.show();

        new Thread(() -> {
            try {
                Thread.sleep(500);
                dialog.close();
                Platform.runLater(() -> {
                    DataTransfer.getInstance().getDataMap().put("keyword", searchField.getText());
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/member/document/search_screen.fxml"));
                    try {
                        StackPane searchScreen = fxmlLoader.load();
                        Parent parent = mainStackPane.getParent();
                        ((BorderPane) parent).setCenter(searchScreen);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
