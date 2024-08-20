package com.example.prehrambenica1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HomeOrderAdapter extends ArrayAdapter<OrdersInRestClass> {
    public HomeOrderAdapter(@NonNull Context context, ArrayList<OrdersInRestClass> dataArrayList) {
        super(context, R.layout.restaurant_orders_layout, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        OrdersInRestClass restaurant = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.restaurant_orders_layout, parent, false);
        }

        TextView name = view.findViewById(R.id.name4);
        TextView number = view.findViewById(R.id.number_of_orders);
        ImageView listImage = view.findViewById(R.id.imageView);
        String num;
        String numRead = String.valueOf(restaurant.getNumber());
        if(restaurant.getNumber() == 0 ){
            num = "";
            number.setText(num);
        }
        else{
            num = numRead + " New orders";
            number.setText(num);
        }
        name.setText(restaurant.getName());
        listImage.setImageBitmap(restaurant.getImage());

        return view;
    }
}
