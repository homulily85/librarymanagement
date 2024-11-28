package com.cozyspace.librarymanagement.controller.librarian;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.BorrowRequestRecord;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.datasource.MemberRecord;
import com.cozyspace.librarymanagement.user.Librarian;
import com.cozyspace.librarymanagement.user.SearchBook;
import com.cozyspace.librarymanagement.user.UserManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HomeController {
    @FXML
    private StackPane stackPane;
    @FXML
    private Label greeting;
    @FXML
    private Label numberOfDocument;
    @FXML
    private Label numberOfMember;
    @FXML
    private Label numberOfRequest;
    @FXML
    private PieChart documentPie;
    @FXML
    private PieChart requestPie;

    public void initialize() {
        greeting.setText("Xin chào, " + UserManager.getUserInstance().getInfo().getName());
        DataTransfer.getInstance().getDataMap().put("searchMode", ((Integer) SearchBook.SEARCH_ALL_DOCUMENT).toString());
        ObservableList<Document> documents = UserManager.getUserInstance().viewDocument(
                Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode")));
        ObservableList<MemberRecord> memberRecords = ((Librarian) UserManager.getUserInstance()).viewAllMember();
        ObservableList<BorrowRequestRecord> request = UserManager.getUserInstance().viewAllBorrowRequestRecords();

        numberOfDocument.setText(String.valueOf(documents.size()));
        numberOfMember.setText(String.valueOf(memberRecords.size()));
        numberOfRequest.setText(String.valueOf(request.size()));

        ObservableList<PieChart.Data> documentData = FXCollections.observableArrayList(
                new PieChart.Data("Tài liệu hiện có", documents.stream().filter(i -> i.getQuantity() > 0).count()),
                new PieChart.Data("Tài liệu đã hết", documents.stream().filter(i -> i.getQuantity() == 0).count()));

        documentPie.setData(documentData);

        ObservableList<PieChart.Data> borrowRequest = FXCollections.observableArrayList(
                new PieChart.Data(BorrowRequestRecord.BorrowRequestStatus.PENDING, request.stream()
                        .filter(i -> i.getStatus().equals(BorrowRequestRecord.BorrowRequestStatus.PENDING)).count()),
                new PieChart.Data(BorrowRequestRecord.BorrowRequestStatus.BORROWED, request.stream()
                        .filter(i -> i.getStatus().equals(BorrowRequestRecord.BorrowRequestStatus.BORROWED)).count()),
                new PieChart.Data(BorrowRequestRecord.BorrowRequestStatus.RETURNED, request.stream()
                        .filter(i -> i.getStatus().equals(BorrowRequestRecord.BorrowRequestStatus.RETURNED)).count()),
                new PieChart.Data(BorrowRequestRecord.BorrowRequestStatus.CANCELLED, request.stream()
                        .filter(i -> i.getStatus().equals(BorrowRequestRecord.BorrowRequestStatus.CANCELLED)).count()));

        requestPie.setData(borrowRequest);
    }

    public void logout() {
        JFXDialogLayout content = new JFXDialogLayout();
        var heading = new Label("Đăng xuất");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        content.setHeading(heading);
        content.setBody(new Label("Bạn có chắc chắn muốn đăng xuất?"));
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

        JFXButton okButton = new JFXButton("Xác nhận");
        String css = Main.class.getResource("css/button_type_2.css").toExternalForm();
        okButton.getStylesheets().add(css);

        JFXButton cancelButton = new JFXButton("Hủy");
        cancelButton.getStylesheets().add(css);

        cancelButton.setOnAction(_ -> dialog.close());

        okButton.setOnAction(_ -> {
            UserManager.removeUserInstance();
            dialog.close();
            Parent root = null;
            Stage stage = (Stage) stackPane.getScene().getWindow();

            try {
                root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/login/login_phase_1.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        });

        content.setActions(okButton, cancelButton);
        dialog.show();
    }
}
