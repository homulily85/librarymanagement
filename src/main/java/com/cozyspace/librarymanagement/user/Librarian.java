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

}
