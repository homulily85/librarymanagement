package com.cozyspace.librarymanagement.datasource;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.Main;
import com.cozyspace.librarymanagement.user.SearchBook;
import com.password4j.Hash;
import com.password4j.Password;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Datasource {

    private Datasource() {

    }

//    public static final String DB_NAME = Objects.requireNonNull(Main.class.getResource("library.db")).toString();
//    public static final String CONNECTION_NAME = "jdbc:sqlite:%s".formatted(DB_NAME);

    //TODO: Only enable this line when running the jar file
    public static final String CONNECTION_NAME = "jdbc:sqlite:%s/library.db".formatted(DataTransfer.getInstance().getDataMap().get("jarPath"));

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

    public static final String TABLE_BORROW_REQUEST = "borrow_request";
    public static final String TABLE_BORROW_REQUEST_COLUMN_ID = "id";
    public static final String TABLE_BORROW_REQUEST_COLUMN_USERNAME = "username";
    public static final String TABLE_BORROW_REQUEST_COLUMN_DOCUMENT_ID = "documentID";
    public static final String TABLE_BORROW_REQUEST_COLUMN_REQUEST_DATE = "requestDate";
    public static final String TABLE_BORROW_REQUEST_COLUMN_BORROW_DATE = "borrowDate";
    public static final String TABLE_BORROW_REQUEST_COLUMN_RETURN_DATE = "returnDate";
    public static final String TABLE_BORROW_REQUEST_COLUMN_DUE_DATE = "dueDate";
    public static final String TABLE_BORROW_REQUEST_COLUMN_STATUS = "status";
    public static final String TABLE_BORROW_REQUEST_COLUMN_QUANTITY = "quantity";

    public static final int TABLE_BORROW_REQUEST_INDEX_COLUMN_ID = 1;
    public static final int TABLE_BORROW_REQUEST_INDEX_COLUMN_USERNAME = 2;
    public static final int TABLE_BORROW_REQUEST_INDEX_COLUMN_DOCUMENT_ID = 3;
    public static final int TABLE_BORROW_REQUEST_INDEX_COLUMN_REQUEST_DATE = 4;
    public static final int TABLE_BORROW_REQUEST_INDEX_COLUMN_BORROW_DATE = 5;
    public static final int TABLE_BORROW_REQUEST_INDEX_COLUMN_RETURN_DATE = 6;
    public static final int TABLE_BORROW_REQUEST_INDEX_COLUMN_DUE_DATE = 7;
    public static final int TABLE_BORROW_REQUEST_INDEX_COLUMN_STATUS = 8;
    public static final int TABLE_BORROW_REQUEST_INDEX_COLUMN_QUANTITY = 9;

    private static Connection connection = null;

    /**
     * Bắt đầu kết nối tới CSDL account
     */
    public static void openConnection() {
        if (connection != null) return;
        try {
            connection = DriverManager.getConnection(CONNECTION_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
                        query = connection.prepareStatement("select * from %s where %s like ? order by %s"
                                .formatted(TABLE_DOCUMENT, searchType, TABLE_DOCUMENT_COLUMN_ID));
                case SearchBook.SEARCH_ALL_AVAILABLE_DOCUMENT ->
                        query = connection.prepareStatement("select * from %s where %s like ? and %s>0 order by %s"
                                .formatted(TABLE_DOCUMENT, searchType, TABLE_DOCUMENT_COLUMN_QUANTITY, TABLE_DOCUMENT_COLUMN_ID));
                case SearchBook.SEARCH_ALL_UNAVAILABLE_DOCUMENT ->
                        query = connection.prepareStatement("select * from %s where %s like ? and %s=0 order by %s"
                                .formatted(TABLE_DOCUMENT, searchType, TABLE_DOCUMENT_COLUMN_QUANTITY, TABLE_DOCUMENT_COLUMN_ID));
            }

            query.setString(1, sb.toString());

            return getDocuments(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObservableList<Document> viewAllDocument(int mode) {
        try {
            PreparedStatement query = connection.prepareStatement("select * from %s where %s > 0 order by %s"
                    .formatted(TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_QUANTITY, TABLE_DOCUMENT_COLUMN_ID));
            switch (mode) {
                case SearchBook.SEARCH_ALL_DOCUMENT ->
                        query = connection.prepareStatement("select * from %s order by %s"
                                .formatted(TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_ID));
                case SearchBook.SEARCH_ALL_AVAILABLE_DOCUMENT ->
                        query = connection.prepareStatement("select * from %s where %s > 0 order by %s"
                                .formatted(TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_QUANTITY, TABLE_DOCUMENT_COLUMN_ID));
                case SearchBook.SEARCH_ALL_UNAVAILABLE_DOCUMENT ->
                        query = connection.prepareStatement("select * from %s where %s = 0 order by %s"
                                .formatted(TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_QUANTITY, TABLE_DOCUMENT_COLUMN_ID));
            }
            return getDocuments(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ObservableList<Document> getDocuments(PreparedStatement query) throws SQLException {
        ResultSet resultSet = query.executeQuery();
        ObservableList<Document> result = FXCollections.observableList(new ArrayList<>());
        while (resultSet.next()) {
            result.add(new Document(resultSet.getInt(TABLE_DOCUMENT_INDEX_COLUMN_ID),
                    resultSet.getString(TABLE_DOCUMENT_INDEX_COLUMN_ISBN),
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

    public static ObservableList<Document> getDocumentByIdOrTittle(String query) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from %s where %s like ? or %s = ? order by %s"
                    .formatted(TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_TITLE, TABLE_DOCUMENT_COLUMN_ID, TABLE_DOCUMENT_COLUMN_ID));
            preparedStatement.setString(1, "%" + query + "%");
            preparedStatement.setString(2, query);
            return getDocuments(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            query.setString(1, newDoc.getTitle());
            query.setString(2, newDoc.getAuthor());
            query.setString(3, newDoc.getDescription());
            query.setString(4, newDoc.getType());
            query.setInt(5, newDoc.getQuantity());
            query.setString(6, newDoc.getISBN());
            query.setString(7, newDoc.getSubject());
            query.setString(8, newDoc.getCoverPageLocation());
            query.executeUpdate();
            query.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateDocumentInfo(Document document) {
        try {
            PreparedStatement query = connection.prepareStatement(
                    "update %s set %s = ?, %s = ?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=? where %s = ?"
                            .formatted(TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_AUTHOR, TABLE_DOCUMENT_COLUMN_DESCRIPTION,
                                    TABLE_DOCUMENT_COLUMN_TYPE, TABLE_DOCUMENT_COLUMN_QUANTITY, TABLE_DOCUMENT_COLUMN_ISBN,
                                    TABLE_DOCUMENT_COLUMN_SUBJECT, TABLE_DOCUMENT_COLUMN_TITLE, TABLE_DOCUMENT_COLUMN_COVER_PAGE_LOCATION,
                                    TABLE_DOCUMENT_COLUMN_ID));
            query.setString(1, document.getAuthor());
            query.setString(2, document.getDescription());
            query.setString(3, document.getType());
            query.setInt(4, document.getQuantity());
            query.setString(5, document.getISBN());
            query.setString(6, document.getSubject());
            query.setString(7, document.getTitle());
            query.setString(8, document.getCoverPageLocation());
            query.setInt(9, document.getId());
            query.executeUpdate();
            query.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<MemberRecord> queryMember(String searchType, String value) {
        PreparedStatement query;
        try {
            if (!searchType.equals(TABLE_ACCOUNT_COLUMN_NAME)) {
                query = connection.prepareStatement("select * from %s where %s = ? and %s =?"
                        .formatted(TABLE_ACCOUNT, searchType, TABLE_ACCOUNT_COLUMN_ROLE));
                query.setString(1, value);
                query.setString(2, "Member");

            } else {
                query = connection.prepareStatement("select * from %s where %s like ? and %s =?"
                        .formatted(TABLE_ACCOUNT, searchType, TABLE_ACCOUNT_COLUMN_ROLE));
                query.setString(1, "%" + value + "%");
                query.setString(2, "Member");

            }

            return getMemberRecord(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ObservableList<MemberRecord> getMemberRecord(PreparedStatement query) throws SQLException {
        ResultSet resultSet = query.executeQuery();
        ObservableList<MemberRecord> result = FXCollections.observableList(new ArrayList<>());
        while (resultSet.next()) {
            result.add(new MemberRecord(resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_USERNAME),
                    resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_NAME),
                    resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_ADDRESS),
                    resultSet.getString(TABLE_ACCOUNT_INDEX_COLUMN_EMAIL),
                    resultSet.getString(TABLE_ACCOUNT_COLUMN_PHONE)));
        }
        resultSet.close();
        query.close();
        return result;
    }

    public static ObservableList<MemberRecord> viewAllMember() {
        try {
            PreparedStatement query = connection.prepareStatement("select * from %s where %s = ?"
                    .formatted(TABLE_ACCOUNT, TABLE_ACCOUNT_COLUMN_ROLE));

            query.setString(1, "Member");

            return getMemberRecord(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateMemberInfo(MemberRecord newMemberRecord) {
        try {
            PreparedStatement query = connection.prepareStatement(
                    "update %s set %s = ?, %s = ?, %s=?, %s=? where %s = ?"
                            .formatted(TABLE_ACCOUNT, TABLE_ACCOUNT_COLUMN_ADDRESS, TABLE_ACCOUNT_COLUMN_PHONE,
                                    TABLE_ACCOUNT_COLUMN_EMAIL, TABLE_ACCOUNT_COLUMN_NAME, TABLE_ACCOUNT_COLUMN_USERNAME));
            query.setString(1, newMemberRecord.getAddress());
            query.setString(2, newMemberRecord.getPhone());
            query.setString(3, newMemberRecord.getEmail());
            query.setString(4, newMemberRecord.getName());
            query.setString(5, newMemberRecord.getUsername());
            query.executeUpdate();
            query.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<BorrowRequestRecord> viewAllBorrowRequest() {
        try {
            PreparedStatement query = connection.prepareStatement("""
                    select *
                    from (%s join %s on %s.%s= %s.%s)
                             join %s using (%s);
                    """
                    .formatted(TABLE_BORROW_REQUEST, TABLE_DOCUMENT, TABLE_BORROW_REQUEST, TABLE_BORROW_REQUEST_COLUMN_DOCUMENT_ID,
                            TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_ID, TABLE_ACCOUNT, TABLE_ACCOUNT_COLUMN_USERNAME));
            return getBorrowRequestRecord(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ObservableList<BorrowRequestRecord> getBorrowRequestRecord(PreparedStatement query) {
        try {
            ResultSet resultSet = query.executeQuery();
            ObservableList<BorrowRequestRecord> result = FXCollections.observableList(new ArrayList<>());
            while (resultSet.next()) {
                result.add(new BorrowRequestRecord(resultSet.getString(TABLE_BORROW_REQUEST_INDEX_COLUMN_ID),
                        resultSet.getInt(TABLE_BORROW_REQUEST_INDEX_COLUMN_DOCUMENT_ID),
                        resultSet.getString(TABLE_ACCOUNT_COLUMN_NAME),
                        resultSet.getString(TABLE_DOCUMENT_COLUMN_TITLE),
                        resultSet.getString(TABLE_BORROW_REQUEST_INDEX_COLUMN_REQUEST_DATE),
                        resultSet.getString(TABLE_BORROW_REQUEST_INDEX_COLUMN_BORROW_DATE),
                        resultSet.getString(TABLE_BORROW_REQUEST_INDEX_COLUMN_RETURN_DATE),
                        resultSet.getString(TABLE_BORROW_REQUEST_INDEX_COLUMN_DUE_DATE),
                        resultSet.getString(TABLE_BORROW_REQUEST_INDEX_COLUMN_STATUS)
                        , resultSet.getInt(TABLE_BORROW_REQUEST_INDEX_COLUMN_QUANTITY)));
            }
            resultSet.close();
            query.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObservableList<BorrowRequestRecord> searchBorrowRequestByRequestID(String id) {
        try {
            PreparedStatement query = connection.prepareStatement("""
                    select *
                    from (%s join %s on %s.%s= %s.%s)
                             join %s using (%s)
                    where %s.%s = ?;
                    """
                    .formatted(TABLE_BORROW_REQUEST, TABLE_DOCUMENT, TABLE_BORROW_REQUEST, TABLE_BORROW_REQUEST_COLUMN_DOCUMENT_ID,
                            TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_ID, TABLE_ACCOUNT, TABLE_ACCOUNT_COLUMN_USERNAME,
                            TABLE_BORROW_REQUEST, TABLE_BORROW_REQUEST_COLUMN_ID));
            query.setString(1, id);
            return getBorrowRequestRecord(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObservableList<BorrowRequestRecord> searchBorrowRequestByMemberName(String memberName) {
        try {
            PreparedStatement query = connection.prepareStatement("""
                    select *
                    from (%s join %s on %s.%s= %s.%s)
                             join %s using (%s)
                    where %s.%s like ?;
                    """
                    .formatted(TABLE_BORROW_REQUEST, TABLE_DOCUMENT, TABLE_BORROW_REQUEST, TABLE_BORROW_REQUEST_COLUMN_DOCUMENT_ID,
                            TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_ID, TABLE_ACCOUNT, TABLE_ACCOUNT_COLUMN_USERNAME,
                            TABLE_ACCOUNT, TABLE_ACCOUNT_COLUMN_NAME));
            query.setString(1, "%" + memberName + "%");
            return getBorrowRequestRecord(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObservableList<BorrowRequestRecord> searchBorrowRequestByDocumentTittle(String documentTittle) {
        try {
            PreparedStatement query = connection.prepareStatement("""
                    select *
                    from (%s join %s on %s.%s= %s.%s)
                             join %s using (%s)
                    where %s.%s like ?;
                    """
                    .formatted(TABLE_BORROW_REQUEST, TABLE_DOCUMENT, TABLE_BORROW_REQUEST, TABLE_BORROW_REQUEST_COLUMN_DOCUMENT_ID,
                            TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_ID, TABLE_ACCOUNT, TABLE_ACCOUNT_COLUMN_USERNAME,
                            TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_TITLE));
            query.setString(1, "%" + documentTittle + "%");
            return getBorrowRequestRecord(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createNewBorrowRequest(String username, int documentId, int quantity, String requestDate,
                                              String borrowDate, String returnDate, String dueDate, String status) {
        try {
            PreparedStatement query = connection.prepareStatement("""
                    insert into %s (%s, %s, %s, %s, %s, %s, %s, %s)
                    values (?,? ,? ,? ,? ,? ,? ,?);
                    """
                    .formatted(TABLE_BORROW_REQUEST, TABLE_BORROW_REQUEST_COLUMN_USERNAME, TABLE_BORROW_REQUEST_COLUMN_DOCUMENT_ID,
                            TABLE_BORROW_REQUEST_COLUMN_REQUEST_DATE, TABLE_BORROW_REQUEST_COLUMN_BORROW_DATE,
                            TABLE_BORROW_REQUEST_COLUMN_RETURN_DATE, TABLE_BORROW_REQUEST_COLUMN_DUE_DATE,
                            TABLE_BORROW_REQUEST_COLUMN_STATUS, TABLE_BORROW_REQUEST_COLUMN_QUANTITY));
            query.setString(1, username);
            query.setInt(2, documentId);
            query.setString(3, requestDate);
            query.setString(4, borrowDate);
            query.setString(5, returnDate);
            query.setString(6, dueDate);
            query.setString(7, status);
            query.setInt(8, quantity);
            query.executeUpdate();
            query.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeDocument(int documentId) {
        try {
            PreparedStatement query = connection.prepareStatement("delete from %s where %s = ?"
                    .formatted(TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_ID));
            query.setInt(1, documentId);
            query.executeUpdate();
            query.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBorrowRequest(BorrowRequestRecord record) {
        try {
            PreparedStatement query = connection.prepareStatement("""
                    update %s
                    set %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?
                    where %s = ?;
                    """
                    .formatted(TABLE_BORROW_REQUEST, TABLE_BORROW_REQUEST_COLUMN_RETURN_DATE,
                            TABLE_BORROW_REQUEST_COLUMN_STATUS, TABLE_BORROW_REQUEST_COLUMN_BORROW_DATE,
                            TABLE_BORROW_REQUEST_COLUMN_DUE_DATE, TABLE_BORROW_REQUEST_COLUMN_QUANTITY,
                            TABLE_BORROW_REQUEST_COLUMN_RETURN_DATE, TABLE_BORROW_REQUEST_COLUMN_STATUS,
                            TABLE_BORROW_REQUEST_COLUMN_ID));
            query.setString(1, record.getReturnDate());
            query.setString(2, record.getStatus());
            query.setString(3, record.getBorrowDate());
            query.setString(4, record.getDueDate());
            query.setInt(5, record.getQuantity());
            query.setString(6, record.getReturnDate());
            query.setString(7, record.getStatus());
            query.setString(8, record.getRequestId());
            query.executeUpdate();
            query.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Document> getDocumentById(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from %s where %s = ?"
                    .formatted(TABLE_DOCUMENT, TABLE_DOCUMENT_COLUMN_ID));
            preparedStatement.setInt(1, id);
            return getDocuments(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
