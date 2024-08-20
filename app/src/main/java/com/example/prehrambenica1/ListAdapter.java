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

public class ListAdapter extends ArrayAdapter<Restaurant>{

    public ListAdapter(@NonNull Context context, ArrayList<Restaurant> dataArrayList) {
        super(context, R.layout.restaurant_layout, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Restaurant restaurant = getItem(position);
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.restaurant_layout, parent, false);
        }

        TextView name = view.findViewById(R.id.name3);
        TextView description = view.findViewById(R.id.description3);
        ImageView listImage = view.findViewById(R.id.imageView);


        name.setText(restaurant.getName());
        description.setText(restaurant.getDescription());
        listImage.setImageBitmap(restaurant.getImage());

        return view;
    }
}
