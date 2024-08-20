package com.example.prehrambenica1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ConfirmOrderActivity extends AppCompatActivity {
    private EditText address, phone;
    private TextView price;
    private Button confirm, cancel;
    private DBHelper DB;
    private LocalDBHelper LDB;
    private User currentUser;
    OrderWhole orderWhole;
    private Double priceDouble = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        address = (EditText) findViewById(R.id.delivery_address);
        phone = (EditText) findViewById(R.id.phone_number);
        price = (TextView) findViewById(R.id.price2);

        confirm = (Button) findViewById(R.id.confirm2);
        cancel = (Button) findViewById(R.id.cancel2);

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);
        currentUser = LDB.getCurrentUser();
        if(currentUser.getAddress() != null)
        {
            address.setText(currentUser.getAddress());
        }
        if(currentUser.getPhone() != null)
        {
            phone.setText(currentUser.getPhone());
        }

        orderWhole = viewData();
        price.setText(orderWhole.getPrice().toString() + " euro");
        confirm.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adres = address.getText().toString();
                String phon = phone.getText().toString();
                DateTimeFormatter formatter = null;
                String formattedDateTime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    formattedDateTime = orderWhole.getTime().format(formatter);
                }
                Boolean result = false;
                for (int i = 0; i < orderWhole.getItems().size(); i++) {
                    SingleOrder order = orderWhole.convert();
                    order.setItemID(orderWhole.getItems().get(i).getID());
                    order.setAddress(adres);
                    order.setCallNumber(phon);
                    result = DB.insertDataOrder(order, formattedDateTime);
                }
                if(result == true)
                {
                    Toast.makeText(ConfirmOrderActivity.this, "Your order request was sent successfully!", Toast.LENGTH_SHORT).show();
                    LDB.deleteItems();
                    Intent intent = new Intent(getApplicationContext(),ViewYourOrdersActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ConfirmOrderActivity.this, "An error occurred! Try later.", Toast.LENGTH_SHORT).show();
                }

            }
        }));

    }

    private OrderWhole viewData() {
        Cursor cursor = LDB.viewItemData();
        MenuItem item = null;
        int restID = 0;
        LocalDateTime dateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.now();
        }
        orderWhole = new OrderWhole();
        ArrayList<MenuItem> list = new ArrayList<>();
        Integer num = DB.generateOrderNumber();

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
                list.add(item);
                restID = item.getRestaurantID();
                priceDouble = priceDouble + item.getPrice();
            }
            orderWhole.setNumber(num);
            orderWhole.setItems(list);
            orderWhole.setUser(currentUser.getUsername());
            orderWhole.setRestaurantID(restID);
            orderWhole.setPrice(priceDouble);
            orderWhole.setTime(dateTime);
        }
        return orderWhole;
    }
}