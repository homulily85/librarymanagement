package com.cozyspace.librarymanagement.datasource;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDatasource {

    private static final String DB_NAME = "account.db";
    private static final String CONNECTION_NAME = "jdbc:sqlite:" + new File(DB_NAME).getAbsolutePath();

    private static final String TABLE_ACCOUNT = "account";
    private static final String COLUMN_ID = "id";
    public static final int INDEX_COLUMN_ID = 1;
    private static final String COLUMN_PASSWORD = "password";
    public static final int INDEX_COLUMN_PASSWORD = 2;
    private static final String COLUMN_NAME = "name";
    public static final int INDEX_COLUMN_NAME = 3;
    private static final String COLUMN_ADDRESS = "address";
    public static final int INDEX_COLUMN_ADDRESS = 4;
    private static final String COLUMN_EMAIL = "email";
    public static final int INDEX_COLUMN_EMAIL = 5;
    private static final String COLUMN_PHONE = "phone";
    public static final int INDEX_COLUMN_PHONE = 6;
    private static final String COLUMN_ROLE = "role";
    public static final int INDEX_COLUMN_ROLE = 7;

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

    public static List<String> getAccountInfo(String id, String password) {
        try {
            PreparedStatement query = connection.prepareStatement("Select * from %s where %s = ? and %s = ?"
                    .formatted(TABLE_ACCOUNT, COLUMN_ID, COLUMN_PASSWORD));
            query.setString(1, id);
            query.setString(2, password);
            ResultSet resultSet = query.executeQuery();
            List<String> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getString(INDEX_COLUMN_ID));
                result.add(resultSet.getString(INDEX_COLUMN_PASSWORD));
                result.add(resultSet.getString(INDEX_COLUMN_NAME));
                result.add(resultSet.getString(INDEX_COLUMN_ADDRESS));
                result.add(resultSet.getString(INDEX_COLUMN_EMAIL));
                result.add(resultSet.getString(INDEX_COLUMN_PHONE));
                result.add(resultSet.getString(INDEX_COLUMN_ROLE));
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
