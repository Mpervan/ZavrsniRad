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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ViewYourOrdersActivity extends AppCompatActivity {
    private Button newOrder, home, order;
    private TextView message;
    private ListView ordersList;
    private ListView ordersList2;
    private ArrayList<SingleOrder> orderList;
    private ArrayList<SingleOrder> orderList2;
    private ArrayAdapter adapter;
    private ArrayAdapter adapter2;
    private DBHelper DB;
    private LocalDBHelper LDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_your_orders);
        newOrder = (Button) findViewById(R.id.newOrder);
        order = (Button) findViewById(R.id.order);
        home = (Button) findViewById(R.id.home);
        message = (TextView) findViewById(R.id.message);

        ordersList = findViewById(R.id.active_list);
        ordersList2 = findViewById(R.id.active_list2);

        orderList = new ArrayList<>();
        orderList2 = new ArrayList<>();

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);

        User currentUser = LDB.getCurrentUser();

        viewData(currentUser.getUsername());
        ordersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SingleOrder r = orderList.get(i);
                Intent intent = new Intent(getApplicationContext(), ViewOrderActivity.class);
                intent.putExtra("key_order", String.valueOf(r.getID()));
                startActivity(intent);
            }
        });
        viewDataIP(currentUser.getUsername());
        ordersList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SingleOrder r = orderList2.get(i);
                Intent intent = new Intent(getApplicationContext(), ViewOrderActivity.class);
                intent.putExtra("key_order", String.valueOf(r.getID()));
                startActivity(intent);
            }
        });

        newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),OrderActivity.class);
                startActivity(intent);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),OrderActivity.class);
                startActivity(intent);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        message.setText(DB.getMessage(currentUser.getUsername()));
    }

    private void viewData(String username) {

        DateTimeFormatter formatter = null;
        LocalDateTime dateTime = null;
        int number = 1000;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        }

        Cursor cursor = DB.viewOrderDataInProgressUser(username);
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
            adapter = new OrderListAdapter(ViewYourOrdersActivity.this, orderList);
            ordersList.setAdapter(adapter);
        }
    }

    private void viewDataIP(String username) {

        DateTimeFormatter formatter = null;
        LocalDateTime dateTime = null;
        int number = 1000;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        }

        Cursor cursor = DB.viewOrderDataOld(username);
        SingleOrder item = null;
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No orders in progress", Toast.LENGTH_SHORT).show();
        } else {
            for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
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
            adapter2 = new OrderListAdapter(ViewYourOrdersActivity.this, orderList2);
            ordersList2.setAdapter(adapter2);
        }
    }
}