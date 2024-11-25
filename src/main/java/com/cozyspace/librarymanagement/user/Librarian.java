package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.BorrowRequestRecord;
import com.cozyspace.librarymanagement.datasource.Datasource;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.datasource.MemberRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public final class Librarian extends User implements SearchMember {

    private Librarian(String name, String address, String email, String phone, String avatar, String username) {
        super(name, address, email, phone, avatar, username);
    }

    private static Librarian instance = null;

    /**
     * Kiểm tra xem đã có đối tượng kiểu Librarian nào tồn tại không
     *
     * @return true nếu có, false nếu không
     */
    public static boolean isInstanceExist() {
        return instance != null;
    }

    static Librarian getInstance() {
        if (!isInstanceExist()) throw new RuntimeException("Librarian instance does not exist");
        return instance;
    }

    /**
     * Tạo một đối tượng Member khi chưa có đối tượng thuộc kiểu Librarian nào tồn tại.
     */
    static void createNewInstance(String name, String address, String email, String phone, String avatar, String username) {
        if (isInstanceExist()) throw new RuntimeException("Librarian instance exists");
        instance = new Librarian(name, address, email, phone, avatar, username);
    }

    /**
     * Xóa đối tượng thuộc kiểu Librarian.
     */
    static void removeInstance() {
        instance = null;
    }

    /**
     * Thêm tài liệu mới vào cơ sở dữ liệu
     *
     * @param newDoc Tài liệu cần thêm
     */
    public void addDocument(Document newDoc) {
        Datasource.addNewDocument(newDoc);
    }

    /**
     * Xóa tài liệu ra khỏi cơ sở dữ liệu.
     */
    public void removeDocument(int documentId) {
        Datasource.removeDocument(documentId);
    }

    /**
     * Thay đổi thông tin của tài liệu
     */
    public void editDocument(Document document) {
        Datasource.updateDocumentInfo(document);
    }

    /**
     * Thay đổi thông tin của một thành viên (trừ tên đăng nhập và mặt khảu)
     */
    public void updateMemberInfo(MemberRecord newMemberRecord) {
        Datasource.updateMemberInfo(newMemberRecord);
    }

    public ObservableList<MemberRecord> viewAllMember() {

        return Datasource.viewAllMember();
    }

    @Override
    public ObservableList<MemberRecord> searchMember(String query) {
        ObservableList<MemberRecord> data = Datasource.viewAllMember();
        if (data == null || data.isEmpty()) return null;
        else return data.stream()
                .filter(member -> member.getName().toLowerCase().contains(query) ||
                                  member.getEmail().toLowerCase().contains(query) ||
                                  member.getPhone().toLowerCase().contains(query))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    public ObservableList<BorrowRequestRecord> viewAllBorrowRequestRecords() {
        return Datasource.viewAllBorrowRequest();
    }

    public ObservableList<BorrowRequestRecord> searchBorrowRequest(String keyword) {
        ObservableList<BorrowRequestRecord> data = Datasource.viewAllBorrowRequest();
        if (data == null || data.isEmpty()) return null;
        else return data.stream()
                .filter(record -> record.getRequestId().toLowerCase().contains(keyword) ||
                                  record.getDocumentTittle().toLowerCase().contains(keyword) ||
                                  record.getMemberName().toLowerCase().contains(keyword))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    public void createNewBorrowRequest(String username, int documentId, int quantity, String dueDate) {
        Datasource.createNewBorrowRequest(username, documentId, quantity, LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), null, dueDate, BorrowRequestRecord.BorrowRequestStatus.BORROWED);
    }

    public void updateBorrowRequest(BorrowRequestRecord record) {
        Datasource.updateBorrowRequest(record);
    }

    @Override
    public ObservableList<Document> searchDocument(String keyword, int mode) {
        ObservableList<Document> data = Datasource.viewAllDocument(mode);
        if (data == null || data.isEmpty()) return null;
        else return data.stream()
                .filter(document -> document.getTitle().toLowerCase().contains(keyword) ||
                                    (document.getAuthor() != null && document.getAuthor().toLowerCase().contains(keyword) ||
                                     (document.getISBN() != null && document.getISBN().equals(keyword))))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
}
