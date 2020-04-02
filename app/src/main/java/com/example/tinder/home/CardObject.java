package com.example.tinder.home;

public class CardObject {
    private String userId;
    private String name;
    private String age;
    private String avatarUrl;

    public CardObject(String name, String age, String avatarUrl,String userId) {
        this.name = name;
        this.age = age;
        this.avatarUrl = avatarUrl;
        this.userId=userId;
    }

    public CardObject() {
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
