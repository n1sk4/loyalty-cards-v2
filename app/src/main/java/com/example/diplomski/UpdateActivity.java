package com.example.diplomski;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {

    EditText name_editText;
    Button update_button;
    Button delete_button;
    Button updateLogo_button;
    ImageView logo_imageView;

    String id, name, barcode;
    Bitmap logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        findViews();

        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if(name != null){
            ab.setTitle(name);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoresDB myDB = new StoresDB(UpdateActivity.this);
                name = name_editText.getText().toString().trim();
                barcode = "000000000000";
                myDB.updateData(id, name, barcode, logo);
                ActionBar ab = getSupportActionBar();
                if(name != null){
                    assert ab != null;
                    ab.setTitle(name);
                }
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

        updateLogo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAndPlaceLogo();
            }
        });
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name")
                && getIntent().hasExtra("barcode")){
            //Getting Data From Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            barcode = getIntent().getStringExtra("barcode");
            //Setting Data
            name_editText.setText(name);
        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + "?");
        builder.setMessage("Are you sure you want to delete " + name + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StoresDB myDB = new StoresDB(UpdateActivity.this);
                myDB.deleteOneRow(id);
                myDB.updateData(id, name, barcode, logo);
                Intent intent = new Intent(UpdateActivity.this,
                        com.example.diplomski.MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO Do something
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void selectAndPlaceLogo(){
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(
                UpdateActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), resultUri);
                    logo_imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void findViews(){
        name_editText = findViewById(R.id.name_input_up);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);
        updateLogo_button = findViewById(R.id.update_logo);
        logo_imageView = findViewById(R.id.addLogo_imageView);
    }
}