package com.cozyspace.librarymanagement.user;


// Do chỉ có tài khoản sử dụng phần mềm tại một thời điểm nên ta chỉ tạo duy nhất một đối tượng Librarian.
// Vì vậy ta có thể sử dụng mẫu thiết kế Singleton

public class Librarian extends Account {
    private static Librarian instance = null;

    private Librarian() {
    }

    public static Librarian getInstance() {
        if (instance == null) instance = new Librarian();
        return instance;
    }

    /**
     * Xem tất cả các tài liệu có trong cơ sở dữ liệu
     */
    public void viewAllAvailableBook() {

    }

    /**
     * Thêm tài liệu mới vào cơ sở dữ liệu
     */
    public void addDocument() {

    }

    /**
     * Xóa tài liệu ra khỏi cơ sở dữ liệu.
     *
     */
    public void removeDocument(){

    }

    /**
     * Thay đổi thông tin của tài liệu
     */
    public void editABook(){

    }

    public void searchForMember(){

    }

    public void addMember(){

    }

    public void removeMember(){

    }

    public void editMemberInfo(){

    }

    public void viewAllMember(){

    }

}
