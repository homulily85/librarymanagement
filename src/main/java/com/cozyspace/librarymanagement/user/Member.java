package com.cozyspace.librarymanagement.user;

// Do chỉ có tài khoản sử dụng phần mềm tại một thời điểm nên ta chỉ tạo duy nhất một đối tượng Member.
// Vì vậy ta có thể sử dụng mẫu thiết kế Singleton

public class Member extends Account {
    private static Member instance = null;

    private Member() {
    }

    public static Member getInstance() {
        if (instance == null) instance = new Member();
        return instance;
    }
}