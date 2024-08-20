package com.example.prehrambenica1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateRestActivity extends AppCompatActivity {
    private EditText name2, address, phone, description, open;
    private Button confirm, cancel;

    public String username;
    private DBHelper DB;
    private LocalDBHelper LDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rest);

        name2 = findViewById(R.id.name2);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        open = findViewById(R.id.open);
        description = findViewById(R.id.description);
        confirm = findViewById(R.id.rregister);
        cancel = findViewById(R.id.cancel);

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);

        username = LDB.getCurrentUser().getUsername();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name2.getText().toString();
                String adres = address.getText().toString();
                String num = phone.getText().toString();
                String desc = description.getText().toString();
                String open1 = open.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(adres) || TextUtils.isEmpty(num) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(open1))
                    Toast.makeText(CreateRestActivity.this, "All fields Required", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkName = DB.checkRestaurantName(name);
                    if (checkName == false) {
                        int ID = DB.generateID();
                        Boolean insert = DB.insertDataRest(ID, name, adres, num, desc, open1, username);
                        if (insert == true) {
                            Toast.makeText(CreateRestActivity.this, "Restaurant Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplication(), EditRestaurantsActivity.class);
                            intent.putExtra("key_ID", ID);
                            startActivity(intent);
                        } else {
                            Toast.makeText(CreateRestActivity.this, "Restaurant Registration Failed!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CreateRestActivity.this, "Name already Exists", Toast.LENGTH_SHORT).show();
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