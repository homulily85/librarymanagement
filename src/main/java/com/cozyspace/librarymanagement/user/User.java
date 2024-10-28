package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.Datasource;
import com.cozyspace.librarymanagement.datasource.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class User implements SearchBook {

    private String username;
    private String password;
    private Person person;
    private static User instance;
    private static int code;

    User() {
        person = new Person();
    }

    public static void setCode(int newCode) {
        code = newCode;
    }

    public static int getCode() {
        return code;
    }

    public static User getInstance() {
        return instance;
    }

    /**
     * Đăng nhập tài khoản. Phương thức này chỉ có thể được gọi khi có khi phần mềm ở
     * trạng thái chưa đăng nhập.
     *
     * @param username tên đăng nhập
     */
    public static void createNewUserInstance(String username) {
        if (instance != null) {
            throw new RuntimeException("User instance exists.");
        }
        List<String> userInfo = Datasource.getAccountInfo(username);
        assert userInfo != null;
        if (userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ROLE - 1).equals("Member")) {
            instance = new Member();
        } else if (userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ROLE - 1).equals("Librarian")) {
            instance = new Librarian();
        }
        User.instance.person.setName(userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_NAME - 1));
        User.instance.person.setAddress(userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ADDRESS - 1));
        User.instance.person.setEmail(userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_EMAIL - 1));
        User.instance.person.setPhone(userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_PHONE - 1));
    }

    /**
     * Đăng xuất tài khoản hiện tại. Phương thức này chỉ có thể được gọi khi đã có
     * tài khoản đăng nhập vào phần mềm.
     */
    public static void logout() {
        if (instance == null) throw new RuntimeException("User instance does not exist.");
        User.instance.person = null;
        instance = null;
        code = 0;
    }

    public static boolean authenticate(String username, String password) {
        return Datasource.authenticate(username, password);
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return person.getEmail();
    }

    /**
     * Lưu thông tin người dùng khi họ đăng kí tài khoản. Phương thức này chỉ được gọi đến khi tạo tài khoản mới.
     */
    public static void setUserInfoFromDataBase(String username, String password, String name, String address, String email, String phone) {
        if (instance != null) {
            throw new RuntimeException("User instance exists.");
        }

        instance = new Member();
        instance.username = username;
        instance.password = password;
        User.instance.person.setName(name);
        User.instance.person.setAddress(address);
        User.instance.person.setEmail(email);
        User.instance.person.setPhone(phone);

    }

    /**
     * Thêm một tài khoản mới với vai trò người dùng sử dụng thông tin được đưa vào bởi phương thức setNewUserInfo.
     * Để thêm tài khoản mới với vai trò bất kì, sử dụng phương thức addAccount ở lớp Librarian.
     */
    public static void addNewMember() {
        Datasource.addUser(User.getInstance().username, User.getInstance().password, User.instance.person.getName(), User.instance.person.getAddress(),
                User.instance.person.getAddress(), User.instance.person.getPhone(), "Member");
    }

    public static void updatePassword(String username, String newPassword) {
        Datasource.updatePassword(username, newPassword);
    }

    /**
     * Kiểm tra tên đăng nhập có tồn tại không
     *
     * @param username tên đăng nhập cần kiểm tra
     * @return true nếu tên đăng nhập tồn tại, false nếu ngược lại
     */
    public static boolean isUserNameExist(String username) {
        return Datasource.searchForUserName(username);
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
        return Datasource.queryDocument(Datasource.TABLE_DOCUMENT_COLUMN_ISBN, ISBN);
    }

    @Override
    public ObservableList<Document> viewAllAvailableDocument() {
        return Datasource.getAvailableDocument();
    }

}
