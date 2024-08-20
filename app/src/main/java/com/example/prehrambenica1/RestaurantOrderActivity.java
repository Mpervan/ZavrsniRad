package com.example.prehrambenica1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RestaurantOrderActivity extends AppCompatActivity {

    private Button viewOrder, home;
    private ListView ordersList;
    private ListView ordersList2;
    private ArrayList<SingleOrder> orderList;
    private ArrayList<SingleOrder> orderList2;
    private ArrayAdapter adapter;
    private ArrayAdapter adapter2;
    private DBHelper DB;
    private LocalDBHelper LDB;
    private Bundle extras;
    private String ID;
    private User currentUser;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_order);
        viewOrder = findViewById(R.id.viewOrder);
        home = findViewById(R.id.home);
        ordersList = findViewById(R.id.rest_list3);
        orderList = new ArrayList<>();
        ordersList2 = findViewById(R.id.rest_list_progress);
        orderList2 = new ArrayList<>();

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);
        extras = getIntent().getExtras();

        if (extras != null) {
            ID = extras.getString("key_ID");
        }
        getIntent().removeExtra("key_ID");

        currentUser = LDB.getCurrentUser();
        restaurant = DB.getRestaurant(ID);

        viewData(ID);
        ordersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SingleOrder r = orderList.get(i);
                Intent intent = new Intent(getApplicationContext(), ViewOrderAdminActivity.class);
                intent.putExtra("key_order", String.valueOf(r.getID()));
                startActivity(intent);
            }
        });
        viewDataIP(ID);
        ordersList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SingleOrder r = orderList2.get(i);
                Intent intent = new Intent(getApplicationContext(), ConfirmOrderAdminActivity.class);
                intent.putExtra("key_order", String.valueOf(r.getID()));
                startActivity(intent);
            }
        });

        viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ViewYourOrdersActivity.class);
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
        TextView title = (TextView) findViewById(R.id.rest_name);
        ImageView image  = (ImageView) findViewById(R.id.imageView4) ;

        title.setText(restaurant.getName() + " Orders:");
        image.setImageBitmap(restaurant.getImage());
    }

    private void viewData(String id) {

        DateTimeFormatter formatter = null;
        LocalDateTime dateTime = null;
        int number = 1000;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        }

        Cursor cursor = DB.viewOrderData(id);
        SingleOrder item = null;
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No new orders", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    dateTime = LocalDateTime.parse(cursor.getString(8), formatter);
                }
                int num = cursor.getInt(1);
                if(number != num)
                {
                    number = num;
                    item = new SingleOrder();
                    item.setID(cursor.getInt(0));
                    item.setTime(dateTime);
                    item.setRestaurantID(cursor.getInt(6));
                    item.setStatus(cursor.getString(9));
                    item.setPrice(Double.parseDouble(cursor.getString(7)));
                    item.setUser(cursor.getString(5));
                    item.setCallNumber(cursor.getString(3));
                    item.setAddress(cursor.getString(2));
                    item.setNumber(cursor.getInt(1));
                    item.setItemID(cursor.getInt(4));
                    orderList.add(item);
                }
            }
            adapter = new OrderListAdapter(RestaurantOrderActivity.this, orderList);
            ordersList.setAdapter(adapter);
        }
    }
    private void viewDataIP(String id) {

        DateTimeFormatter formatter = null;
        LocalDateTime dateTime = null;
        int number = 1000;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        }

        Cursor cursor = DB.viewOrderDataInProgress(id);
        SingleOrder item = null;
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No orders in progress", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    dateTime = LocalDateTime.parse(cursor.getString(8), formatter);
                }
                int num = cursor.getInt(1);
                if(number != num)
                {
                    number = num;
                    item = new SingleOrder();
                    item.setID(cursor.getInt(0));
                    item.setTime(dateTime);
                    item.setRestaurantID(cursor.getInt(6));
                    item.setStatus(cursor.getString(9));
                    item.setPrice(Double.parseDouble(cursor.getString(7)));
                    item.setUser(cursor.getString(5));
                    item.setCallNumber(cursor.getString(3));
                    item.setAddress(cursor.getString(2));
                    item.setNumber(cursor.getInt(1));
                    item.setItemID(cursor.getInt(4));
                    orderList2.add(item);
                }
            }
            adapter2 = new OrderListAdapter(RestaurantOrderActivity.this, orderList2);
            ordersList2.setAdapter(adapter2);
        }
    }
}