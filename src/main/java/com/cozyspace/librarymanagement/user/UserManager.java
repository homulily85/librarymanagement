package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.Datasource;

import java.util.List;

public final class UserManager {

    private UserManager() {

    }

    /**
     * Tạo một đối tượng User mới khi không có đối tượng nào thuộc kiểu User tồn tại
     *
     * @param username tên đăng nhập của người dùng
     */
    public static void createNewUserInstance(String username) {
        if (Member.isInstanceExist() || Librarian.isInstanceExist()) throw new RuntimeException("User instance exist");
        List<String> userInfo = Datasource.getAccountInfo(username);
        String name = userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_NAME - 1);
        String address = userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ADDRESS - 1);
        String email = userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_EMAIL - 1);
        String phone = userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_PHONE - 1);
        if (userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ROLE - 1).equals("Member")) {
            Member.createNewInstance(name, address, email, phone);
        } else if (userInfo.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ROLE - 1).equals("Librarian")) {
            Librarian.createNewInstance(name, address, email, phone);
        }
    }

    public static User getUserInstance() {
        if (Member.isInstanceExist()) return Member.getInstance();
        else if (Librarian.isInstanceExist()) return Librarian.getInstance();
        else {
            throw new RuntimeException("No User instance exists.");
        }
    }

    /**
     * Xóa đối tượng thuộc kiểu User
     */
    public static void removeUserInstance() {
        Member.removeInstance();
        Librarian.removeInstance();
    }

    /**
     * Kiểm tra thông tin đăng nhập của người dùng
     *
     * @param username tên đăng nhập
     * @param password mật khẩu
     * @return true nếu thông tin đăng nhập trùng với một bản ghi trong CSDL, false nếu ngược lại
     */
    public static boolean authenticate(String username, String password) {
        return Datasource.authenticate(username, password);
    }

    /**
     * Kiểm tra tên đăng nhập có tồn tại trong CSDL hay không
     *
     * @param username tên đăng nhập cần kiểm tra
     * @return true nếu tên đăng nhập tồn tại, false nếu ngược lại
     */
    public static boolean isUserNameExist(String username) {
        return Datasource.searchForUserName(username);
    }

    /**
     * Tạo một người dùng mới vào trong CSDL
     *
     * @param username tên đăng nhập
     * @param password mật khẩu
     * @param name     tên
     * @param address  địa chỉ
     * @param email    địa chỉ email
     * @param phone    số điện thoại
     * @param role     vai trò
     */

    public static void createNewUser(String username, String password, String name, String address, String email, String phone, String role) {
        Datasource.addUser(username, password, name, address, email, phone, role);
    }

    /**
     * Thay đổi mật khẩu của người dùng.
     *
     * @param username    tên đăng nhập
     * @param newPassword mật khâu mới
     */
    public static void updatePassword(String username, String newPassword) {
        Datasource.updatePassword(username, newPassword);
    }

}
