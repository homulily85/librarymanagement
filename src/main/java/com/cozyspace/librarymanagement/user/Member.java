package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.BorrowRequestRecord;
import com.cozyspace.librarymanagement.datasource.Comment;
import com.cozyspace.librarymanagement.datasource.Datasource;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class Member extends User {

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

    /**
     * Mượn tài liệu
     */
    public void createNewBorrowRequest(int documentID, int quantity, int numberOfDays) {
        Datasource.createNewBorrowRequest(info.getUsername(), documentID, quantity, LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                null, null, LocalDate.now().plusDays(numberOfDays).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                BorrowRequestRecord.BorrowRequestStatus.PENDING);
    }

    public ObservableList<BorrowRequestRecord> getBorrowRequestRecords() {
        return Datasource.getBorrowRequestByMember(info.getUsername());
    }

    public ObservableList<Comment> getCommentByDocumentID(int documentID) {
        return Datasource.getCommentByDocumentId(documentID);
    }

    public Comment createNewComment(int documentID, String comment) {
        new Thread(
                () -> Datasource.createNewComment(info.getUsername(), documentID, comment,
                        LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))).start();
        return new Comment(info.getUsername(), documentID, comment, LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
}

