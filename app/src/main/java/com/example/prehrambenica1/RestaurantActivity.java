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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity {
    private Restaurant restaurant;
    private Bundle extras;
    private String ID, username;
    private Button viewOrder, home;
    private ListView itemsList;
    private ArrayList<MenuItem> itemList;
    private SearchView searchView;
    private ArrayAdapter adapter;
    private DBHelper DB;
    private LocalDBHelper LDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        itemsList = findViewById(R.id.item_list);
        itemList = new ArrayList<>();

        viewOrder = (Button) findViewById(R.id.viewOrder);
        home = (Button) findViewById(R.id.home);
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

        DB = new DBHelper(this);
        LDB = new LocalDBHelper(this);

        extras = getIntent().getExtras();
        if (extras != null) {
            ID = extras.getString("key_ID");
            username = extras.getString("key_username");
        }
        getIntent().removeExtra("key_ID");

        viewData(ID);
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MenuItem item = itemList.get(i);
                Intent intent = new Intent(getApplicationContext(), ViewItemActivity.class);
                intent.putExtra("key_ItemID", String.valueOf(item.getID()));
                intent.putExtra("key_ID", ID);
                startActivity(intent);
            }
        });

        viewOrder.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewYourOrdersActivity.class);
                startActivity(intent);
            }
        }));
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
        restaurant = DB.getRestaurant(ID);
        TextView name = (TextView) findViewById(R.id.name1);
        TextView address = (TextView) findViewById(R.id.address1);
        TextView phone = (TextView) findViewById(R.id.phone1);
        TextView description = (TextView) findViewById(R.id.description1);
        TextView open = (TextView) findViewById(R.id.open1);
        ImageView image  = (ImageView) findViewById(R.id.imageView4) ;

        name.setText(restaurant.getName());
        address.setText(restaurant.getAddress());
        phone.setText(restaurant.getPhone());
        open.setText(restaurant.getOpen());
        description.setText(restaurant.getDescription());
        image.setImageBitmap(restaurant.getImage());

    }
    private void filterList(String text) {
        ArrayList<MenuItem> filteredList = new ArrayList<>();
        for (MenuItem item : itemList){
            if(item.getItem().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter = new ItemListAdapter(RestaurantActivity.this, filteredList);
            itemsList.setAdapter(adapter);
        }
    }

    private void viewData(String id) {
        Cursor cursor = DB.viewItemData(id);
        MenuItem item = null;
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                item = new MenuItem();
                item.setID(cursor.getInt(0));
                item.setItem(cursor.getString(1));
                item.setRestaurantID(cursor.getInt(2));
                item.setPrice(Double.parseDouble(cursor.getString(3)));
                item.setDescription(cursor.getString(4));
                byte [] imageBytes = cursor.getBlob(5);
                Bitmap bitmap;
                if(imageBytes != null){
                    bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                }
                else{
                    bitmap = BitmapFactory.decodeResource(RestaurantActivity.this.getResources(), R.drawable.menu_item);
                }
                item.setImage(bitmap);
                itemList.add(item);
            }
            adapter = new ItemListAdapter(RestaurantActivity.this, itemList);
            itemsList.setAdapter(adapter);
        }
    }
}