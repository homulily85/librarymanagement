package com.cozyspace.librarymanagement.datasource;

public record Document(String ISBN, String title, String author, String description, String type, int quantity,
                       String subject, String coverPageLocation) {

}
