package com.example.tinder.matches;

public class NewMatchObject {
    private String name;
    private String avatarUrl;
    private String id;

    public NewMatchObject(String name, String avatarUrl,String id) {
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NewMatchObject(){

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
}
