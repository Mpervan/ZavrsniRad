package com.example.prehrambenica1;

import android.graphics.Bitmap;

public class MenuItem {
    private Integer ID;
    private String itemName;
    private Integer restaurantID;
    private Double price;
    private String description;
    private Bitmap image;
    public MenuItem(){

    }
    public Integer getID() {return ID;}
    public String getItem() {return itemName;}
    public Integer getRestaurantID() {return restaurantID;}
    public Double getPrice() {return price;}
    public String getDescription() {return description;}
    public void setID(Integer id) {
        ID = id;
    }
    public void setItem(String name) {
        itemName = name;
    }
    public void setRestaurantID(Integer id) {
        restaurantID = id;
    }
    public void setPrice(Double money) {
        price = money;
    }
    public void setDescription(String desc) {
        description = desc;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
