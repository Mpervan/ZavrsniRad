package com.example.prehrambenica1;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import androidx.annotation.Nullable;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Database.db";
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;

    public DBHelper(Context context) {

        super(context,"Database.db", null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table users(username TEXT primary key, password TEXT, role TEXT, name TEXT, email TEXT, address TEXT, phone TEXT, image BLOB)");
        db.execSQL("create table restaurants(ID INT NOT NULL UNIQUE primary key, name TEXT, address TEXT, phone TEXT, description TEXT, open TEXT, owner TEXT, image BLOB)");
        db.execSQL("create table orders(ID INT NOT NULL UNIQUE primary key, number INT, address TEXT, callNumber TEXT, itemID INT, user TEXT, restaurantID INT, price TEXT, time TEXT, status TEXT)");
        db.execSQL("create table menuItems(ID INT NOT NULL UNIQUE primary key, itemName TEXT, restaurantID INT, price TEXT, description TEXT, image BLOB)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists users");
        db.execSQL("drop table if exists restaurants");
        db.execSQL("drop table if exists orders");
        db.execSQL("drop table if exists menuItems");
    }
    public Boolean insertData(String username, String password, String role, String name, String email, Bitmap image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();

        values.put("username", username);
        values.put("password", password);
        values.put("role", role);
        values.put("name", name);
        values.put("email", email);
        values.put("address", (String) null);
        values.put("phone", (String) null);
        values.put("image", imageInBytes);

        long result = db.insert("users", null, values);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean insertDataRest(Integer ID, String  name, String address, String phone, String description, String open, String owner){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ID", ID);
        values.put("name", name);
        values.put("address", address);
        values.put("phone", phone);
        values.put("description", description);
        values.put("open", open);
        values.put("owner", owner);
        values.put("image", (String) null);

        long result = db.insert("restaurants", null, values);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean insertDataItem(Integer ID, String name, Integer restaurant, String price, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ID", ID);
        values.put("itemName", name);
        values.put("restaurantID", restaurant);
        values.put("price", price);
        values.put("description", description);
        values.put("image", (String) null);

        long result = db.insert("menuItems", null, values);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean insertDataOrder(SingleOrder order, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ID", generateOrderID());
        values.put("number", order.getNumber());
        values.put("user", order.getUser());
        values.put("address", order.getAddress());
        values.put("callNumber", order.getCallNumber());
        values.put("itemID", order.getItemID());
        values.put("restaurantID", order.getRestaurantID());
        values.put("price", order.getPrice().toString());
        values.put("time", time);
        values.put("status", "Requested");

        long result = db.insert("orders", null, values);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean setImageRestaurant(Bitmap image, String id){
        SQLiteDatabase db = this.getWritableDatabase();

        byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put("image",imageInBytes);
        long result = db.update("restaurants", values, "ID = ?", new String[]{id});
        if(result==-1) return false;
        else
            return true;
    }
    public Boolean setImageItem(Bitmap image, String id){
        SQLiteDatabase db = this.getWritableDatabase();

        byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put("image",imageInBytes);
        long result = db.update("menuItems", values, "ID = ?", new String[]{id});
        if(result==-1) return false;
        else
            return true;
    }
    public Boolean setImageUser(Bitmap image, String username){
        SQLiteDatabase db = this.getWritableDatabase();

        byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put("image",imageInBytes);
        long result = db.update("users", values, "username = ?", new String[]{username});
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean updateDataRest(String ID, String  name, String address, String phone, String description, String open){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("address", address);
        values.put("phone", phone);
        values.put("description", description);
        values.put("open", open);

        long result = db.update("restaurants", values, "ID = ?", new String[]{ID});
        if(result==-1) return false;
        else
            return true;
    }
    public Boolean updateDataUser(String username, String fullName, String email, String address, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", fullName);
        values.put("email", email);
        values.put("address", address);
        values.put("phone", phone);

        long result = db.update("users", values, "username = ?", new String[]{username});
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean updateDataItem(String ID, String name, String description, String price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("itemName", name);
        values.put("price", price);
        values.put("description", description);

        long result = db.update("menuItems", values, "ID = ?", new String[]{ID});
        if(result==-1) return false;
        else
            return true;
    }
    public Boolean makeAdmin(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("role", "admin");

        long result = db.update("users", values, "username = ?", new String[]{username});
        if(result==-1) return false;
        else
            return true;
    }
    public Boolean orderStatusChange(Integer number, String time, String newStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from orders where number=? and time=?", new String[] {number.toString(),time});
        while(cursor.moveToNext()){
            ContentValues values = new ContentValues();

            values.put("status", newStatus);

            long result = db.update("orders", values, "number=?", new String[]{number.toString()});
            if(result==-1) return false;
            else
                return true;
        }
        return false;
    }
    public Boolean changePass(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("password", password);

        long result = db.update("users", values, "username = ?", new String[]{username});
        if(result==-1) return false;
        else
            return true;
    }
    public Boolean checkusername(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=?", new String[] {username});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public Boolean checkEmail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where email=?", new String[] {email});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public Boolean checkuserandpass(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=? and password=?", new String[]{username, password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public Boolean checkAdmin(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=? and role=?", new String[] {username, "admin"});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean checkRestaurantName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from restaurants where name=?", new String[] {name});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public User getCurrentUser(String username) {
        User user = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from users where username=?", new String[]{username});
            if (cursor.moveToFirst()) {
                user = new User();
                user.setUsername(cursor.getString(0));
                user.setPassword(cursor.getString(1));
                user.setRole(cursor.getString(2));
                user.setName(cursor.getString(3));
                user.setEmail(cursor.getString(4));
                user.setAddress(cursor.getString(5));
                user.setPhone(cursor.getString(6));
                byte[] imageBytes = cursor.getBlob(7);
                if (imageBytes != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    user.setImage(bitmap);
                }
            }
        } catch(Exception e){
            user = null;
        }
        return user;

    }
    public Restaurant getRestaurant(String ID){
        Restaurant restaurant = null;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from restaurants where ID=?", new String[] {ID});
            if(cursor.moveToFirst()){
                restaurant = new Restaurant();
                restaurant.setID(cursor.getInt(0));
                restaurant.setName(cursor.getString(1));
                restaurant.setAddress(cursor.getString(2));
                restaurant.setPhone(cursor.getString(3));
                restaurant.setDescription(cursor.getString(4));
                restaurant.setOpen(cursor.getString(5));
                restaurant.setOwner(cursor.getString(6));
                byte [] imageBytes = cursor.getBlob(7);
                if(imageBytes != null){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
                    restaurant.setImage(bitmap);
                }
            }
        } catch (Exception e){
            restaurant = null;
        }
        return restaurant;
    }
    public MenuItem getMenuItem(String ID){
        MenuItem menuItem = null;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from menuItems where ID=?", new String[] {ID});
            if(cursor.moveToFirst()){
                menuItem = new MenuItem();
                menuItem.setID(cursor.getInt(0));
                menuItem.setItem(cursor.getString(1));
                menuItem.setRestaurantID(cursor.getInt(2));
                menuItem.setPrice(cursor.getDouble(3));
                menuItem.setDescription(cursor.getString(4));
                byte [] imageBytes = cursor.getBlob(5);
                if(imageBytes != null){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
                    menuItem.setImage(bitmap);
                }
            }
        } catch (Exception e){
            menuItem = null;
        }
        return menuItem;
    }
    public SingleOrder getOrder(String ID){
        SingleOrder order = null;
        DateTimeFormatter formatter = null;
        LocalDateTime dateTime = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        }
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from orders where ID=?", new String[] {ID});

            if(cursor.moveToFirst()){
                order = new SingleOrder();
                order.setID(cursor.getInt(0));
                order.setNumber(cursor.getInt(1));
                order.setAddress(cursor.getString(2));
                order.setCallNumber(cursor.getString(3));
                order.setItemID(cursor.getInt(4));
                order.setUser(cursor.getString(5));
                order.setRestaurantID(cursor.getInt(6));
                order.setPrice(Double.parseDouble(cursor.getString(7)));
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    dateTime = LocalDateTime.parse(cursor.getString(8), formatter);
                }
                order.setTime(dateTime);
                order.setStatus(cursor.getString(9));
            }
        } catch (Exception e){
            order = null;
        }
        return order;
    }
    public ArrayList<Integer> getOrderItemsIDs(int number, String time){
        ArrayList<Integer> list = new ArrayList<Integer>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from orders where number=? and time=?", new String[]{String.valueOf(number), time});
        while(cursor.moveToNext()){
            list.add(cursor.getInt(4));
        }
        return list;
    }

    public Cursor viewData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from restaurants";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
    public Cursor viewDataAdmin(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from restaurants where owner=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        return cursor;
    }
    public Cursor viewOrderData(String restID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from orders where restaurantID=? and status=?";
        Cursor cursor = db.rawQuery(query, new String[]{restID, "Requested"});
        return cursor;
    }

    public Cursor viewOrderDataInProgress(String restID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from orders where restaurantID=? and status=?";
        Cursor cursor = db.rawQuery(query, new String[]{restID, "In progress"});
        return cursor;
    }
    public Cursor viewOrderDataInProgressUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from orders where user=? and status=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, "In progress"});
        return cursor;
    }
    public Cursor viewOrderDataOld(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from orders where user=? and status=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, "Completed"});
        return cursor;
    }

    public String getMessage(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from orders where user=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        if (cursor.moveToLast()) {
            String num = "";
            int n = cursor.getInt(1);
            String status = cursor.getString(9);
            String numRead = String.valueOf(n);
            if(status.equals("CANCELED")){
                if(n <= 9 ){
                    num = "00" + numRead;
                }
                if(n > 9 && n < 100){
                    num = "0" + numRead;
                }
                if(n > 99){
                    num = numRead;
                }
                return "order number: " + num + " CANCELED";
            }
            if(status.equals("Requested")){
                return "Waiting for order confirmation...";
            }
            else{
                return "";
            }
        }
        else{
            return "";
        }
    }
    public Cursor viewItemDataByID(String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from menuItems where ID=?";
        Cursor cursor = db.rawQuery(query, new String[]{ID});
        return cursor;
    }

    public Cursor viewItemData(String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from menuItems where restaurantID=?";
        Cursor cursor = db.rawQuery(query, new String[]{ID});
        return cursor;
    }

    public Integer orderQuantity(String restID){
        int quantity = 0;
        int num = 1000;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from orders where restaurantID=? and status=?";
        Cursor cursor = db.rawQuery(query, new String[]{restID, "Requested"});
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int extra = cursor.getInt(1);
                if(extra != num){
                    quantity = quantity + 1;
                    num = extra;
                }
            }
        }
        return quantity;
    }
    public int generateID(){
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        Cursor cursor = db.rawQuery("select * from restaurants where ID=?", new String[]{String.valueOf(id)});
        if(cursor.getCount()==0){
            return id;
        }
        else {
            while (cursor.getCount()!=0) {
                id = id + 1;
                cursor = db.rawQuery("select * from restaurants where ID=?", new String[]{String.valueOf(id)});
            }
            return id;
        }
    }
    public int generateItemID(){
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        Cursor cursor = db.rawQuery("select * from menuItems where ID=?", new String[]{String.valueOf(id)});
        if (cursor.getCount() != 0) {
            while (cursor.getCount() != 0) {
                id = id + 1;
                cursor = db.rawQuery("select * from menuItems where ID=?", new String[]{String.valueOf(id)});
            }
        }
        return id;
    }
    public int generateOrderID(){
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        Cursor cursor = db.rawQuery("select * from orders where ID=?", new String[]{String.valueOf(id)});
        if (cursor.getCount() != 0) {
            while (cursor.getCount() != 0) {
                id = id + 1;
                cursor = db.rawQuery("select * from orders where ID=?", new String[]{String.valueOf(id)});
            }
        }
        return id;
    }
    public int generateOrderNumber(){
        SQLiteDatabase db = this.getWritableDatabase();
        int number = 0;
        Cursor cursor = db.rawQuery("select * from orders", null);
        if(cursor.getCount()==0)
            return number;
        else{
            while(cursor.moveToNext()) {
                number = cursor.getInt(1);
            }
            if(number == 999) {
                return 0;
            }
            else{
                return number + 1;
            }
        }

    }

    public OrderWhole constructWholeOrder(int number, String time) {
        DateTimeFormatter formatter = null;
        LocalDateTime dateTime = null;

        OrderWhole newOrder = new OrderWhole();
        MenuItem item;
        ArrayList<MenuItem> itemList = new ArrayList<MenuItem>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.parse(time, formatter);
        }
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from orders where number=? and time=?", new String[]{String.valueOf(number), time});

            if (cursor.moveToFirst()) {
                newOrder.setFirstOrderID(cursor.getInt(0));
                newOrder.setAddress(cursor.getString(2));
                newOrder.setCallNumber(cursor.getString(3));
                String str = String.valueOf(cursor.getInt(4));
                newOrder.setUser(cursor.getString(5));
                int restID = cursor.getInt(6);
                newOrder.setNumber(number);
                newOrder.setTime(dateTime);


                newOrder.setRestaurantID(restID);

                newOrder.setPrice(Double.parseDouble(cursor.getString(7)));

                newOrder.setStatus(cursor.getString(9));

                Cursor cursor3 = db.rawQuery("select * from restaurants where ID=?", new String[]{String.valueOf(restID)});
                newOrder.setRestaurantName(cursor3.getString(1));

                while (cursor.moveToNext()) {
                    item = new MenuItem();

                    Cursor cursor2 = db.rawQuery("select * from menuItems where ID=?", new String[]{str});
                    item.setID(Integer.parseInt(str));
                    item.setDescription(cursor2.getString(4));
                    item.setPrice(Double.parseDouble(cursor2.getString(3)));
                    item.setItem(cursor2.getString(1));
                    item.setRestaurantID(cursor2.getInt(2));
                    itemList.add(item);
                }
                newOrder.setItems(itemList);
            }
        }catch (Exception e){
            newOrder = null;
        }
        return newOrder;
    }

}

