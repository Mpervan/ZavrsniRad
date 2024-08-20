package com.example.prehrambenica1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private User user;
    private Button logout, home, viewOrder, edit, changePass;
    private ImageView objectImageView;
    private DBHelper DB;
    private LocalDBHelper LDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);
        logout = (Button) findViewById(R.id.logout);
        home = (Button) findViewById(R.id.home);
        viewOrder = (Button) findViewById(R.id.viewOrder);
        edit = (Button) findViewById(R.id.edit);
        changePass = (Button) findViewById(R.id.change_password);

        try{
            objectImageView = findViewById(R.id.edit_image);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        logout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                LDB.deleteCurrentUser();
                startActivity(intent);
            }
        }));
        viewOrder.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ViewYourOrdersActivity.class);
                startActivity(intent);
            }
        }));
        edit.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
                startActivity(intent);
            }
        }));
        changePass.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ChangePasswordActivity.class);
                startActivity(intent);
            }
        }));
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = LDB.getCurrentUser();
                Boolean isAdmin = DB.checkAdmin(user.getUsername());
                Intent intent;
                if(isAdmin == true){
                    intent = new Intent(getApplicationContext(), HomeAdminActivity.class);
                }
                else{
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                }
                startActivity(intent);
            }
        });
        user = LDB.getCurrentUser();
        TextView username = (TextView) findViewById(R.id.username1);
        TextView fullname = (TextView) findViewById(R.id.fullname);
        TextView email = (TextView) findViewById(R.id.email1);
        TextView role = (TextView) findViewById(R.id.role1);
        TextView address = (TextView) findViewById(R.id.address);
        TextView phone = (TextView) findViewById(R.id.phone);

        username.setText(user.getUsername());
        fullname.setText(user.getName());
        email.setText(user.getEmail());
        role.setText(user.getRole());
        if(user.getAddress() != (String) null){
            address.setText(user.getAddress());
        }
        else{
            address.setText("Home Address");
        }
        if(user.getPhone() != (String) null){
            phone.setText(user.getPhone());
        }
        else{
            phone.setText("Phone Number");
        }
        objectImageView.setImageBitmap(user.getImage());
    }
}