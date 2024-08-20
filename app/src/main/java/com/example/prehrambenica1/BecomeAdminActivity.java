package com.example.prehrambenica1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BecomeAdminActivity extends AppCompatActivity {
    private Button home, order, confirm, cancel;
    private DBHelper DB;
    private LocalDBHelper LDB;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_admin);

        order = findViewById(R.id.viewOrder);
        home = findViewById(R.id.home);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);

        currentUser = LDB.getCurrentUser();

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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewYourOrdersActivity.class);
                startActivity(intent);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.makeAdmin(currentUser.getUsername());
                User newUser = DB.getCurrentUser(currentUser.getUsername());
                LDB.deleteCurrentUser();
                LDB.setCurrentUser(newUser);
                Intent intent = new Intent(getApplicationContext(), HomeAdminActivity.class);
                startActivity(intent);
            }
        });
    }
}