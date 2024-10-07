package com.cozyspace.librarymanagement.user;

public class Person {
    private String name;
    private String address;
    private String email;
    private String phone;

    private static final Person instance = new Person();

    private Person(){

    }

    public static Person getInstance(){
        return instance;
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
}
