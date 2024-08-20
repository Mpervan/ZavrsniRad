package com.example.prehrambenica1;

import android.graphics.Bitmap;

public class Restaurant {
    private Integer ID;
    private String name;
    private String address;
    private String phone;
    private String description;
    private String open;
    private String owner;
    private Bitmap image;

    public Restaurant(){}
    public Integer getID(){
        return ID;
    }
    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public String getPhone(){
        return phone;
    }
    public String getDescription(){
        return description;
    }
    public String getOwner(){
        return owner;
    }
    public String getOpen(){
        return open;
    }
    public void setID(Integer id) {
        ID = id;
    }
    public void setName(String n) {
        name = n;
    }
    public void setAddress(String add) {
        address = add;
    }
    public void setPhone(String pho) {
        phone = pho;
    }
    public void setDescription(String desc) {
        description = desc;
    }
    public void setOwner(String user) {
        owner = user;
    }
    public void setOpen(String o) {
        open = o;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
