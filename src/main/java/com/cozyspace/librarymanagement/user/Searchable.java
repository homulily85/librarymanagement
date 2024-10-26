package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.Document;
import javafx.collections.ObservableList;

public interface Searchable {
    /**
     * Tìm kiếm tài liệu theo tên.
     *
     * @param title từ khóa
     * @return ObservableList chứa các tài liệu mà tiêu đề có chứa từ khóa cần tìm
     */
    public ObservableList<Document> searchDocumentByTitle(String title);

    /**
     * Tìm kiếm tài liệu theo tác giả.
     *
     * @param author từ khóa
     * @return ObservableList chứa các tài liệu mà tiêu đề có chứa từ khóa cần tìm
     */
    public ObservableList<Document> searchDocumentByAuthor(String author);

    /**
     * Tìm kiếm theo mã ISBN
     *
     * @param ISBN mã ISBN của tài liệu cần tìm
     * @return ObservableList chứa tài liệu cần tìm
     */
    public ObservableList<Document> searchByISBN(String ISBN);

    /**
     * Xem tất cả các tài liệu hiện có trong cơ sở dữ liệu.
     * Tài liệu hiện có là tài liệu có số lượng lơn hơn 0.
     */
    public ObservableList<Document> viewAllAvailableDocument();


}
