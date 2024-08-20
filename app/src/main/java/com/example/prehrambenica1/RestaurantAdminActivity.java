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

public class RestaurantAdminActivity extends AppCompatActivity {
    private Restaurant restaurant;
    private Bundle extras;
    private String ID;
    private Uri imageFilePath;
    private Button viewOrder, home, addItem;
    private ImageView objectImageView;
    private ListView itemsList;
    private ArrayList<MenuItem> itemList;
    private ArrayAdapter adapter;
    private DBHelper DB;
    private LocalDBHelper LDB;
    private Bitmap imageToStore;
    private EditText name;
    private EditText address;
    private EditText phone;
    private EditText description;
    private EditText open;
    private static final int PICK_IMAGE_REQUEST = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_admin);

        viewOrder = (Button) findViewById(R.id.viewOrder);
        home = (Button) findViewById(R.id.home);
        addItem = (Button) findViewById(R.id.add_item);

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);

        try{
            objectImageView = findViewById(R.id.add_image_rest);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        itemsList = findViewById(R.id.item_list);
        itemList = new ArrayList<>();

        extras = getIntent().getExtras();
        if (extras != null) {
            ID = extras.getString("key_ID");
        }
        getIntent().removeExtra("key_ID");

        viewData(ID);
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MenuItem item = itemList.get(i);
                Intent intent = new Intent(getApplicationContext(), ItemAdminActivity.class);
                intent.putExtra("key_ItemID", String.valueOf(item.getID()));
                intent.putExtra("key_ID", ID);
                startActivity(intent);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddItemActivity.class);
                intent.putExtra("key_ID", ID);
                startActivity(intent);
            }
        });


        viewOrder.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ViewYourOrdersActivity.class);
                startActivity(intent);
            }
        }));
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIntent().removeExtra("key_ID");
                User user = LDB.getCurrentUser();
                Boolean isAdmin = DB.checkAdmin(user.getUsername());
                Intent intent;
                if(isAdmin){
                    intent = new Intent(getApplicationContext(), HomeAdminActivity.class);
                }
                else{
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                }
                startActivity(intent);
            }
        });
        restaurant = DB.getRestaurant(ID);
        name = (EditText) findViewById(R.id.name1);
        address = (EditText) findViewById(R.id.address1);
        phone = (EditText) findViewById(R.id.phone1);
        description = (EditText) findViewById(R.id.description1);
        open = (EditText) findViewById(R.id.open1);
        //Bitmap bitmap = restaurant.getImage();

        name.setText(restaurant.getName());
        address.setText(restaurant.getAddress());
        phone.setText(restaurant.getPhone());
        open.setText(restaurant.getOpen());
        description.setText(restaurant.getDescription());
        /*Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        if (bitmap.sameAs(emptyBitmap)) {
            Bitmap newBitmap = BitmapFactory.decodeResource(RestaurantAdminActivity.this.getResources(), R.drawable.blur_rest2);
            restaurant.setImage(newBitmap);
        }*/
        objectImageView.setImageBitmap(restaurant.getImage());
    }
    private void viewData(String id) {
        Cursor cursor = DB.viewItemData(id);
        MenuItem item = null;
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                item = new MenuItem();
                item.setID(cursor.getInt(0));
                item.setItem(cursor.getString(1));
                item.setRestaurantID(cursor.getInt(2));
                item.setPrice(Double.parseDouble(cursor.getString(3)));
                item.setDescription(cursor.getString(4));
                byte [] imageBytes = cursor.getBlob(5);
                Bitmap bitmap;
                if(imageBytes != null){
                    bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                }
                else{
                    bitmap = BitmapFactory.decodeResource(RestaurantAdminActivity.this.getResources(), R.drawable.menu_item);
                }
                item.setImage(bitmap);
                itemList.add(item);
            }
            adapter = new ItemListAdapter(RestaurantAdminActivity.this, itemList);
            itemsList.setAdapter(adapter);
        }
    }
    public void chooseImage(View objectView){
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

    public void confirmChanges(View view){
        String nm = name.getText().toString();
        String adrs = address.getText().toString();
        String phn = phone.getText().toString();
        String desc = description.getText().toString();
        String opn = open.getText().toString();
        if(TextUtils.isEmpty(nm) || TextUtils.isEmpty(adrs) || TextUtils.isEmpty(phn) || TextUtils.isEmpty(desc)){
            Toast.makeText(this, "No empty fields!", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                Boolean checkuser = DB.checkRestaurantName(nm);
                if (checkuser == false || restaurant.getName().equals(nm)) {
                    Boolean result = DB.updateDataRest(restaurant.getID().toString(), nm, adrs, phn, desc, opn);
                    if(result == true){
                        Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "Restaurant name already in use", Toast.LENGTH_SHORT).show();
                }
                if (objectImageView.getDrawable() != null && imageToStore != null) {
                    DB.setImageRestaurant(imageToStore, ID);
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