package com.example.prehrambenica1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class LocalDBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "LocalDB.db";

    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;

    public LocalDBHelper(Context context) {

        super(context, "LocalDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(username TEXT primary key, password TEXT, role TEXT, name TEXT, email TEXT, address TEXT, phone TEXT, image BLOB)");
        db.execSQL("create table menuItems(temporaryID INT primary key, ID INT, itemName TEXT, restaurantID INT, price TEXT, description TEXT, image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
        db.execSQL("drop table if exists menuItems");
    }

    public Boolean setCurrentUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        byteArrayOutputStream = new ByteArrayOutputStream();
        user.getImage().compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();

        ContentValues values = new ContentValues();

        values.put("username", user.getUsername());
        values.put("password", "hidden");
        values.put("role", user.getRole());
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("address", user.getAddress());
        values.put("phone", user.getPhone());
        values.put("image", imageInBytes);

        long result = db.insert("users", null, values);
        if (result == -1) return false;
        else
            return true;
    }
    public Boolean insertDataItem(MenuItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        byteArrayOutputStream = new ByteArrayOutputStream();
        item.getImage().compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();

        values.put("temporaryID", generateTemporaryItemID());
        values.put("ID", item.getID());
        values.put("itemName", item.getItem());
        values.put("restaurantID", item.getRestaurantID());
        values.put("price", Double.toString(item.getPrice()));
        values.put("description", item.getDescription());
        values.put("image", imageInBytes);

        long result = db.insert("menuItems", null, values);
        if(result==-1) return false;
        else
            return true;
    }
    public User getCurrentUser(){
        User user = null;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from users", null);
            if(cursor.moveToFirst()){
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
        } catch (Exception e){
            user = null;
        }
        return user;
    }

    public MenuItem getMenuItem(String ID){
        MenuItem menuItem = null;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from menuItems where ID=?", new String[] {ID});
            if(cursor.moveToFirst()){
                menuItem = new MenuItem();
                menuItem.setID(cursor.getInt(1));
                menuItem.setItem(cursor.getString(2));
                menuItem.setRestaurantID(cursor.getInt(3));
                menuItem.setPrice(cursor.getDouble(4));
                menuItem.setDescription(cursor.getString(5));
            }
        } catch (Exception e){
            menuItem = null;
        }
        return menuItem;
    }
    public void deleteCurrentUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM users");
        db.close();
    }
    public void deleteItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM menuItems");
        db.close();
    }

    public Cursor viewItemData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from menuItems";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
    public int returnRestaurantID(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from menuItems", null);
        if(cursor.getCount()==0){
            return 0;
        }
        else {
            return cursor.getInt(2);
        }
    }
    public Boolean checkItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from menuItems", null);
        if(cursor.getCount()==0){
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean checkItemsAndID(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from menuItems where restaurantID=?", new String[] {String.valueOf(ID)});
        if(checkItems() == true && cursor.getCount()==0)
        {
            return false;
        }
        else{
            return true;
        }
    }

    public void deleteItem(Integer ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String id = String.valueOf(ID);
        db.delete("menuItems", "ID=?", new String[]{id});
        db.close();
    }

    public int generateTemporaryItemID(){
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        Cursor cursor = db.rawQuery("select * from menuItems where temporaryID=?", new String[]{String.valueOf(id)});
        if(cursor.getCount()==0){
            return id;
        }
        else {
            while (cursor.getCount()!=0) {
                id = id + 1;
                cursor = db.rawQuery("select * from menuItems where temporaryID=?", new String[]{String.valueOf(id)});
            }
            return id;
        }
    }
}
