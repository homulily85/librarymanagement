package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.Datasource;
import com.cozyspace.librarymanagement.datasource.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class User implements Searchable {

    private String username;
    private String password;
    private static Person person;
    private static User instance;
    private static int code;

    User() {
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
            instance.setNewUserInfo(userInfo);
        } else if (userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ROLE - 1).equals("Librarian")) {
            person = new Person();
            instance = new Librarian();
            instance.setNewUserInfo(userInfo);
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
        code = 0;
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
    public static void setNewUserInfo(String username, String password, String name, String address, String email, String phone) {
        if (instance != null) {
            throw new RuntimeException("User instance exists.");
        }

        person = new Person();
        instance = new Member();
        instance.username = username;
        instance.password = password;
        person.setName(name);
        person.setAddress(address);
        person.setEmail(email);
        person.setPhone(phone);

    }

    /**
     * Thêm một tài khoản mới với vai trò người dùng sử dụng thông tin được đưa vào bởi phương thức setNewUserInfo.
     * Để thêm tài khoản mới với vai trò bất kì, sử dụng phương thức addAccount ở lớp Librarian.
     */
    public static void addNewMember() {
        Datasource.addUser(User.getInstance().username, User.getInstance().password, person.getName(), person.getAddress(),
                person.getAddress(), person.getPhone(), "Member");
    }

    private void setNewUserInfo(List<String> info) {
        this.username = info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_USERNAME - 1);
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
