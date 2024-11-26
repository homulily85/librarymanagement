package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.Datasource;
import com.cozyspace.librarymanagement.datasource.Document;
import javafx.collections.ObservableList;

public abstract class User implements SearchBook, ManageBorrowRequest {

    protected Person info;

    User(String name, String address, String email, String phone, String avatar, String username, String password) {
        info = new Person(name, address, email, phone, avatar, username, password);
    }

    public Person getInfo() {
        return info;
    }

    @Override
    public ObservableList<Document> viewDocument(int mode) {
        return Datasource.viewAllDocument(mode);
    }

}
