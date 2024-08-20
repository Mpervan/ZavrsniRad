package com.example.prehrambenica1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {

    private User user;
    private Button cancel, admin, home, viewOrder;
    private EditText name, email, address, phone;
    private DBHelper DB;
    private LocalDBHelper LDB;
    private Uri imageFilePath;
    private ImageView objectImageView;
    private Bitmap imageToStore;
    private static final int PICK_IMAGE_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);
        admin = (Button) findViewById(R.id.admin);
        cancel = (Button) findViewById(R.id.cancel);
        home = (Button) findViewById(R.id.home);
        viewOrder = (Button) findViewById(R.id.viewOrder);

        try{
            objectImageView = findViewById(R.id.edit_image);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        cancel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
            }
        }));
        admin.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),BecomeAdminActivity.class);
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
        name = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.email1);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);


        username.setText(user.getUsername());
        name.setText(user.getName());
        email.setText(user.getEmail());
        if(user.getAddress() != (String) null) {
            address.setText(user.getAddress());
        }
        if(user.getPhone() != (String) null){
            phone.setText(user.getPhone());
        }
        objectImageView.setImageBitmap(user.getImage());
    }
    public void chooseImage(View objectView){
        try{
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*");

            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent, PICK_IMAGE_REQUEST);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
            {
                imageFilePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);

                objectImageView.setImageBitmap(imageToStore);
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void confirmChanges(View view){
        String mail = email.getText().toString();
        String nm = name.getText().toString();
        String adres = address.getText().toString();
        String phon = phone.getText().toString();
        if(TextUtils.isEmpty(nm) || TextUtils.isEmpty(mail)){
            Toast.makeText(this, "No empty fields!", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                    Boolean result = DB.updateDataUser(user.getUsername(), nm, mail, adres, phon);
                    if(result == true){
                        User newUser = DB.getCurrentUser(user.getUsername());
                        LDB.deleteCurrentUser();
                        LDB.setCurrentUser(newUser);
                        Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show();
                    }
                    if (objectImageView.getDrawable() != null && imageToStore != null) {
                    DB.setImageUser(imageToStore, user.getUsername());
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No new image upload", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}