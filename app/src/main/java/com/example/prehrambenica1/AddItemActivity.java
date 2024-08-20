package com.example.prehrambenica1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity {
    private Bundle extras;
    private EditText name1, price1, description;
    private Button confirm, cancel;

    public String username, restID;
    private DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        name1 = findViewById(R.id.name4);
        price1 = findViewById(R.id.price);
        description = findViewById(R.id.description1);
        confirm = findViewById(R.id.rregister);
        cancel = findViewById(R.id.cancel);

        DB = new DBHelper(this);

        extras = getIntent().getExtras();
        if (extras != null) {
            restID = extras.getString("key_ID");
            username = extras.getString("key_username");
        }


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name1.getText().toString();
                String price = price1.getText().toString();
                String desc = description.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(desc)) {
                    Toast.makeText(AddItemActivity.this, "All fields Required", Toast.LENGTH_SHORT).show();
                }
                if(!price.matches("\\d+(?:\\.\\d+)?")){
                    Toast.makeText(AddItemActivity.this, "Price is not a number. Please insert a number!", Toast.LENGTH_SHORT).show();
                }
                else {
                    int ID = DB.generateItemID();
                        Boolean insert = DB.insertDataItem(ID,name, Integer.parseInt(restID),price, desc);
                        if (insert == true) {
                            Toast.makeText(AddItemActivity.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplication(), RestaurantAdminActivity.class);
                            intent.putExtra("key_ID", String.valueOf(restID));
                            intent.putExtra("key_ItemID", ID);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddItemActivity.this, "Item Registration Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeAdminActivity.class);
                startActivity(intent);
            }
        });
    }
}