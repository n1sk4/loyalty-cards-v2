package com.example.diplomski;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
                if(store_input.getText().toString().length() <= 0){
                    store_input.setError("This field cannot be empty!");
                    Toast.makeText(AddActivity.this, "Store name field cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    myDB.addStore(store_input.getText().toString().trim());
                    store_input.setError(null);
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent);
                }
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