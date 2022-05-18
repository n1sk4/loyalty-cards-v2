package com.example.diplomski;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;


public class AddActivity extends AppCompatActivity {

    EditText storeName_editText;
    Button add_button;
    Button addLogo_button;
    Button addBarcode_button;
    ImageView logo_imageView;
    ImageView barcode_imageView;
    String id, barcode;

    private static final int REQUEST_CAMERA_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        findViews();

        getAddBarcodeIntentDataAndSetImage();

        if(ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoresDB myDB = new StoresDB(AddActivity.this);
                if(storeName_editText.getText().toString().length() <= 0){
                    storeName_editText.setError("Store name field cannot be empty!");
                    Toast.makeText(AddActivity.this, "Store name field cannot be empty!", Toast.LENGTH_SHORT).show();
                }else{
                    long db_result_name = myDB.addStoreName(storeName_editText.getText().toString().trim());
                    storeName_editText.setError(null);
                    if(barcode != null){
                        long db_result_barcode = myDB.addStoreBarcode(barcode.trim(), String.valueOf(db_result_name));
                        Intent intent = new Intent(AddActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                        openDialogBarcodeMissing();
                    }
                }
            }
        });

        addLogo_button.setOnClickListener(v -> selectAndPlaceLogo());

        logo_imageView.setOnClickListener(v -> selectAndPlaceLogo());

        addBarcode_button.setOnClickListener(v -> openAddBarcodeActivity());

        barcode_imageView.setOnClickListener(v -> openAddBarcodeActivity());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_new_item_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    logo_imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void selectAndPlaceLogo(){
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(AddActivity.this);
    }

    private void openAddBarcodeActivity(){
        Intent intent = new Intent(AddActivity.this, AddBarcodeActivity.class);
        startActivity(intent);
    }

    private void openDialogBarcodeMissing(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Barcode missing!");
        builder.setMessage("Are you sure you want to continue without " + storeName_editText.getText().toString().trim() + " store barcode?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            startActivity(intent);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            //TODO Do nothing
        });
        builder.create().show();
    }

    private void getAddBarcodeIntentDataAndSetImage(){
        if(getIntent().hasExtra("barcode") && getIntent().hasExtra("AddBarcodeActivity")){
            barcode = getIntent().getStringExtra("barcode");
            try {
                MultiFormatWriter writer = new MultiFormatWriter();
                if(getIntent().getStringExtra("barcodeType").equals("false")){
                    BitMatrix matrix = writer.encode(barcode, BarcodeFormat.CODE_128, 350, 100);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    barcode_imageView.setImageBitmap(bitmap);
                }else{
                    BitMatrix matrix = writer.encode(barcode, BarcodeFormat.AZTEC, 100, 100);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    barcode_imageView.setImageBitmap(bitmap);
                }
                addBarcode_button.setText("Update barcode");
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

        if(id != null) Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }

    private void findViews(){
        storeName_editText = findViewById(R.id.name_input);
        add_button = findViewById(R.id.add_button);
        addLogo_button = findViewById(R.id.add_logo);
        addBarcode_button = findViewById(R.id.add_barcode);
        logo_imageView = findViewById(R.id.addLogo_imageView);
        barcode_imageView = findViewById(R.id.barcode_ImageView);
    }
}