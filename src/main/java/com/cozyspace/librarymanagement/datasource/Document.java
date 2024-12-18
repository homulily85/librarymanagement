package com.cozyspace.librarymanagement.datasource;

import java.util.Objects;

public class Document {
    private static int idCounter = 0;
    private int id;
    private String ISBN;
    private String title;
    private String author;
    private String description;
    private String type;
    private int quantity;
    private String subject;
    private String coverPageLocation;

    public Document(int id, String ISBN, String title, String author, String description, String type,
                    int quantity, String subject, String coverPageLocation) {
        this.id = id;
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.description = description;
        this.type = type;
        this.quantity = quantity;
        this.subject = subject;
        this.coverPageLocation = coverPageLocation;
        idCounter = id + 1;
    }

    public Document(String ISBN, String title, String author, String description, String type,
                    int quantity, String subject, String coverPageLocation) {
        this.id = idCounter;
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.description = description;
        this.type = type;
        this.quantity = quantity;
        this.subject = subject;
        this.coverPageLocation = coverPageLocation;
        idCounter++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCoverPageLocation() {
        return coverPageLocation;
    }

    public void setCoverPageLocation(String coverPageLocation) {
        this.coverPageLocation = coverPageLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document document)) return false;
        return id == document.id && quantity == document.quantity
               && Objects.equals(ISBN, document.ISBN) && Objects.equals(title, document.title)
               && Objects.equals(author, document.author) && Objects.equals(description, document.description)
               && Objects.equals(type, document.type) && Objects.equals(subject, document.subject)
               && Objects.equals(coverPageLocation, document.coverPageLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ISBN, title, author, description, type, quantity, subject, coverPageLocation);
    }
}
