package com.example.diplomski;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ShowBarcodeActivity extends AppCompatActivity {

    ImageView barcode_imageView;
    ImageView logo_imageView;
    Button editData_button;
    Button close_button;
    TextView barcode_text;
    View layout;

    String id, name, barcode;
    Bitmap logo;

    StoresDB myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_barcode);

        findViews();

        getIntentData();

        ActionBar ab = getSupportActionBar();
        if(name != null){
            assert ab != null;
            ab.setTitle(name + " barcode");
        }

        generateBarcodeImage();
        generateLogoImage();

        changeBackgroundColor(logo);

        barcode_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(barcode.equals(""))startUpdateActivity();
            }
        });

        logo_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logo == null)startUpdateActivity();
            }
        });

        editData_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdateActivity();
            }
        });

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });
    }

    private void getIntentData(){
        if(getIntent().hasExtra("id")){
            id = getIntent().getStringExtra("id");
            myDB = new StoresDB(ShowBarcodeActivity.this);

            name = myDB.getStoreName(id);
            barcode = myDB.getStoreBarcode(id);
            logo = myDB.getStoreLogo(id);
        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            startMainActivity();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void generateBarcodeImage(){
        MultiFormatWriter writer = new MultiFormatWriter();
        if(!barcode.equals("")){
            try {
                int width, height;
                width = 800;
                height = 1000;
                BitMatrix matrix = writer.encode(barcode, BarcodeFormat.CODE_128, width, height);
                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(matrix);
                barcode_imageView.setImageBitmap(bitmap);
                barcode_text.setText(barcode);
                barcode_text.setTextColor(R.color.black);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }else {
            barcode_text.setTextColor(R.color.gray);
            barcode_text.setText("BARCODE MISSING");
            //TODO Do something
        }
    }

    private void generateLogoImage(){
        if(logo != null){
            logo_imageView.setVisibility(ImageView.VISIBLE);
            logo_imageView.setImageBitmap(logo);
        }else {
            logo_imageView.setVisibility(ImageView.GONE);
        }
    }

    private void findViews(){
        barcode_imageView = findViewById(R.id.barcode_Barcode_ImageView);
        logo_imageView = findViewById(R.id.logo_Barcode_ImageView);
        editData_button = findViewById(R.id.editData_Barcode_Button);
        close_button = findViewById(R.id.closeActivity_Barcode_Button);
        barcode_text = findViewById(R.id.barcodeText_Barcode_TextView);
        layout = findViewById(R.id.showBarcode_Layout);
    }

    private void startUpdateActivity(){
        Intent intent = new Intent(ShowBarcodeActivity.this, UpdateStoreActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void startMainActivity(){
        Intent intent = new Intent(ShowBarcodeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void changeBackgroundColor(Bitmap bitmap){
        if(logo != null){
            Palette.from(bitmap).generate(palette -> {
                assert palette != null;
                layout.setBackgroundColor(palette.getDominantColor(ContextCompat
                        .getColor(ShowBarcodeActivity.this, R.color.white)));
            });
        }
    }
}