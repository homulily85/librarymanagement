package com.cozyspace.librarymanagement.datasource;

public class Comment {
    private final String username;
    private final int documentID;
    private String comment;
    private final String time;

    public Comment(String username, int documentID, String comment, String date) {
        this.username = username;
        this.documentID = documentID;
        this.comment = comment;
        this.time = date;
    }

    public String getUsername() {
        return username;
    }

    public int getDocumentID() {
        return documentID;
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
