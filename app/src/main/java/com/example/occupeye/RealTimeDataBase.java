package com.example.occupeye;

public class RealTimeDataBase {
    private String name,email,username,password;

    public RealTimeDataBase() {
    }

    public RealTimeDataBase(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
