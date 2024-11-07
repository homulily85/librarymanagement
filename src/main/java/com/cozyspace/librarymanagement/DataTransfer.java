package com.cozyspace.librarymanagement;

import java.util.HashMap;

/**
 * Lớp dùng để trao đổi dữ liệu giữa các màn hình.
 * Dữ liệu được lưu trong một Hashmap với khóa là tên của giá trị trao đổi.
 */
public final class DataTransfer {
    private static DataTransfer instance;
    private final HashMap<String, String> dataMap;

    public HashMap<String, String> getDataMap() {
        return dataMap;
    }

    private DataTransfer() {
        dataMap = new HashMap<>();
    }

    public static DataTransfer getInstance() {
        if (instance == null) {
            instance = new DataTransfer();
        }
        return instance;
    }
}
