package com.example.diplomski;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList store_id, store_name;

    int position;

    CustomAdapter(Context context,
                  ArrayList store_id,
                  ArrayList store_name){

        this.context = context;
        this.store_id = store_id;
        this.store_name = store_name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.store_name_txt.setText(String.valueOf(store_name.get(position)));

        holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, com.example.diplomski.UpdateActivity.class);
                intent.putExtra("id", String.valueOf(store_id.get(position)));
                intent.putExtra("name", String.valueOf(store_name.get(position)));
                context.startActivity(intent);
                return true;
            }
        });

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.example.diplomski.BarcodeActivity.class);
                intent.putExtra("id", String.valueOf(store_id.get(position)));
                intent.putExtra("name", String.valueOf(store_name.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return store_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView store_id_txt, store_name_txt;
        ImageView store_logo_img;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            store_name_txt = itemView.findViewById(R.id.store_name_txt);
            store_logo_img = itemView.findViewById(R.id.logo_imageView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
