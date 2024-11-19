package com.cozyspace.librarymanagement.controller.member;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.datasource.BorrowRequestRecord;
import com.cozyspace.librarymanagement.user.Member;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;

public class HomeController {
    @FXML
    private Label numberOfPending;
    @FXML
    private Label numberOfBorrowed;
    @FXML
    private Label numberOfLate;
    @FXML
    private TableColumn<BorrowRequestRecord, String> title;
    @FXML
    private TableColumn<BorrowRequestRecord, String> quantity;
    @FXML
    private TableColumn<BorrowRequestRecord, String> borrowDate;
    @FXML
    private TableColumn<BorrowRequestRecord, String> dueDate;
    @FXML
    private TableView<BorrowRequestRecord> table;
    @FXML
    private BarChart<String, Long> chart;
    @FXML
    private ImageView avatar;
    @FXML
    private Text nameLabel;

    public void initialize() {
        nameLabel.setText(UserManager.getUserInstance().getInfo().getName());
        if (UserManager.getUserInstance().getInfo().getAvatar() != null) {
            avatar.setImage(new Image(Objects.requireNonNull(Main.class.getResource
                    ("avatar/" + UserManager.getUserInstance().getInfo().getAvatar())).toString()));
        }

        ObservableList<BorrowRequestRecord> data = ((Member) UserManager.getUserInstance()).getBorrowRequestRecords();

        XYChart.Series<String, Long> set1 = new XYChart.Series<>();
        set1.getData().add(new XYChart.Data<>("Tháng 1", data.stream().filter(i -> i.getBorrowDate() != null)
                .filter(i -> LocalDate.parse(i.getBorrowDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getMonthValue() == 1).count()));
        set1.getData().add(new XYChart.Data<>("Tháng 2", data.stream().filter(i -> i.getBorrowDate() != null)
                .filter(i -> LocalDate.parse(i.getBorrowDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getMonthValue() == 2).count()));
        set1.getData().add(new XYChart.Data<>("Tháng 3", data.stream().filter(i -> i.getBorrowDate() != null)
                .filter(i -> LocalDate.parse(i.getBorrowDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getMonthValue() == 3).count()));
        set1.getData().add(new XYChart.Data<>("Tháng 4", data.stream().filter(i -> i.getBorrowDate() != null)
                .filter(i -> LocalDate.parse(i.getBorrowDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getMonthValue() == 4).count()));
        set1.getData().add(new XYChart.Data<>("Tháng 5", data.stream().filter(i -> i.getBorrowDate() != null)
                .filter(i -> LocalDate.parse(i.getBorrowDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getMonthValue() == 5).count()));
        set1.getData().add(new XYChart.Data<>("Tháng 6", data.stream().filter(i -> i.getBorrowDate() != null)
                .filter(i -> LocalDate.parse(i.getBorrowDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getMonthValue() == 6).count()));
        set1.getData().add(new XYChart.Data<>("Tháng 7", data.stream().filter(i -> i.getBorrowDate() != null)
                .filter(i -> LocalDate.parse(i.getBorrowDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getMonthValue() == 7).count()));
        set1.getData().add(new XYChart.Data<>("Tháng 8", data.stream().filter(i -> i.getBorrowDate() != null)
                .filter(i -> LocalDate.parse(i.getBorrowDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getMonthValue() == 8).count()));
        set1.getData().add(new XYChart.Data<>("Tháng 9", data.stream().filter(i -> i.getBorrowDate() != null)
                .filter(i -> LocalDate.parse(i.getBorrowDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getMonthValue() == 9).count()));
        set1.getData().add(new XYChart.Data<>("Tháng 10", data.stream().filter(i -> i.getBorrowDate() != null)
                .filter(i -> LocalDate.parse(i.getBorrowDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getMonthValue() == 10).count()));
        set1.getData().add(new XYChart.Data<>("Tháng 11", data.stream().filter(i -> i.getBorrowDate() != null)
                .filter(i -> LocalDate.parse(i.getBorrowDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getMonthValue() == 11).count()));
        set1.getData().add(new XYChart.Data<>("Tháng 12", data.stream().filter(i -> i.getBorrowDate() != null)
                .filter(i -> LocalDate.parse(i.getBorrowDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getMonthValue() == 12).count()));

        chart.getData().addAll(set1);
        chart.setLegendVisible(false);

        ObservableList<BorrowRequestRecord> borrowRequestRecords = (((Member) UserManager.getUserInstance()).getBorrowRequestRecords());
        ObservableList<BorrowRequestRecord> borrowedRecord = FXCollections.observableArrayList(((Member) UserManager.getUserInstance())
                .getBorrowRequestRecords().stream().filter(i -> i.getStatus().equals(BorrowRequestRecord.BorrowRequestStatus.BORROWED))
                .sorted(Comparator.comparing(BorrowRequestRecord::getDueDate)).toList());
        table.getItems().setAll(borrowedRecord);
        title.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getDocumentTittle()));
        quantity.setCellValueFactory(i -> new SimpleStringProperty(((Integer) i.getValue().getQuantity()).toString()));
        borrowDate.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getBorrowDate()));
        dueDate.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getDueDate()));

        numberOfPending.setText(String.valueOf(borrowRequestRecords.stream()
                .filter(i -> i.getStatus().equals(BorrowRequestRecord.BorrowRequestStatus.PENDING)).count()));
        numberOfBorrowed.setText(String.valueOf(borrowRequestRecords.stream()
                .filter(i -> i.getStatus().equals(BorrowRequestRecord.BorrowRequestStatus.BORROWED)).count()));
        numberOfLate.setText(String.valueOf(borrowRequestRecords.stream().filter(i -> i.getStatus().equals(BorrowRequestRecord.BorrowRequestStatus.BORROWED))
                .filter(i -> LocalDate.parse(i.getDueDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).isBefore(LocalDate.now())).count()));
    }
}

