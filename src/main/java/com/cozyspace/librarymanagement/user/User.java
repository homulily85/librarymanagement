package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.Datasource;
import com.cozyspace.librarymanagement.datasource.Document;
import javafx.collections.ObservableList;

import java.util.List;

public class User {

    private String id;
    private String password;
    private final Person person;

    public User() {
        person = Person.getInstance();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setInfo(List<String> info) {
        this.id = info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ID - 1);
        this.password = info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_PASSWORD - 1);
        this.person.setName(info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_NAME - 1));
        this.person.setAddress(info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_ADDRESS - 1));
        this.person.setEmail(info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_EMAIL - 1));
        this.person.setPhone(info.get(Datasource.TABLE_ACCOUNT_INDEX_COLUMN_PHONE - 1));
    }

    /**
     * Tìm kiếm tài liệu theo tên
     */
    public static ObservableList<Document> searchDocumentByTitle(String title) {
        return Datasource.queryDocument(Datasource.TABLE_DOCUMENT_COLUMN_TITLE, title);
    }

    /**
     * Tìm kiếm tài liệu theo tác giả
     */
    public static ObservableList<Document> searchDocumentByAuthor(String author) {
        return Datasource.queryDocument(Datasource.TABLE_DOCUMENT_COLUMN_AUTHOR, author);
    }

    /**
     * Xem tất cả các tài liệu có trong cơ sở dữ liệu
     */
    public static ObservableList<Document> viewAllAvailableDocument() {
        return Datasource.getAvailableDocument();
    }


}
