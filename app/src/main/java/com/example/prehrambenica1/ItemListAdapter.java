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

public class ItemListAdapter extends ArrayAdapter<MenuItem>{

    public ItemListAdapter(@NonNull Context context, ArrayList<MenuItem> dataArrayList) {
        super(context, R.layout.item_layout, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        MenuItem item = getItem(position);
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false);
        }
        TextView name = view.findViewById(R.id.name3);
        TextView price = view.findViewById(R.id.price);
        ImageView listImage = view.findViewById(R.id.imageView);

        name.setText(item.getItem());
        price.setText(item.getPrice().toString() + " euro");
        listImage.setImageBitmap(item.getImage());

        return view;
    }
}
