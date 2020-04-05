package com.example.shreyas.hola;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String uid;
    public User() {

    }
    public User(String username) {
        this.username=username;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username=username;
    }
    public String getUID() {
        return uid;
    }
    public void setUID(String uid) {
        this.uid=uid;
    }
}
