package com.example.diplomski;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class AddLogoActivity extends AppCompatActivity {

    Button captureImage_button;
    Button back_button;
    Button next_button;
    ImageView logoPreview_imageView;

    String database_id;
    Bitmap logoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_logo);

        findViews();

        getIntentData();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddBarcodeActivity();
            }
        });

        logoPreview_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAndPlaceLogo();
            }
        });

        captureImage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAndPlaceLogo();
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logoBitmap == null){
                    confirmNoDataDialog();
                }else{
                    storeLogoToDatabase();
                    startMainActivity();
                }
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
        Intent intent = new Intent(AddLogoActivity.this, ShowBarcodeActivity.class);
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

    private void getIntentData(){
        if(getIntent().hasExtra("id")){
            database_id = getIntent().getStringExtra("id");
        }
    }

    private void storeLogoToDatabase(){
        StoresDB myDB = new StoresDB(AddLogoActivity.this);
        if(database_id == null) {
            Toast.makeText(AddLogoActivity.this,
                    "You are missing store name!", Toast.LENGTH_SHORT).show();
        }else if(logoBitmap == null){
            Toast.makeText(this, "Logo is missing!", Toast.LENGTH_SHORT).show();
        }else{
            myDB.addStoreLogo(database_id, logoBitmap);
        }
    }

    private void selectAndPlaceLogo(){
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                .start(AddLogoActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                assert result != null;
                Uri resultUri = result.getUri();
                try {
                    logoBitmap = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), resultUri);
                    logoPreview_imageView.setImageBitmap(logoBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}