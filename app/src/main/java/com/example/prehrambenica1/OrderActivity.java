package com.example.prehrambenica1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    private User currentUser;
    private Bundle extras;
    private String ID, ItemID;
    private Button confirm, home, add, cancel;
    private ListView itemsList;
    private ArrayList<MenuItem> itemList;
    private ArrayAdapter adapter;
    private DBHelper DB;
    private LocalDBHelper LDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        confirm = (Button) findViewById(R.id.confirm1);
        home = (Button) findViewById(R.id.home);
        add = (Button) findViewById(R.id.add_item);
        cancel = (Button) findViewById(R.id.cancel1);

        itemsList = findViewById(R.id.item_list);
        itemList = new ArrayList<>();

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);

        extras = getIntent().getExtras();
        if (extras != null) {
            ID = extras.getString("key_ID");
            ItemID = extras.getString("key_ItemID");
        }
        getIntent().removeExtra("key_ID");
        getIntent().removeExtra("key_ItemID");

        viewData(ID);
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MenuItem item = itemList.get(i);
                LDB.deleteItem(item.getID());
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser = LDB.getCurrentUser();
                Toast.makeText(OrderActivity.this, "Order Canceled", Toast.LENGTH_SHORT).show();
                LDB.deleteItems();
                Intent intent = new Intent(getApplication(), HomeActivity.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check = LDB.checkItems();
                Intent intent;
                if(check == false){
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                }
                else{
                    intent = new Intent(getApplication(), RestaurantActivity.class);
                    intent.putExtra("key_ID", ID);
                }
                startActivity(intent);

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check = LDB.checkItems();
                if(check == false)
                {
                    Toast.makeText(OrderActivity.this, "Nothing To Confirm!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getApplication(), ConfirmOrderActivity.class);
                    startActivity(intent);
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser = LDB.getCurrentUser();
                Boolean isAdmin = DB.checkAdmin(currentUser.getUsername());
                Intent intent;
                if (isAdmin == true) {
                    intent = new Intent(getApplicationContext(), HomeAdminActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                }
                startActivity(intent);
            }
        });
    }
    private void viewData(String id) {
        Cursor cursor = LDB.viewItemData();
        MenuItem item = null;
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                item = new MenuItem();
                item.setID(cursor.getInt(1));
                item.setItem(cursor.getString(2));
                item.setRestaurantID(cursor.getInt(3));
                item.setPrice(Double.parseDouble(cursor.getString(4)));
                item.setDescription(cursor.getString(5));
                byte [] imageBytes = cursor.getBlob(6);
                Bitmap bitmap;
                if(imageBytes != null){
                    bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                }
                else{
                    bitmap = BitmapFactory.decodeResource(OrderActivity.this.getResources(), R.drawable.menu_item);
                }
                item.setImage(bitmap);
                itemList.add(item);
            }
            adapter = new ItemListAdapter(OrderActivity.this, itemList);
            itemsList.setAdapter(adapter);
        }
    }

}