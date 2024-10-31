package com.cozyspace.librarymanagement.user;

public final class Member extends User {

    private static Member instance = null;

    private Member(String name, String address, String email, String phone) {
        super(name, address, email, phone);
    }

    /**
     * Kiểm tra xem đã có đối tượng kiểu Member nào tồn tại không
     *
     * @return true nếu có, false nếu không
     */
    public static boolean isInstanceExist() {
        return instance != null;
    }

    static Member getInstance() {
        if (!isInstanceExist()) throw new RuntimeException("Member instance does not exist");
        return instance;
    }

    /**
     * Tạo một đối tượng Member khi chưa có đối tượng thuộc kiểu Member nào tồn tại.
     */
    static void createNewInstance(String name, String address, String email, String phone) {
        if (isInstanceExist()) throw new RuntimeException("Member instance exists");
        instance = new Member(name, address, email, phone);
    }

    /**
     * Xóa đối tượng thuộc kiểu Member.
     */
    static void removeInstance() {
        instance = null;
    }

    /**
     * Mượn tài liệu
     */
    public void borrowDocument() {

    }

    /**
     * Trả tài liệu
     */
    public void returnDocument() {
    }
}
