package com.example.shreyas.hola;


public class ChatMessage {
    private String messageText;
    private String messageUser;
    private String messageTime;
    private boolean isMessageSent;

    public ChatMessage(String messageText, String messageUser, String messageTime,boolean isMessageSent) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = messageTime;
        this.isMessageSent = isMessageSent;
    }
    public ChatMessage(String messageText, String messageUser, String messageTime) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = messageTime;
    }
    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        messageTime = timeStamp.getTime();
    }
    public ChatMessage() {

    }
    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public boolean getIsMessageSent() {
        return this.isMessageSent;
    }
}

