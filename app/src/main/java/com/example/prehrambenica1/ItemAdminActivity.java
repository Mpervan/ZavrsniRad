package com.example.prehrambenica1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ItemAdminActivity extends AppCompatActivity {

    private MenuItem menuItem;
    private User currentUser;
    private Restaurant restaurant;
    private Bundle extras;
    private String ID, ItemID;
    private Button viewOrder, home, cancel;
    private DBHelper DB;
    private LocalDBHelper LDB;
    private EditText name;
    private EditText description;
    private EditText price;
    private Bitmap imageToStore;
    private Uri imageFilePath;
    private ImageView objectImageView;
    private static final int PICK_IMAGE_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_admin);


        viewOrder = (Button) findViewById(R.id.viewOrder);
        home = (Button) findViewById(R.id.home);
        cancel = (Button) findViewById(R.id.delete);

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);

        try{
            objectImageView = findViewById(R.id.imageView2);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        extras = getIntent().getExtras();
        if (extras != null) {
            ID = extras.getString("key_ID");
            ItemID = extras.getString("key_ItemID");
        }
        getIntent().removeExtra("key_ID");

        viewOrder.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ViewYourOrdersActivity.class);
                startActivity(intent);
            }
        }));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser = LDB.getCurrentUser();
                Intent intent = new Intent(getApplication(), RestaurantActivity.class);
                startActivity(intent);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = LDB.getCurrentUser();
                Boolean isAdmin = DB.checkAdmin(user.getUsername());
                Intent intent;
                if (isAdmin == true) {
                    intent = new Intent(getApplicationContext(), HomeAdminActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                }
                startActivity(intent);
            }
        });
        menuItem = DB.getMenuItem(ItemID);
        restaurant = DB.getRestaurant(ID);
        String restName = restaurant.getName();

        name = (EditText) findViewById(R.id.itemName);
        description = (EditText) findViewById(R.id.description4);
        price = (EditText) findViewById(R.id.price1);
        TextView restaurant = (TextView) findViewById(R.id.restaurantName);

        name.setText(menuItem.getItem());
        description.setText(menuItem.getDescription());
        price.setText(menuItem.getPrice().toString() + " euro");
        objectImageView.setImageBitmap(menuItem.getImage());
        restaurant.setText(restName);
    }
    public void chooseImage2(View objectView){
        try{
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*");

            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent, PICK_IMAGE_REQUEST);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
            {
                imageFilePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);

                objectImageView.setImageBitmap(imageToStore);
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void confirmChanges2(View view){
        String nm = name.getText().toString();
        String desc = description.getText().toString();
        String pr = price.getText().toString();
        String newString = pr.replace(" euro", "");
        if(TextUtils.isEmpty(nm) || TextUtils.isEmpty(nm) || TextUtils.isEmpty(pr) || TextUtils.isEmpty(desc)){
            Toast.makeText(this, "No empty fields!", Toast.LENGTH_SHORT).show();
        }
        if(!newString.matches("\\d+(?:\\.\\d+)?")){
            Toast.makeText(ItemAdminActivity.this, "Price is not a number. Please insert a number!", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                Boolean result = DB.updateDataItem(menuItem.getID().toString(), nm, desc, newString);
                if(result == true){
                    Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show();
                }


                if (objectImageView.getDrawable() != null && imageToStore != null) {
                    DB.setImageItem(imageToStore, ItemID);
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No new image upload", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}