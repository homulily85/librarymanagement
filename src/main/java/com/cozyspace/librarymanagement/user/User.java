package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.Datasource;
import com.cozyspace.librarymanagement.datasource.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class User implements Searchable {

    private String id;
    private static Person person;
    private static User instance;

    User() {
    }

    public static User getInstance() {
        return instance;
    }

    /**
     * Đăng nhập tài khoản. Phương thức này chỉ có thể được gọi khi có khi phần mềm ở
     * trạng thái chưa đăng nhập.
     *
     * @param username tên đăng nhập
     * @param password mật khẩu
     * @return true nếu tồn tại một bản ghi trong cơ sở dữ liệu tương ứng với thông tin nhập vào,
     * false nếu ngược lại
     */
    public static boolean login(String username, String password) {
        if (instance != null) {
            throw new RuntimeException("User instance exists.");
        }
        List<String> userInfo = Datasource.getAccountInfo(username, password);
        if (userInfo == null) {
            return false;
        } else if (userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ROLE - 1).equals("Member")) {
            person = new Person();
            instance = new Member();
            instance.setInfo(userInfo);
        } else if (userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ROLE - 1).equals("Librarian")) {
            person = new Person();
            instance = new Librarian();
            instance.setInfo(userInfo);
        }
        return true;
    }

    /**
     * Đăng xuất tài khoản hiện tại. Phương thức này chỉ có thể được gọi khi đã có
     * tài khoản đăng nhập vào phần mềm.
     */
    public static void logout() {
        if (instance == null) throw new RuntimeException("User instance does not exist.");
        person = null;
        instance = null;
    }

    public String getId() {
        return id;
    }

    private void setInfo(List<String> info) {
        this.id = info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ID - 1);
        person.setName(info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_NAME - 1));
        person.setAddress(info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ADDRESS - 1));
        person.setEmail(info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_EMAIL - 1));
        person.setPhone(info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_PHONE - 1));
    }

    public ObservableList<String> getUserInfo() {
        ObservableList<String> userInfo = FXCollections.observableArrayList();
        userInfo.add(person.getName());
        userInfo.add(person.getAddress());
        userInfo.add(person.getEmail());
        userInfo.add(person.getPhone());
        return userInfo;
    }

    @Override
    public ObservableList<Document> searchDocumentByTitle(String title) {
        return Datasource.queryDocument(Datasource.TABLE_DOCUMENT_COLUMN_TITLE, title);
    }

    @Override
    public ObservableList<Document> searchDocumentByAuthor(String author) {
        return Datasource.queryDocument(Datasource.TABLE_DOCUMENT_COLUMN_AUTHOR, author);
    }

    @Override
    public ObservableList<Document> searchByISBN(String ISBN) {
        return Datasource.queryDocument(Datasource.TABLE_DOCUMENT_COLUMN_ISBN,ISBN);
    }

    @Override
    public ObservableList<Document> viewAllAvailableDocument() {
        return Datasource.getAvailableDocument();
    }

}
