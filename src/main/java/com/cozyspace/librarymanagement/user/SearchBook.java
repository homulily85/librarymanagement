package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.Document;
import javafx.collections.ObservableList;

public interface SearchBook {
    int SEARCH_ALL_DOCUMENT = 0;
    int SEARCH_ALL_AVAILABLE_DOCUMENT = 1;
    int SEARCH_ALL_UNAVAILABLE_DOCUMENT = 2;

    ObservableList<Document> searchDocument(String keyword, int mode);

    /**
     * Xem tất cả các tài liệu
     *
     * @param mode chế độ tìm kiếm, là một trong ba chế độ được khai báo trong interface SearchBook
     * @return ObservableList chứa tài liệu cần tìm
     */
    ObservableList<Document> viewDocument(int mode);

}
