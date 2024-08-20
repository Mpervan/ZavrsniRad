package com.example.prehrambenica1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewItemActivity extends AppCompatActivity {

    private MenuItem menuItem;
    private User currentUser;
    private Restaurant restaurant;
    private Bundle extras;
    private String ID, ItemID;
    private Button viewOrder, home, add, cancel;
    private DBHelper DB;
    private LocalDBHelper LDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);


        viewOrder = (Button) findViewById(R.id.viewOrder);
        home = (Button) findViewById(R.id.home);
        add = (Button) findViewById(R.id.add_item);
        cancel = (Button) findViewById(R.id.cancel);

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);

        extras = getIntent().getExtras();
        if (extras != null) {
            ID = extras.getString("key_ID");
            ItemID = extras.getString("key_ItemID");
        }
        getIntent().removeExtra("key_ID");

        viewOrder.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),OrderActivity.class);
                startActivity(intent);
            }
        }));
        add.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check = LDB.checkItemsAndID(menuItem.getRestaurantID());
                if(check == true) {
                    currentUser = LDB.getCurrentUser();
                    Boolean insert = LDB.insertDataItem(menuItem);
                    if (insert == true) {
                        Toast.makeText(ViewItemActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplication(), OrderActivity.class);
                        intent.putExtra("key_ID", ID);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ViewItemActivity.this, "Error! Cannot add item to order!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ViewItemActivity.this, "Your order is from a different restaurant!", Toast.LENGTH_SHORT).show();
                }
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
                if (isAdmin == true) {
                    Intent intent = new Intent(getApplicationContext(), HomeAdminActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
        menuItem = DB.getMenuItem(ItemID);
        restaurant = DB.getRestaurant(ID);
        String restName = restaurant.getName();

        TextView name = (TextView) findViewById(R.id.itemName);
        TextView description = (TextView) findViewById(R.id.description4);
        TextView price = (TextView) findViewById(R.id.price1);
        TextView restaurant = (TextView) findViewById(R.id.restaurantName);

        name.setText(menuItem.getItem());
        description.setText(menuItem.getDescription());
        price.setText(menuItem.getPrice().toString() + " euro");
        restaurant.setText(restName);
    }
}