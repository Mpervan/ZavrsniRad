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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ConfirmOrderAdminActivity extends AppCompatActivity {

    private OrderWhole orderWhole;
    SingleOrder order;
    private ListView itemsList;
    private ArrayList<MenuItem> itemList;
    private ArrayAdapter adapter;
    private Restaurant restaurant;
    private Bundle extras;
    private String time;
    private String orderID;
    private Button viewOrder, home, finish;
    private DBHelper DB;
    private LocalDBHelper LDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order_admin);

        itemsList = findViewById(R.id.item_list);
        itemList = new ArrayList<>();

        viewOrder = (Button) findViewById(R.id.order);
        home = (Button) findViewById(R.id.home);
        finish = (Button) findViewById(R.id.finish);

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
        finish.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.orderStatusChange(order.getNumber(), time, "Completed");
                Intent intent = new Intent(getApplicationContext(), RestaurantOrderActivity.class);
                intent.putExtra("key_ID", String.valueOf(order.getRestaurantID()));
                startActivity(intent);
            }
        }));



        restaurant = DB.getRestaurant(String.valueOf(order.getRestaurantID()));

        TextView number = (TextView) findViewById(R.id.orderNumber);
        TextView price = (TextView) findViewById(R.id.price2);
        TextView time1 = (TextView) findViewById(R.id.time);
        TextView address = (TextView) findViewById(R.id.address3);
        TextView phone = (TextView) findViewById(R.id.callNumber);
        TextView user = (TextView) findViewById(R.id.username);
        String num;
        String numRead = String.valueOf(order.getNumber());
        if(order.getNumber() <= 9 ){
            num = "00" + numRead;
            number.setText(num);
        }
        if(order.getNumber() >= 9 && order.getNumber() < 100){
            num = "0" + numRead;
            number.setText(num);
        }
        if(order.getNumber() > 99){
            num = numRead;
            number.setText(num);
        }

        price.setText(order.getPrice().toString());
        time1.setText(time);
        address.setText(order.getAddress());
        phone.setText(order.getCallNumber());
        user.setText(order.getUser());
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
                    bitmap = BitmapFactory.decodeResource(ConfirmOrderAdminActivity.this.getResources(), R.drawable.menu_item);
                }
                item.setImage(bitmap);
                itemList.add(item);
            }
        }
        adapter = new ItemListAdapter(ConfirmOrderAdminActivity.this, itemList);
        itemsList.setAdapter(adapter);
    }
}