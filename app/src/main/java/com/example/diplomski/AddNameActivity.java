package com.example.diplomski;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNameActivity extends AppCompatActivity {

    Button next_button;
    Button cancel_button;
    EditText storeName_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);

        findViews();

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storeName_editText.getText().toString().trim().length() <= 0){
                    confirmNoStoreNameDialog();
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmExitDialog();
            }
        });
    }

    private void confirmNoStoreNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Store name missing!");
        builder.setMessage("Are you sure you want to continue without store name?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAddBarcodeActivity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void confirmExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to cancel new loyalty card input?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startMainActivity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void startAddBarcodeActivity(){
        Intent intent = new Intent(AddNameActivity.this, AddBarcodeActivity.class);
        startActivity(intent);
    }

    private void startMainActivity(){
        Intent intent = new Intent(AddNameActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void findViews(){
        next_button = findViewById(R.id.next_AddName_Button);
        cancel_button = findViewById(R.id.cancel_AddName_Button);
        storeName_editText = findViewById(R.id.editStoreName_AddName_EditText);
    }
}