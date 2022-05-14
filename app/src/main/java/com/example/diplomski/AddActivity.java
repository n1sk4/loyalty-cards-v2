package com.example.diplomski;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class AddActivity extends AppCompatActivity {

    EditText store_input;
    Button add_button, add_logo;
    ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        store_input = findViewById(R.id.name_input);
        add_button = findViewById(R.id.add_button);
        add_logo = findViewById(R.id.add_logo);
        logoImageView = (ImageView) findViewById(R.id.logo_imageView);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreNamesDB myDB = new StoreNamesDB(AddActivity.this);
                myDB.addStore(store_input.getText().toString().trim());
            }
        });

        add_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_new_item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}