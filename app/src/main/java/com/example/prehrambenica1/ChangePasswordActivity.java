package com.example.prehrambenica1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText password1, password2;
    private Button confirm, cancel;
    private DBHelper DB;
    private LocalDBHelper LDB;
    private User currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);
        cancel = (Button) findViewById(R.id.cancel);
        confirm = (Button) findViewById(R.id.confirm);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);

        currentuser = LDB.getCurrentUser();

        cancel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
            }
        }));
        confirm.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass1 = password1.getText().toString();
                String pass2 = password2.getText().toString();
                if(TextUtils.isEmpty(pass1) || TextUtils.isEmpty(pass2)){
                    Toast.makeText(ChangePasswordActivity.this, "No empty fields!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(pass1.equals(pass2)){
                        Boolean result = DB.changePass(currentuser.getUsername(), pass1);
                        if(result == true){
                            Toast.makeText(ChangePasswordActivity.this, "Password change successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(ChangePasswordActivity.this, "Error in the program", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }));
    }
}