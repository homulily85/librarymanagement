package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.BorrowRequestRecord;
import com.cozyspace.librarymanagement.datasource.Datasource;
import com.cozyspace.librarymanagement.datasource.Document;
import com.cozyspace.librarymanagement.datasource.MemberRecord;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class Librarian extends User implements SearchMember {

    private Librarian(String name, String address, String email, String phone) {
        super(name, address, email, phone);
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
    static void createNewInstance(String name, String address, String email, String phone) {
        if (isInstanceExist()) throw new RuntimeException("Librarian instance exists");
        instance = new Librarian(name, address, email, phone);
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
    public void removeDocument() {

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
    public ObservableList<MemberRecord> searchMemberByName(String name) {
        return Datasource.queryMember(Datasource.TABLE_ACCOUNT_COLUMN_NAME, name);
    }

    @Override
    public ObservableList<MemberRecord> searchMemberByEmail(String email) {
        return Datasource.queryMember(Datasource.TABLE_ACCOUNT_COLUMN_EMAIL, email);
    }

    @Override
    public ObservableList<MemberRecord> searchMemberByPhone(String phone) {
        return Datasource.queryMember(Datasource.TABLE_ACCOUNT_COLUMN_PHONE, phone);
    }

    public ObservableList<BorrowRequestRecord> viewAllBorrowRequest() {
        return Datasource.viewAllBorrowRequest();
    }

    public ObservableList<BorrowRequestRecord> searchBorrowRequestByRequestID(String id) {
        return Datasource.searchBorrowRequestByRequestID(id);
    }

    public ObservableList<BorrowRequestRecord> searchBorrowRequestByMemberName(String memberName) {
        return Datasource.searchBorrowRequestByMemberName(memberName);
    }

    public ObservableList<BorrowRequestRecord> searchBorrowRequestByDocumentTittle(String documentTittle) {
        return Datasource.searchBorrowRequestByDocumentTittle(documentTittle);
    }

    public ObservableList<Document> searchDocumentByIdOrTitle(String idOrTitle) {
        return Datasource.getDocumentByIdOrTittle(idOrTitle);
    }

    public void createNewBorrowRequest(String username, int documentId, int quantity, String dueDate) {
        Datasource.createNewBorrowRequest(username, documentId, quantity, LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), null, dueDate, BorrowRequestRecord.BorrowRequestStatus.BORROWED);
    }

}
