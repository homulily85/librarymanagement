package com.cozyspace.librarymanagement.datasource;

import com.password4j.Hash;
import com.password4j.Password;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    private static final String DB_NAME = "src/main/resources/com/cozyspace/librarymanagement/library.db";
    private static final String CONNECTION_NAME = "jdbc:sqlite:" + new File(DB_NAME).getAbsolutePath();

    private static final String TABLE_ACCOUNT = "account";
    private static final String TABLE_ACCOUNT_COLUMN_ID = "id";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_ID = 1;
    private static final String TABLE_ACCOUNT_COLUMN_PASSWORD = "password";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_PASSWORD = 2;
    private static final String TABLE_ACCOUNT_COLUMN_NAME = "name";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_NAME = 3;
    private static final String TABLE_ACCOUNT_COLUMN_ADDRESS = "address";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_ADDRESS = 4;
    private static final String TABLE_ACCOUNT_COLUMN_EMAIL = "email";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_EMAIL = 5;
    private static final String TABLE_ACCOUNT_COLUMN_PHONE = "phone";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_PHONE = 6;
    private static final String TABLE_ACCOUNT_COLUMN_ROLE = "role";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_ROLE = 7;

    private static final String TABLE_DOCUMENT = "document";
    private static final String TABLE_DOCUMENT_COLUMN_ID = "id";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_ID = 1;
    private static final String TABLE_DOCUMENT_COLUMN_TITLE = "title";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_TITLE = 2;
    private static final String TABLE_DOCUMENT_COLUMN_AUTHOR = "author";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_AUTHOR = 3;
    private static final String TABLE_DOCUMENT_COLUMN_DESCRIPTION = "description";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_DESCRIPTION = 4;
    private static final String TABLE_DOCUMENT_COLUMN_TYPE = "type";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_TYPE = 5;
    private static final String TABLE_DOCUMENT_COLUMN_QUANTITY = "quantity";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_QUANTITY = 6;

    private static Connection connection = null;

    /**
     * Bắt đầu kết nối tới CSDL account
     */
    public static void openConection() {
        if (connection != null) return;
        try {
            connection = DriverManager.getConnection(CONNECTION_NAME);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Đóng CSDL account.
     */
    public static void closeConnection() {
        if (connection == null) return;
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Kiểm tra và lấy thông tin người dùng.
     *
     * @param id       tên đăng nhập
     * @param password mật khẩu
     * @return null nếu tên đăng nhập hoặc mật khẩu không đúng,
     * List chứa thông tin người duùng nếu thông tin đăng nhập khớp với một bản ghi trong CSDL
     */
    public static List<String> getAccountInfo(String id, String password) {
        Hash hash = Password.hash(password).addSalt("1").withArgon2();
        try {
            PreparedStatement query = connection.prepareStatement("Select * from %s where %s = ?"
                    .formatted(TABLE_ACCOUNT, TABLE_ACCOUNT_COLUMN_ID));
            query.setString(1, id);
            ResultSet resultSet = query.executeQuery();
            List<String> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_ID));
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_PASSWORD));
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_NAME));
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_ADDRESS));
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_EMAIL));
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_PHONE));
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_ROLE));
            }
            if (!result.isEmpty() &&
                    hash.getResult().equals(result.get(TABLE_ACCOUNT_INDEX_COLUMN_PASSWORD - 1))) return result;
            else return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
