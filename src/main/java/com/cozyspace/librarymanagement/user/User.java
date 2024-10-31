package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.Datasource;
import com.cozyspace.librarymanagement.datasource.Document;
import javafx.collections.ObservableList;

public abstract class User implements SearchBook {

    protected Person person;
    User(String name, String address, String email, String phone) {
        person = new Person(name, address, email, phone);
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
