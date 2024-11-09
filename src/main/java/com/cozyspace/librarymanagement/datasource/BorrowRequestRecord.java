package com.cozyspace.librarymanagement.datasource;

public class BorrowRequestRecord {
    private final String requestId;
    private final String name;
    private final String documentTittle;
    private final String requestDate;
    private final String borrowDate;
    private final int quantity;
    private String returnDate;
    private String dueDate;
    private String status;

    public BorrowRequestRecord(String requestId, String name, String documentTittle, String requestDate, String borrowDate,
                               String returnDate, String dueDate, String status, int quantity) {
        this.requestId = requestId;
        this.name = name;
        this.documentTittle = documentTittle;
        this.requestDate = requestDate;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.status = status;
        this.quantity = quantity;

    }

    public String getRequestId() {
        return requestId;
    }

    public String getName() {
        return name;
    }

    public String getDocumentTittle() {
        return documentTittle;
    }


    public String getRequestDate() {
        return requestDate;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public static class BorrowRequestStatus {
        public static final String PENDING = "Đang chờ xử lý";
        public static final String BORROWED = "Đang mượn";
        public static final String RETURNED = "Đã trả";
    }
}
