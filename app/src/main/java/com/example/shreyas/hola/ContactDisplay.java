package com.example.shreyas.hola;

import java.io.Serializable;

public class ContactDisplay implements Serializable {
    private String name;
    private int displayImgPath;
    public ContactDisplay(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getDisplayImgPath() {
        return displayImgPath;
    }
}
