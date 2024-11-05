package com.cozyspace.librarymanagement.datasource;

import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.user.SearchBook;
import com.password4j.Hash;
import com.password4j.Password;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.print.Doc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Datasource {

    private Datasource() {

    }

    public static final String DB_NAME = Objects.requireNonNull(Main.class.getResource("library.db")).toString();
    public static final String CONNECTION_NAME = "jdbc:sqlite:%s".formatted(DB_NAME);

    public static final String TABLE_ACCOUNT = "account";
    public static final String TABLE_ACCOUNT_COLUMN_USERNAME = "username";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_USERNAME = 1;
    public static final String TABLE_ACCOUNT_COLUMN_PASSWORD = "password";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_PASSWORD = 2;
    public static final String TABLE_ACCOUNT_COLUMN_NAME = "name";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_NAME = 3;
    public static final String TABLE_ACCOUNT_COLUMN_ADDRESS = "address";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_ADDRESS = 4;
    public static final String TABLE_ACCOUNT_COLUMN_EMAIL = "email";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_EMAIL = 5;
    public static final String TABLE_ACCOUNT_COLUMN_PHONE = "phone";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_PHONE = 6;
    public static final String TABLE_ACCOUNT_COLUMN_ROLE = "role";
    public static final int TABLE_ACCOUNT_INDEX_COLUMN_ROLE = 7;

    public static final String TABLE_DOCUMENT = "document";
    public static final String TABLE_DOCUMENT_COLUMN_ID = "id";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_ID = 1;
    public static final String TABLE_DOCUMENT_COLUMN_TITLE = "title";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_TITLE = 2;
    public static final String TABLE_DOCUMENT_COLUMN_AUTHOR = "author";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_AUTHOR = 3;
    public static final String TABLE_DOCUMENT_COLUMN_DESCRIPTION = "description";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_DESCRIPTION = 4;
    public static final String TABLE_DOCUMENT_COLUMN_TYPE = "type";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_TYPE = 5;
    public static final String TABLE_DOCUMENT_COLUMN_QUANTITY = "quantity";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_QUANTITY = 6;
    public static final String TABLE_DOCUMENT_COLUMN_ISBN = "ISBN";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_ISBN = 7;
    public static final String TABLE_DOCUMENT_COLUMN_SUBJECT = "subject";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_SUBJECT = 8;
    public static final String TABLE_DOCUMENT_COLUMN_COVER_PAGE_LOCATION = "coverPageLocation";
    public static final int TABLE_DOCUMENT_INDEX_COLUMN_COVER_PAGE_LOCATION = 9;

    private static Connection connection = null;

    /**
     * Bắt đầu kết nối tới CSDL account
     */
    public static void openConnection() {
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
     * @param username tên đăng nhập
     * @return null nếu tên đăng nhập hoặc mật khẩu không đúng,
     * List chứa thông tin người dùng nếu thông tin đăng nhập khớp với một bản ghi trong CSDL
     */
    public static List<String> getAccountInfo(String username) {
        try {
            PreparedStatement query = connection.prepareStatement("Select * from %s where %s = ?"
                    .formatted(TABLE_ACCOUNT, TABLE_ACCOUNT_COLUMN_USERNAME));
            query.setString(1, username);
            ResultSet resultSet = query.executeQuery();
            List<String> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_USERNAME));
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_PASSWORD));
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_NAME));
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_ADDRESS));
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_EMAIL));
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_PHONE));
                result.add(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_ROLE));
            }
            resultSet.close();
            query.close();
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ObservableList<Document> queryDocument(String searchType, String value, int mode) {
        StringBuilder sb = new StringBuilder(value);
        sb.append("%");
        sb.insert(0, "%");

        PreparedStatement query = null;
        try {
            switch (mode) {
                case SearchBook.SEARCH_ALL_DOCUMENT ->
                        query = connection.prepareStatement("select * from %s where %s like ?"
                                .formatted(TABLE_DOCUMENT, searchType));
                case SearchBook.SEARCH_ALL_AVAILABLE_DOCUMENT ->
                        query = connection.prepareStatement("select * from %s where %s like ? and %s>0"
                                .formatted(TABLE_DOCUMENT, searchType, TABLE_DOCUMENT_COLUMN_QUANTITY));
                case SearchBook.SEARCH_ALL_UNAVAILABLE_DOCUMENT ->
                        query = connection.prepareStatement("select * from %s where %s like ? and %s=0"
                                .formatted(TABLE_DOCUMENT, searchType, TABLE_DOCUMENT_COLUMN_QUANTITY));
            }

            query.setString(1, sb.toString());

            return getDocuments(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ObservableList<Document> getDocument(int mode) {
        try {
            PreparedStatement query = connection.prepareStatement("select * from %s where %s > 0"
                    .formatted(TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_QUANTITY));
            switch (mode) {
                case SearchBook.SEARCH_ALL_DOCUMENT -> query = connection.prepareStatement("select * from %s"
                        .formatted(TABLE_DOCUMENT));
                case SearchBook.SEARCH_ALL_AVAILABLE_DOCUMENT ->
                        query = connection.prepareStatement("select * from %s where %s > 0"
                                .formatted(TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_QUANTITY));
                case SearchBook.SEARCH_ALL_UNAVAILABLE_DOCUMENT ->
                        query = connection.prepareStatement("select * from %s where %s = 0"
                                .formatted(TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_QUANTITY));
            }
            return getDocuments(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static ObservableList<Document> getDocuments(PreparedStatement query) throws SQLException {
        ResultSet resultSet = query.executeQuery();
        ObservableList<Document> result = FXCollections.observableList(new ArrayList<>());
        while (resultSet.next()) {
            result.add(new Document(resultSet.getString(TABLE_DOCUMENT_INDEX_COLUMN_ISBN),
                    resultSet.getString(TABLE_DOCUMENT_INDEX_COLUMN_TITLE),
                    resultSet.getString(TABLE_DOCUMENT_INDEX_COLUMN_AUTHOR),
                    resultSet.getString(TABLE_DOCUMENT_INDEX_COLUMN_DESCRIPTION),
                    resultSet.getString(TABLE_DOCUMENT_INDEX_COLUMN_TYPE),
                    resultSet.getInt(TABLE_DOCUMENT_INDEX_COLUMN_QUANTITY),
                    resultSet.getString(TABLE_DOCUMENT_INDEX_COLUMN_SUBJECT),
                    resultSet.getString(TABLE_DOCUMENT_INDEX_COLUMN_COVER_PAGE_LOCATION)));

        }
        resultSet.close();
        query.close();
        return result;
    }

    /**
     * Tìm kiếm tên đăng nhập trong cơ sở dữ liệu
     *
     * @param username tên đăng nhập cần tìm
     * @return true nếu tên đăng nhập tồn tại, false nếu ngược lai.
     */
    public static boolean searchForUserName(String username) {
        boolean ans = false;
        try {
            PreparedStatement query = connection.prepareStatement("Select * from %s where %s = ?"
                    .formatted(TABLE_ACCOUNT, TABLE_ACCOUNT_COLUMN_USERNAME));
            query.setString(1, username);
            ResultSet resultSet = query.executeQuery();
            if (resultSet.isBeforeFirst()) {
                ans = true;
            }
            resultSet.close();
            query.close();
            return ans;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return ans;
        }
    }

    /**
     * Thêm tài khoản mới vào cơ sơ dữ liệu.
     *
     * @param username tên đăng nhập
     * @param password mật khẩu
     * @param name     tên
     * @param address  địa chỉ
     * @param email    email
     * @param phone    số điện thoại
     */
    public static void addUser(String username, String password, String name, String address, String email, String phone,
                               String role) {
        Hash hash = Password.hash(password).addSalt("1").withArgon2();
        try {
            PreparedStatement query = connection.prepareStatement(("insert into %s (%s, %s, %s, %s, %s, %s, %s) values (?,?,?,?,?,?,?)").
                    formatted(TABLE_ACCOUNT, TABLE_ACCOUNT_COLUMN_USERNAME, TABLE_ACCOUNT_COLUMN_PASSWORD,
                            TABLE_ACCOUNT_COLUMN_NAME, TABLE_ACCOUNT_COLUMN_ADDRESS, TABLE_ACCOUNT_COLUMN_EMAIL,
                            TABLE_ACCOUNT_COLUMN_PHONE, TABLE_ACCOUNT_COLUMN_ROLE));
            query.setString(1, username);
            query.setString(2, hash.getResult());
            query.setString(3, name);
            query.setString(4, address);
            query.setString(5, email);
            query.setString(6, phone);
            query.setString(7, role);
            query.executeUpdate();
            query.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Xác thực thông tin người dùng
     *
     * @param username tên đăng nhập
     * @param password mật khẩu
     * @return true nếu thông tin trùng với một bản ghi trong CDSL, false nếu ngược lại.
     */
    public static boolean authenticate(String username, String password) {
        Hash hash = Password.hash(password).addSalt("1").withArgon2();
        try {
            PreparedStatement query = connection.prepareStatement("Select * from %s where %s = ? and %s = ?"
                    .formatted(TABLE_ACCOUNT, TABLE_ACCOUNT_COLUMN_USERNAME, TABLE_ACCOUNT_COLUMN_PASSWORD));
            query.setString(1, username);
            query.setString(2, hash.getResult());
            ResultSet resultSet = query.executeQuery();
            boolean success = resultSet.isBeforeFirst();
            resultSet.close();
            query.close();
            return success;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void updatePassword(String username, String newPassword) {
        Hash hash = Password.hash(newPassword).addSalt("1").withArgon2();
        try {
            PreparedStatement query = connection.prepareStatement("update %s set %s = ? where %s =?"
                    .formatted(TABLE_ACCOUNT, TABLE_ACCOUNT_COLUMN_PASSWORD, TABLE_ACCOUNT_COLUMN_USERNAME));
            query.setString(1, hash.getResult());
            query.setString(2, username);
            query.executeUpdate();
            query.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addNewDocument(Document newDoc) {
        try {
            PreparedStatement query = connection.prepareStatement(("insert into %s " +
                    "(%s, %s, %s, %s, %s, %s, %s, %s) values (?,?,?,?,?,?,?,?)").
                    formatted(TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_TITLE, TABLE_DOCUMENT_COLUMN_AUTHOR,
                            TABLE_DOCUMENT_COLUMN_DESCRIPTION, TABLE_DOCUMENT_COLUMN_TYPE,
                            TABLE_DOCUMENT_COLUMN_QUANTITY, TABLE_DOCUMENT_COLUMN_ISBN, TABLE_DOCUMENT_COLUMN_SUBJECT,
                            TABLE_DOCUMENT_COLUMN_COVER_PAGE_LOCATION));
            query.setString(1, newDoc.title());
            query.setString(2, newDoc.author());
            query.setString(3, newDoc.description());
            query.setString(4, newDoc.type());
            query.setInt(5, newDoc.quantity());
            query.setString(6, newDoc.ISBN());
            query.setString(7, newDoc.subject());
            query.setString(8, newDoc.coverPageLocation());
            query.executeUpdate();
            query.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
