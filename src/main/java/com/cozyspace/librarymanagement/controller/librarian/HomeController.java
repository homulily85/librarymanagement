package com.cozyspace.librarymanagement.controller.librarian;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.datasource.BorrowRequestRecord;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.datasource.MemberRecord;
import com.cozyspace.librarymanagement.user.Librarian;
import com.cozyspace.librarymanagement.user.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class HomeController {
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
        ObservableList<Document> documents = UserManager.getUserInstance().viewDocument(
                Integer.parseInt(DataTransfer.getInstance().getDataMap().get("searchMode")));
        ObservableList<MemberRecord> memberRecords = ((Librarian) UserManager.getUserInstance()).viewAllMember();
        ObservableList<BorrowRequestRecord> request = ((Librarian) UserManager.getUserInstance()).viewAllBorrowRequest();

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
}
