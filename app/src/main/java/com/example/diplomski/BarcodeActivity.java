package com.example.diplomski;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BarcodeActivity extends AppCompatActivity {

    String id, name, website, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        getIntentData();

        ActionBar ab = getSupportActionBar();
        if(name != null){
            ab.setTitle(name + " barcode");
        }
    }

    void getIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name")){
            //Getting Data From Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
}