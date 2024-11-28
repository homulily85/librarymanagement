package com.cozyspace.librarymanagement.datasource;

public class Comment {
    private final String username;
    private final String name;
    private final String comment;
    private final String time;

    public Comment(String username, String comment, String date, String name) {
        this.username = username;
        this.name = name;
        this.comment = comment;
        this.time = date;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
