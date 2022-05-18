package com.example.diplomski;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add_fab;
    RecyclerView recyclerView;
    TextView noData_textView;
    ImageView noData_imageView;

    StoresDB myDB;
    ArrayList<String> store_id, store_name, store_barcode;

    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        myDB = new StoresDB(MainActivity.this);
        store_id = new ArrayList<>();
        store_name = new ArrayList<>();
        store_barcode = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(MainActivity.this, store_id, store_name, store_barcode);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNameActivity.class);
                startActivity(intent);
            }
        });
    }

    private void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            noData_imageView.setVisibility(View.VISIBLE);
            noData_textView.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                store_id.add(cursor.getString(0));
                store_name.add(cursor.getString(1));
                store_barcode.add(cursor.getString(2));
            }

            noData_imageView.setVisibility(View.GONE);
            noData_textView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            confirmDeleteAllDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDeleteAllDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StoresDB myDB = new StoresDB(MainActivity.this);
                myDB.deleteAllData();
                recreate();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void findViews(){
        recyclerView = findViewById(R.id.recyclerView);
        add_fab = findViewById(R.id.add_button);
        noData_textView = findViewById(R.id.no_data);
        noData_imageView = findViewById(R.id.no_data_imageView);
    }
}