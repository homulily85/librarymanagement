package com.cozyspace.librarymanagement.datasource;

public class Document {

    public static final String DOCUMENT_TYPE_BOOK = "book";
    public static final String DOCUMENT_TYPE_NEWSPAPER = "newspaper";
    public static final String DOCUMENT_TYPE_JOURNAL = "journal";
    public static final String DOCUMENT_TYPE_THESIS = "thesis";
    public static final String DOCUMENT_TYPE_MAGAZINE = "magazine";

    private int id;
    private String ISBN;
    private String title;
    private String author;
    private String description;
    private String type;
    private int quantity;
    private String subject;
    private String coverPageLocation;

    public int getId() {
        return id;
    }


    public Document(int id, String ISBN, String title, String author, String description, String type, int quantity,
                    String subject, String coverPageLocation) {
        this.id = id;
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.description = description;
        this.type = type;
        this.quantity = quantity;
        this.subject = subject;
        this.coverPageLocation = coverPageLocation;
    }

    public String getSubject() {
        return subject;
    }

    public String getCoverPageLocation() {
        return coverPageLocation;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getISBN() {
        return ISBN;
    }
}
