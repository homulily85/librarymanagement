package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.Comment;
import javafx.collections.ObservableList;

public interface ManageComment {
    ObservableList<Comment> getCommentByDocumentID(int documentID);

    Comment createNewComment(int documentID, String comment);
}
