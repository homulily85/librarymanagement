package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.Datasource;
import com.cozyspace.librarymanagement.datasource.Document;
import javafx.collections.ObservableList;

public interface Searchable {
    /**
     * Tìm kiếm tài liệu theo tên.
     * @param title tên tài liệu
     */
    public ObservableList<Document> searchDocumentByTitle(String title);

    /**
     * Tìm kiếm tài liệu theo tác giả.
     * @param author tên tác giả
     */
    public ObservableList<Document> searchDocumentByAuthor(String author);

    /**
     * Xem tất cả các tài liệu hiện có trong cơ sở dữ liệu.
     * Tài liệu hiện có là tài liệu có số lượng lơn hơn 0.
     */
    public ObservableList<Document> viewAllAvailableDocument();
}
