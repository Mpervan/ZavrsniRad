package com.example.prehrambenica1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeAdminActivity extends AppCompatActivity {
    private Button edit, viewOrder, profile;
    private ListView restList;
    private ArrayList<OrdersInRestClass> restaList;
    private ArrayAdapter adapter;
    private DBHelper DB;
    private LocalDBHelper LDB;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        edit = findViewById(R.id.edit);
        viewOrder = findViewById(R.id.viewOrder);
        profile = findViewById(R.id.profile);
        restList = findViewById(R.id.rest_list2);
        restaList = new ArrayList<>();

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);

        currentUser = LDB.getCurrentUser();
        getIntent().removeExtra("key_ID");

        viewData();
        restList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OrdersInRestClass r = restaList.get(i);
                Intent intent = new Intent(getApplicationContext(), RestaurantOrderActivity.class);
                intent.putExtra("key_ID", String.valueOf(r.getID()));
                startActivity(intent);
            }
        });

        viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ViewYourOrdersActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EditRestaurantsActivity.class);
                startActivity(intent);
            }
        });
        TextView title = (TextView) findViewById(R.id.wellcome);
        title.setText("Welcome " + currentUser.getUsername());
    }

    private void viewData(){
        Cursor cursor = DB.viewDataAdmin(currentUser.getUsername());
        ArrayList<Restaurant> extraList = new ArrayList<>();
        Restaurant restaurant = null;
        OrdersInRestClass restOrders = null;
        if(cursor.getCount() == 0){
            Toast.makeText(this,"No data to show", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext())
            {
                restaurant = new Restaurant();
                restaurant.setID(cursor.getInt(0));
                restaurant.setName(cursor.getString(1));
                restaurant.setAddress(cursor.getString(2));
                restaurant.setPhone(cursor.getString(3));
                restaurant.setDescription(cursor.getString(4));
                restaurant.setOpen(cursor.getString(5));
                restaurant.setOwner(cursor.getString(6));
                byte [] imageBytes = cursor.getBlob(7);
                Bitmap bitmap;
                if(imageBytes != null){
                    bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                }
                else{
                    bitmap = BitmapFactory.decodeResource(HomeAdminActivity.this.getResources(), R.drawable.restaurant_icon);
                }
                restaurant.setImage(bitmap);
                extraList.add(restaurant);
            }
            for (int i = 0; i < extraList.size(); i++){
                String id = extraList.get(i).getID().toString();
                restOrders = new OrdersInRestClass();
                restOrders.setID(extraList.get(i).getID());
                restOrders.setName(extraList.get(i).getName());
                restOrders.setNumber(DB.orderQuantity(id));
                restOrders.setImage(extraList.get(i).getImage());
                restaList.add(restOrders);
            }
            adapter = new HomeOrderAdapter(HomeAdminActivity.this, restaList);
            restList.setAdapter(adapter);
        }
    }

}