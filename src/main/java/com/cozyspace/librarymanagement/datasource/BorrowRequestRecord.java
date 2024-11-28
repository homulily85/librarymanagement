package com.cozyspace.librarymanagement.datasource;

public class BorrowRequestRecord {
    private final String requestId;
    private final int documentId;
    private final String memberName;
    private final String documentTittle;
    private final String requestDate;
    private String borrowDate;
    private final int quantity;
    private String returnDate;
    private String dueDate;
    private String status;

    public BorrowRequestRecord(String requestId, int documentId  ,String name, String documentTittle, String requestDate, String borrowDate,
                               String returnDate, String dueDate, String status, int quantity) {
        this.requestId = requestId;
        this.documentId = documentId;
        this.memberName = name;
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

    public String getMemberName() {
        return memberName;
    }

    public String getDocumentTittle() {
        return documentTittle;
    }

    public int getDocumentId() {
        return documentId;
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

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    @Override
    public String toString() {
        return "BorrowRequestRecord{" +
               "documentTittle='" + documentTittle + '\'' +
               '}';
    }

    public static class BorrowRequestStatus {
        public static final String PENDING = "Đang chờ xử lí";
        public static final String BORROWED = "Đang mượn";
        public static final String RETURNED = "Đã trả";
        public static final String CANCELLED = "Đã hủy";
    }

}
