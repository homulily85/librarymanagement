package com.cozyspace.librarymanagement.user;

public final class Librarian extends User {

    private Librarian(String name, String address, String email, String phone) {
        super(name, address, email, phone);
    }

    private static Librarian instance = null;

    /**
     * Kiểm tra xem đã có đối tượng kiểu Librarian nào tồn tại không
     *
     * @return true nếu có, false nếu không
     */
    public static boolean isInstanceExist() {
        return instance != null;
    }

    static Librarian getInstance() {
        if (!isInstanceExist()) throw new RuntimeException("Librarian instance does not exist");
        return instance;
    }

    /**
     * Tạo một đối tượng Member khi chưa có đối tượng thuộc kiểu Librarian nào tồn tại.
     */
    static void createNewInstance(String name, String address, String email, String phone) {
        if (isInstanceExist()) throw new RuntimeException("Librarian instance exists");
        instance = new Librarian(name, address, email, phone);
    }

    /**
     * Xóa đối tượng thuộc kiểu Librarian.
     */
    static void removeInstance() {
        instance = null;
    }

    /**
     * Thêm tài liệu mới vào cơ sở dữ liệu
     */
    public void addDocument() {

    }

    /**
     * Xóa tài liệu ra khỏi cơ sở dữ liệu.
     */
    public void removeDocument() {

    }

    /**
     * Thay đổi thông tin của tài liệu
     */
    public void editDocument() {

    }

    /**
     * Tìm kiếm thành viên theo theo tên đăng nhập.
     */
    public void searchForMember() {

    }

    /**
     * Thêm một thành viên mới
     */
    public void addMember() {

    }

    /**
     * Xóa một thành viên khỏi cơ sở dữ liệu
     */
    public void removeMember() {

    }

    /**
     * Thay đổi thông tin của một thành viên (trừ tên đăng nhập và mặt khảu)
     */
    public void editMemberInfo() {

    }

    /**
     * Hiển thị danh sách tất cả thành viên.
     */
    public void viewAllMember() {

    }

}
