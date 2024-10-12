package com.cozyspace.librarymanagement.datasource;

public class Document {

    public static final String DOCUMENT_TYPE_BOOK = "book";
    public static final String DOCUMENT_TYPE_NEWSPAPER = "newspaper";
    public static final String DOCUMENT_TYPE_JOURNAL = "journal";
    public static final String DOCUMENT_TYPE_THESIS = "thesis";
    public static final String DOCUMENT_TYPE_MAGAZINE = "magazine";

    private int id;
    private String title;
    private String author;
    private String description;
    private String type;
    private int quantity;

    public Document(int id, String title, String author, String description, String type, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.type = type;
        this.quantity = quantity;
    }

    /**
     * Hiển thị thông tin tài liệu
     */
    public void showDocumentInfo() {

    }
}
