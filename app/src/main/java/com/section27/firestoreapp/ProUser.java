package com.section27.firestoreapp;

public class ProUser {
    private  String name;
    private  String email;

    public ProUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // required constructor in fireBase
    public ProUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
