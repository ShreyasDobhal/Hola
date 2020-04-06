package com.example.shreyas.hola;


public class ChatMessage {
    private String messageText;
    private String messageUser;
    private String messageTime;
    private String replyToUser;
    private String replyToMessage;
    private String imageURL;
    private boolean isLiked;
    private boolean isMessageSent;
    private String id;
    private String replyId;

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

    public void setReplyToUser(String replyToUser) {
        this.replyToUser = replyToUser;
    }

    public String getReplyToUser() {
        return replyToUser;
    }

    public void setReplyToMessage(String replyToMessage) {
        this.replyToMessage = replyToMessage;
    }

    public String getReplyToMessage() {
        return replyToMessage;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public boolean getIsLiked() {
        return isLiked;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getReplyId() {
        return replyId;
    }
}

