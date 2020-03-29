package com.example.shreyas.hola;

public class ContactDisplay {
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
