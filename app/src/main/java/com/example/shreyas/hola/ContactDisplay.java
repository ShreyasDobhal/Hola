package com.example.shreyas.hola;

import java.io.Serializable;

public class ContactDisplay implements Serializable {
    private String name;
    private int displayImgPath;
    private String uid;
    public ContactDisplay(String name,String uid) {
        this.name = name;
        this.uid = uid;
    }

    public String getName(){
        return name;
    }

    public int getDisplayImgPath() {
        return displayImgPath;
    }

    public String getUID() {
        return uid;
    }
}
