package com.example.prehrambenica1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

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

public class HomeActivity extends AppCompatActivity {
    private Button viewOrder, profile;
    private DBHelper DB;
    private LocalDBHelper LDB;
    private User currentUser;
    private ListView restList;
    private ArrayList<Restaurant> restaList;
    private SearchView searchView;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewOrder = findViewById(R.id.viewOrder);
        profile = findViewById(R.id.profile);
        searchView = findViewById(R.id.search_bar);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        restList = findViewById(R.id.rest_list);
        restaList = new ArrayList<>();

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);

        currentUser = LDB.getCurrentUser();

        viewData();
        restList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Restaurant r = restaList.get(i);
                Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
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
        TextView title = (TextView) findViewById(R.id.wellcome);
        title.setText("Welcome " + currentUser.getUsername());
    }

    private void filterList(String text) {
        ArrayList<Restaurant> filteredList = new ArrayList<>();
        for (Restaurant rest : restaList){
            if(rest.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(rest);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter = new ListAdapter(HomeActivity.this, filteredList);
            restList.setAdapter(adapter);
        }
    }

    private void viewData(){
        Cursor cursor = DB.viewData();
        Restaurant restaurant = null;
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
                    bitmap = BitmapFactory.decodeResource(HomeActivity.this.getResources(), R.drawable.restaurant_icon);
                }
                restaurant.setImage(bitmap);
                restaList.add(restaurant);
            }
            adapter = new ListAdapter(HomeActivity.this, restaList);
            restList.setAdapter(adapter);
        }
    }
}