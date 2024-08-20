package com.example.prehrambenica1;

import android.graphics.Bitmap;

public class OrdersInRestClass {
    private Integer ID;
    private Bitmap image;
    private String name;
    private Integer number;

    OrdersInRestClass(){

    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
