package com.example.tinder.chat;

public class MessageObject {
    private String message;
    private boolean isMyMessage;

    public MessageObject(String message, boolean isMyMessage) {
        this.message = message;
        this.isMyMessage = isMyMessage;
    }

    public MessageObject() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMyMessage() {
        return isMyMessage;
    }

    public void setMyMessage(boolean myMessage) {
        isMyMessage = myMessage;
    }
}
