package com.example.prehrambenica1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ViewOrderActivity extends AppCompatActivity {

    private User currentUser;
    private SingleOrder order;
    private Restaurant restaurant;
    private Bundle extras;
    private String time;
    private String orderID;
    private Button viewOrder, home;
    private DBHelper DB;
    private LocalDBHelper LDB;
    private ListView itemsList;
    private ArrayList<MenuItem> itemList;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);


        viewOrder = (Button) findViewById(R.id.order);
        home = (Button) findViewById(R.id.home);

        itemsList = findViewById(R.id.item_list);
        itemList = new ArrayList<>();

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);

        extras = getIntent().getExtras();
        if (extras != null) {
            orderID = extras.getString("key_order");
        }
        getIntent().removeExtra("key_ID");

        order = DB.getOrder(orderID);
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time = order.getTime().format(formatter);
        }

        viewData(order.getNumber(), time);
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MenuItem item = itemList.get(i);
                Intent intent = new Intent(getApplicationContext(), ViewItemActivity.class);
                intent.putExtra("key_ItemID", String.valueOf(item.getID()));
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
        restaurant = DB.getRestaurant(String.valueOf(order.getRestaurantID()));

        TextView number = (TextView) findViewById(R.id.orderNumber);
        TextView price = (TextView) findViewById(R.id.price2);
        TextView time1 = (TextView) findViewById(R.id.time);
        TextView address = (TextView) findViewById(R.id.address2);
        TextView rest = (TextView) findViewById(R.id.restaurantName);
        String num;
        String numRead = String.valueOf(order.getNumber());
        if(order.getNumber() <= 9 ){
            num = "Order number: 00" + numRead;
            number.setText(num);
        }
        if(order.getNumber() >= 9 && order.getNumber() < 100){
            num = "Order number: 0" + numRead;
            number.setText(num);
        }
        if(order.getNumber() > 99){
            num = "Order number: " + numRead;
            number.setText(num);
        }

        price.setText(order.getPrice().toString() + " euro");
        time1.setText(time);
        address.setText(order.getAddress());
        rest.setText(restaurant.getName());
    }

    private void viewData(int number, String time) {
        ArrayList<Integer> intList = DB.getOrderItemsIDs(number, time);
        for (int i = 0; i < intList.size(); i++) {
            MenuItem item = null;
            Cursor cursor = DB.viewItemDataByID(intList.get(i).toString());
            if (cursor.moveToFirst()) {
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
                    bitmap = BitmapFactory.decodeResource(ViewOrderActivity.this.getResources(), R.drawable.menu_item);
                }
                item.setImage(bitmap);
                itemList.add(item);
            }
        }
        adapter = new ItemListAdapter(ViewOrderActivity.this, itemList);
        itemsList.setAdapter(adapter);
    }
}