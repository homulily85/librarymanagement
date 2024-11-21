package com.cozyspace.librarymanagement.controller.member.document;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
    private ImageView imageHot2;
    @FXML
    private Label nameDay2;
    @FXML
    private Text nameLabel;
    @FXML
    private ImageView avatar;
    @FXML
    private Pane day1;
    @FXML
    private ImageView imageDay1;
    @FXML
    private Label nameDay1;
    @FXML
    private Pane day2;
    @FXML
    private ImageView imageDay2;
    @FXML
    private Pane day3;
    @FXML
    private ImageView imageDay3;
    @FXML
    private Label nameDay3;
    @FXML
    private Pane day4;
    @FXML
    private ImageView imageDay4;
    @FXML
    private Label nameDay4;
    @FXML
    private Pane day5;
    @FXML
    private ImageView imageDay5;
    @FXML
    private Label nameDay5;
    @FXML
    private Pane hot1;
    @FXML
    private ImageView imageHot1;
    @FXML
    private Label nameHot1;
    @FXML
    private Pane hot2;
    @FXML
    private Label nameHot2;
    @FXML
    private Pane hot3;
    @FXML
    private ImageView imageHot3;
    @FXML
    private Label nameHot3;
    @FXML
    private Pane hot4;
    @FXML
    private ImageView imageHot4;
    @FXML
    private Label nameHot4;
    @FXML
    private Pane hot5;
    @FXML
    private ImageView imageHot5;
    @FXML
    private Label nameHot5;

    public void initialize() {
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

//        setDocumentPane(imageDay1, nameDay1, data.get(temp.get(0)));
//        setDocumentPane(imageDay2, nameDay2, data.get(temp.get(1)));
//        setDocumentPane(imageDay3, nameDay3, data.get(temp.get(2)));
//        setDocumentPane(imageDay4, nameDay4, data.get(temp.get(3)));
//        setDocumentPane(imageDay5, nameDay5, data.get(temp.get(4)));
//
//
//        setDocumentPane(imageHot1, nameHot1, sortedData.get(0));
//        setDocumentPane(imageHot2, nameHot2, sortedData.get(1));
//        setDocumentPane(imageHot3, nameHot3, sortedData.get(2));
//        setDocumentPane(imageHot4, nameHot4, sortedData.get(3));
//        setDocumentPane(imageHot5, nameHot5, sortedData.get(4));


        for (int i = 0; i < 5; i++) {
            setDocumentPane((ImageView) dayPanes[i].getChildren().get(0), (Label) dayPanes[i].getChildren().get(1),
                    data.get(temp.get(i)));

            int current = i;

            dayPanes[i].setOnMouseClicked(event -> {
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

            hotPanes[i].setOnMouseClicked(event -> {
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
}