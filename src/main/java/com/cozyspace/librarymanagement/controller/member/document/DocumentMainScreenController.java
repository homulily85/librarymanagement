package com.cozyspace.librarymanagement.controller.member.document;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.user.UserManager;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class DocumentMainScreenController {
    @FXML
    private StackPane mainStackPane;
    @FXML
    private ImageView searchImage;
    @FXML
    private Button searchButton;
    @FXML
    private JFXTextField searchField;
    @FXML
    private Text nameLabel;
    @FXML
    private ImageView avatar;
    @FXML
    private Pane day1;
    @FXML
    private Pane day2;
    @FXML
    private Pane day3;
    @FXML
    private Pane day4;
    @FXML
    private Pane day5;
    @FXML
    private Pane hot1;
    @FXML
    private Pane hot2;
    @FXML
    private Pane hot3;
    @FXML
    private Pane hot4;
    @FXML
    private Pane hot5;

    public void initialize() {
        searchButton.disableProperty().bind(searchField.textProperty().isEmpty());
        searchImage.disableProperty().bind(searchField.textProperty().isEmpty());

        Pane[] dayPanes = {day1, day2, day3, day4, day5};
        Pane[] hotPanes = {hot1, hot2, hot3, hot4, hot5};

        nameLabel.setText(UserManager.getUserInstance().getInfo().getName());
        if (UserManager.getUserInstance().getInfo().getAvatar() != null) {
            avatar.setImage(new Image(Objects.requireNonNull(Main.class.getResource
                    ("avatar/" + UserManager.getUserInstance().getInfo().getAvatar())).toString()));
        }

        ObservableList<Document> data = UserManager.getUserInstance().
                viewDocument(Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode")));
        Random random = new Random(LocalDate.now().toEpochDay());

        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(data.size());
            temp.add(randomIndex);
        }

        var sortedData = data.stream().sorted((o1, o2) -> o2.getQuantity() - o1.getQuantity()).toList();

        for (int i = 0; i < 5; i++) {
            setDocumentPane((ImageView) dayPanes[i].getChildren().get(0), (Label) dayPanes[i].getChildren().get(1),
                    data.get(temp.get(i)));

            int current = i;

            dayPanes[i].setOnMouseClicked(_ -> {
                DataTransfer.getInstance().setCurrentDocument(data.get(temp.get(current)));
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
                newStage.initOwner(day1.getScene().getWindow());
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.showAndWait();
            });

            setDocumentPane((ImageView) hotPanes[i].getChildren().get(0), (Label) hotPanes[i].getChildren().get(1),
                    sortedData.get(i));

            hotPanes[i].setOnMouseClicked(_ -> {
                DataTransfer.getInstance().setCurrentDocument(sortedData.get(current));
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
                newStage.initOwner(day1.getScene().getWindow());
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.showAndWait();
            });

        }

    }


    private void setDocumentPane(ImageView imageView, Label label, Document document) {
        if (document.getCoverPageLocation() != null) {
            imageView.setImage(new Image(Objects.requireNonNull(Main.class
                    .getResource("book_cover/" + document.getCoverPageLocation())).toString()));
        }
        label.setText(document.getTitle());
    }

    public void search() {
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