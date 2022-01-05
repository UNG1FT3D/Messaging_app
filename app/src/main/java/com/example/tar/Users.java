package com.example.tar;

public class Users {

    String email;
    String Name;
    String UserProfile;
    String phone;
    String status;
    String uid;

    public Users() {
    }

    public Users(String email, String name , String uid,String Userprofile) {
        this.email = email;
        this.Name = name;
        this.UserProfile = Userprofile;
        this.phone = phone;
        this.status = status;
        this.uid = uid;
    }

    public Users(String email, String name, String userProfile, String phone, String status, String uid) {
        this.email = email;
        Name = name;
        UserProfile = userProfile;
        this.phone = phone;
        this.status = status;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserProfile() {
        return UserProfile;
    }

    public void setUserProfile(String userProfile) {
        UserProfile = userProfile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
