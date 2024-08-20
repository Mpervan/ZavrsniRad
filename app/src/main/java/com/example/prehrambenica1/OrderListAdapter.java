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

public class OrderListAdapter extends ArrayAdapter<SingleOrder>{


    public OrderListAdapter(@NonNull Context context, ArrayList<SingleOrder> dataArrayList) {
        super(context, R.layout.order_layout, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        SingleOrder item = getItem(position);
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.order_layout, parent, false);
        }
        //ImageView listImage = view.findViewById(R.id.listImage)
        TextView name = view.findViewById(R.id.user);
        TextView price = view.findViewById(R.id.price);
        TextView address = view.findViewById(R.id.address);
        TextView number = view.findViewById(R.id.number);
        String num;
        String numRead = String.valueOf(item.getNumber());
        if(item.getNumber() <= 9 ){
            num = "00" + numRead;
            number.setText(num);
        }
        if(item.getNumber() > 9 && item.getNumber() < 100){
            num = "0" + numRead;
            number.setText(num);
        }
        if(item.getNumber() > 99){
           num = numRead;
            number.setText(num);
        }

        name.setText(item.getUser());
        price.setText(item.getPrice().toString() + " euro");
        address.setText(item.getAddress());

        return view;
    }
}
