package com.cozyspace.librarymanagement.user;

public final class Person {
    private String name;
    private String address;
    private String email;
    private String phone;
    private String avatar;
    private final String username;
    private final String password;

    public static int PERSON_NAME_FIELD_INDEX = 0;
    public static int PERSON_ADDRESS_FIELD_INDEX = 1;
    public static int PERSON_EMAIL_FIELD_INDEX = 2;
    public static int PERSON_PHONE_FIELD_INDEX = 3;

    public Person(String name, String address, String email, String phone, String avatar, String username, String password) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.username = username;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
