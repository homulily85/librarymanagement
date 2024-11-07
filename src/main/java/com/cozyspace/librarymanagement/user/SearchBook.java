package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.Document;
import javafx.collections.ObservableList;

public interface SearchBook {
    int SEARCH_ALL_DOCUMENT = 0;
    int SEARCH_ALL_AVAILABLE_DOCUMENT = 1;
    int SEARCH_ALL_UNAVAILABLE_DOCUMENT = 2;

    /**
     * Tìm kiếm tài liệu theo tên.
     *
     * @param title từ khóa
     * @param mode  chế độ tìm kiếm, là một trong ba chế độ được khai báo trong interface SearchBook
     * @return ObservableList chứa các tài liệu mà tiêu đề có chứa từ khóa cần tìm
     */
    ObservableList<Document> searchDocumentByTitle(String title, int mode);

    /**
     * Tìm kiếm tài liệu theo tác giả.
     *
     * @param mode   chế độ tìm kiếm, là một trong ba chế độ được khai báo trong interface SearchBook
     * @param author từ khóa
     * @return ObservableList chứa các tài liệu mà tiêu đề có chứa từ khóa cần tìm
     */
    ObservableList<Document> searchDocumentByAuthor(String author, int mode);

    /**
     * Tìm kiếm theo mã ISBN
     *
     * @param mode chế độ tìm kiếm, là một trong ba chế độ được khai báo trong interface SearchBook
     * @param ISBN mã ISBN của tài liệu cần tìm
     * @return ObservableList chứa tài liệu cần tìm
     */
    ObservableList<Document> searchByISBN(String ISBN, int mode);

    /**
     * Xem tất cả các tài liệu
     *
     * @param mode chế độ tìm kiếm, là một trong ba chế độ được khai báo trong interface SearchBook
     * @return ObservableList chứa tài liệu cần tìm
     */
    ObservableList<Document> viewDocument(int mode);

}
