package com.example.diplomski;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;

public class AddLogoActivity extends AppCompatActivity {

    Button captureImage_button;
    Button back_button;
    Button next_button;
    ImageView logoPreview_imageView;
    String data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_logo);

        findViews();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddBarcodeActivity();
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNoDataDialog();
            }
        });
    }

    private void confirmNoDataDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Data missing!");
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
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

    private void startMainActivity(){
        Intent intent = new Intent(AddLogoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void startBarcodeActivity(){
        Intent intent = new Intent(AddLogoActivity.this, BarcodeActivity.class);
        startActivity(intent);
    }

    private void startAddBarcodeActivity(){
        Intent intent = new Intent(AddLogoActivity.this, AddBarcodeActivity.class);
        startActivity(intent);
    }

    private void findViews(){
        captureImage_button = findViewById(R.id.captureImage_AddLogo_Button);
        back_button = findViewById(R.id.back_AddLogo_Button);
        next_button = findViewById(R.id.next_AddLogo_Button);
        logoPreview_imageView = findViewById(R.id.logoPreview_AddLogo_ImageView);
    }
}