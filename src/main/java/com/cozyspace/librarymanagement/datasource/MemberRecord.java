package com.cozyspace.librarymanagement.datasource;

public class MemberRecord {
    private final String username;
    private String name;
    private String address;
    private String email;
    private String phone;
    private String avatar;

    public MemberRecord(String userName, String name, String address, String email, String phone, String avatar) {
        this.username = userName;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

