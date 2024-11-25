package com.cozyspace.librarymanagement.user;

import com.cozyspace.librarymanagement.datasource.BorrowRequestRecord;
import javafx.collections.ObservableList;

public interface ManageBorrowRequest {
    ObservableList<BorrowRequestRecord> viewAllBorrowRequestRecords();

    void createNewBorrowRequest(String username, int documentID, int quantity, String dueDate);

    void updateBorrowRequest(BorrowRequestRecord record);
}
