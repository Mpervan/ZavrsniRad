package com.example.prehrambenica1;

import android.graphics.Bitmap;

public class User {
    private String username;
    private String password;
    private String role;
    private String name;
    private String email;
    private String address;
    private String phone;
    private Bitmap image;


    public User(){
    }
    public String getUsername(){
        return username;
    }
    public String getRole(){
        return role;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }

    public void setUsername(String user){
        username = user;
    }
    public void setPassword(String pass){
        password = pass;
    }

    public void setRole(String rol){
        role = rol;
    }
    public void setName(String nam){
        name = nam;
    }

    public void setEmail(String mail){
        email = mail;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
