package com.example.tinder.matches;

public class ConnectedMatchObject {
    private String name;
    private String avatarUrl;
    private String lastestMessage;
    private String id;

    public ConnectedMatchObject(String name, String avatarUrl,String lastestMessage,String id) {
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.lastestMessage=lastestMessage;
        this.id=id;
    }

    public ConnectedMatchObject() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getLastestMessage() {
        return lastestMessage;
    }

    public void setLastestMessage(String lastestMessage) {
        this.lastestMessage = lastestMessage;
    }
}
