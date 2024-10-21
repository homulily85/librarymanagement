package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.Datasource;
import com.cozyspace.librarymanagement.datasource.Document;
import javafx.collections.ObservableList;

import java.util.List;

public class User implements Searchable{

    private String id;
    private final Person person;
    private static User instance;

    User() {
        person = Person.getInstance();
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
        List<String> userInfor = Datasource.getAccountInfo(username, password);
        if (userInfor == null) {
            return false;
        } else if (userInfor.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ROLE - 1).equals("Member")) {
            instance = new Member();
            instance.setInfo(userInfor);
        } else if (userInfor.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ROLE - 1).equals("Librarian")) {
            instance = new Librarian();
            instance.setInfo(userInfor);
        }
        return true;
    }

    /**
     * Đăng xuất tài khoản hiện tại. Phương thức này chỉ có thể được gọi khi đã có
     * tài khoản đăng nhập vào phần mềm.
     */
    public static void logout() {
        if (instance == null) throw new RuntimeException("User instance does not exist.");
        instance = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    private void setInfo(List<String> info) {
        this.id = info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ID - 1);
        this.person.setName(info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_NAME - 1));
        this.person.setAddress(info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ADDRESS - 1));
        this.person.setEmail(info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_EMAIL - 1));
        this.person.setPhone(info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_PHONE - 1));
    }

    /**
     * Tìm kiếm tài liệu theo tên
     */
    @Override
    public ObservableList<Document> searchDocumentByTitle(String title) {
        return Datasource.queryDocument(Datasource.TABLE_DOCUMENT_COLUMN_TITLE, title);
    }

    /**
     * Tìm kiếm tài liệu theo tác giả
     */
    @Override
    public ObservableList<Document> searchDocumentByAuthor(String author) {
        return Datasource.queryDocument(Datasource.TABLE_DOCUMENT_COLUMN_AUTHOR, author);
    }

    /**
     * Xem tất cả các tài liệu có trong cơ sở dữ liệu
     */
    @Override
    public ObservableList<Document> viewAllAvailableDocument() {
        return Datasource.getAvailableDocument();
    }


}
