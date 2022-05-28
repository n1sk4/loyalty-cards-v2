package com.example.diplomski;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<String> storeID, storeName, storeBarcode;
    private final ArrayList<Bitmap> storeLogo;

    CustomAdapter(Context context,
                  ArrayList<String> store_id,
                  ArrayList<String> store_name,
                  ArrayList<String> store_barcode,
                  ArrayList<Bitmap> store_logo){

        this.context = context;
        this.storeID = store_id;
        this.storeName = store_name;
        this.storeBarcode = store_barcode;
        this.storeLogo = store_logo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.storeName_textView.setText(String.valueOf(storeName.get(position)));
        if(storeLogo.get(position) != null){
            holder.storeLogo_ImageView.setImageBitmap(storeLogo.get(position));
            holder.storeLogo_ImageView.setImageTintList(null);
            Palette.from(storeLogo.get(position)).generate(palette -> {
                assert palette != null;
                holder.individualLayout.setBackgroundColor(palette.getDominantColor(ContextCompat.
                        getColor(context, R.color.white)));
            });
        }

        /*
        holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //TODO Open menu /edit/delete/move/
                return true;
            }
        });
        */
        holder.mainLayout.setOnClickListener(v -> startBarcodeActivity(position));
    }

    @Override
    public int getItemCount() {
        return storeID.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView storeName_textView;
        ImageView storeLogo_ImageView;
        LinearLayout mainLayout;
        ConstraintLayout individualLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            storeName_textView = itemView.findViewById(R.id.store_name_txt);
            storeLogo_ImageView = itemView.findViewById(R.id.logo_imageView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            individualLayout = itemView.findViewById(R.id.individualLayout);
        }
    }

    private void startBarcodeActivity(int position){
        Intent intent = new Intent(context, ShowBarcodeActivity.class);
        intent.putExtra("id", String.valueOf(storeID.get(position)));
        intent.putExtra("name", String.valueOf(storeName.get(position)));
        intent.putExtra("barcode", String.valueOf(storeBarcode.get(position)));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
