package com.fyp.alike;

public class User {
    private String name,email,photoName;
    private int tempPercent;

    public String getName() {
        return name;
    }

    public String getPhotoName() {
        return photoName;
    }

    public int getTempPercent() {
        return tempPercent;
    }

    public void setTempPercent(int tempPercent) {
        this.tempPercent = tempPercent;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
