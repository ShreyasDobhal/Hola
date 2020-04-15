package com.example.shreyas.hola;

import java.io.Serializable;

public class ContactDisplay implements Serializable {
    private String name;
    private int displayImgPath;
    private String uid;
    private String fcmToken;
    private String lastMessage="";
    private String lastMessageTime="";

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

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getFCMToken() {
        return  fcmToken;
    }
    public void setFCMToken(String fcmToken) {
        this.fcmToken=fcmToken;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }
    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
