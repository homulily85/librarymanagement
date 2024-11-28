package com.cozyspace.librarymanagement.controller.member;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.user.Member;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class AccountInfoController {
    @FXML
    private ImageView avatar2;
    @FXML
    private StackPane root;
    @FXML
    private Label name;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label phone;

    public void initialize() {
        if (UserManager.getUserInstance().getInfo().getAvatar() != null) {
            avatar2.setImage(new Image(Objects.requireNonNull(Main.class.getResource
                    ("avatar/" + UserManager.getUserInstance().getInfo().getAvatar())).toString()));
        }

        name.setText(UserManager.getUserInstance().getInfo().getName());
        address.setText(address.getText() + UserManager.getUserInstance().getInfo().getAddress());
        email.setText(email.getText() + UserManager.getUserInstance().getInfo().getEmail());
        phone.setText(phone.getText() + UserManager.getUserInstance().getInfo().getPhone());

    }

    public void chooseAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh");
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (file == null) {
            return;
        }

        String t = getFileExtension(file);

        if (!(t.equals(".png") || t.equals(".jpg"))) {
            Notifications.create().title("Lỗi").text("Định dạng ảnh là .png hoặc .jpg").showError();
            return;
        }
        avatar2.setImage(new Image(file.toString()));

        String fileName = System.currentTimeMillis() + getFileExtension(avatar2.getImage().getUrl());

        new Thread(() -> {
            String sour = avatar2.getImage().getUrl().replace("/", "\\");
            URL url = Main.class.getResource("avatar/");
            String des;
            try {
                des = Paths.get(url.toURI()) + "/" + fileName;
                Files.copy(Path.of(sour), Path.of(des));
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            UserManager.getUserInstance().getInfo().setAvatar(fileName);
            ((Member) UserManager.getUserInstance()).updateAvatar(fileName);
        }).start();

        //TODO: Only enable this line when running the jar file
//        new Thread(() -> {
//
//            String sour = avatar.getImage().getUrl().replace("/", "\\");
//            String des = DataTransfer.getInstance().getDataMap().get("jarPath") + "/avatar/" + fileName;
//            try {
//                Files.copy(Path.of(sour), Path.of(des));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();

    }

    private String getFileExtension(String file) {
        int lastIndexOf = file.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return file.substring(lastIndexOf);
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    public void changePassword() {
        Stage newStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(Main.class.getResource
                ("fxml/member/change_password.fxml")));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        newStage.setTitle("Thay đổi mật khẩu");
        newStage.getIcons().add(new Image(String.valueOf(Main.class.getResource("icon/program_icon.png"))));

        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.requestFocus();
        newStage.initOwner(name.getScene().getWindow());
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.showAndWait();

    }
}