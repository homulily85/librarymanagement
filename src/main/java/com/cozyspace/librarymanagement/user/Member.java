package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public final class Member extends User implements ManageComment {

    private static Member instance = null;

    private Member(String name, String address, String email, String phone, String avatar, String username) {
        super(name, address, email, phone, avatar, username);
    }

    /**
     * Kiểm tra xem đã có đối tượng kiểu Member nào tồn tại không
     *
     * @return true nếu có, false nếu không
     */
    public static boolean isInstanceExist() {
        return instance != null;
    }

    static Member getInstance() {
        if (!isInstanceExist()) throw new RuntimeException("Member instance does not exist");
        return instance;
    }

    /**
     * Tạo một đối tượng Member khi chưa có đối tượng thuộc kiểu Member nào tồn tại.
     */
    static void createNewInstance(String name, String address, String email, String phone, String avatar, String username) {
        if (isInstanceExist()) throw new RuntimeException("Member instance exists");
        instance = new Member(name, address, email, phone, avatar, username);
    }

    /**
     * Xóa đối tượng thuộc kiểu Member.
     */
    static void removeInstance() {
        instance = null;
    }

    @Override
    public void createNewBorrowRequest(String username, int documentID, int quantity, String dueDate) {
        Datasource.createNewBorrowRequest(info.getUsername(), documentID, quantity, LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                null, null, dueDate, BorrowRequestRecord.BorrowRequestStatus.PENDING);
    }

    @Override
    public void updateBorrowRequest(BorrowRequestRecord record) {
        Datasource.updateBorrowRequest(record);
    }

    @Override
    public ObservableList<BorrowRequestRecord> viewAllBorrowRequestRecords() {
        return Datasource.getBorrowRequestByMember(info.getUsername());
    }

    @Override
    public ObservableList<Comment> getCommentByDocumentID(int documentID) {
        return Datasource.getCommentByDocumentId(documentID);
    }

    @Override
    public Comment createNewComment(int documentID, String comment) {
        new Thread(
                () -> Datasource.createNewComment(info.getUsername(), documentID, comment,
                        LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))).start();
        return new Comment(info.getUsername(), documentID, comment, LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    @Override
    public ObservableList<Document> searchDocument(String keyword, int mode) {
        ObservableList<Document> data = UserManager.getUserInstance().viewDocument(mode);

        ObservableList<Document> result = data.stream()
                .filter(document -> document.getTitle().toLowerCase().contains(keyword) ||
                                    (document.getAuthor() != null && document.getAuthor().toLowerCase().contains(keyword)) ||
                                    (document.getISBN() != null && document.getISBN().equals(keyword)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        if (result.isEmpty()) {
            try {
                return GoogleBooksAPI.query(keyword);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else return result;
    }
}

