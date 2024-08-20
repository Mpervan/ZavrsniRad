package com.example.prehrambenica1;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderWhole {
    private int number;
    private String  address;
    private String callNumber;
    private ArrayList<MenuItem> items;
    private String user;
    private int restaurantID;
    private Double price;
    private LocalDateTime time;
    private int firstOrderID;
    private String status;
    private String restaurantName;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<MenuItem> items) {
        this.items = items;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public ArrayList<SingleOrder> deconstructOrders() {
        ArrayList<SingleOrder> orderList = new ArrayList<SingleOrder>();
        SingleOrder order = null;
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            order = new SingleOrder();
            order.setItemID(item.getID());
            order.setAddress(address);
            order.setCallNumber(callNumber);
            order.setPrice(price);
            order.setRestaurantID(restaurantID);
            order.setUser(user);
            order.setNumber(number);
            order.setTime(time);
            order.setStatus(status);
            orderList.add(order);
        }
        return orderList;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getFirstOrderID() {
        return firstOrderID;
    }

    public void setFirstOrderID(int firstOrderID) {
        this.firstOrderID = firstOrderID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SingleOrder convert(){
        SingleOrder order = new SingleOrder();
        order.setID(this.firstOrderID);
        order.setNumber(this.number);
        order.setAddress(this.address);
        order.setCallNumber(this.callNumber);
        order.setUser(this.user);
        order.setRestaurantID(this.restaurantID);
        order.setPrice(this.price);
        order.setTime(this.time);
        order.setStatus(this.status);
        return order;
    }
}
