package com.example.diplomski;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_barcode);

        myDB = new StoresDB(ShowBarcodeActivity.this);
        pref = getApplicationContext().
                getSharedPreferences("store_id_barcode", MODE_PRIVATE);
        editor = pref.edit();

        findViews();

        getIntentData();

        ActionBar ab = getSupportActionBar();
        if (name != null) {
            assert ab != null;
            ab.setTitle(name + " barcode");
        }

        generateBarcodeImage();
        generateLogoImage();

        changeBackgroundColor(logo);

        animateBarcode();

        barcode_imageView.setOnClickListener(v -> {
            if (barcode == null || barcode_text.getText().equals("BARCODE MISSING")) startUpdateActivity();
        });

        logo_imageView.setOnClickListener(v -> {
            if (logo == null) startUpdateActivity();
        });

        editData_button.setOnClickListener(v -> startUpdateActivity());

        close_button.setOnClickListener(v -> startMainActivity());
    }

    private void getIntentData() {
        if (pref.getString("id", null) == null) {
            if (getIntent().hasExtra("id")) {
                id = getIntent().getStringExtra("id");

                editor.putString("id", id);
                editor.commit();

                name = myDB.getStoreName(pref.getString("id", "No name"));
                barcode = myDB.getStoreBarcode(pref.getString("id", "000000000000"));
                logo = myDB.getStoreLogo(pref.getString("id", null));
            } else {
                Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
                startMainActivity();
            }
        }else {
            name = myDB.getStoreName(pref.getString("id", null));
            barcode = myDB.getStoreBarcode(pref.getString("id", null));
            logo = myDB.getStoreLogo(pref.getString("id", null));
        }
    }

    @SuppressLint("ResourceAsColor")
    private void generateBarcodeImage() {
        MultiFormatWriter writer = new MultiFormatWriter();
        if (barcode != null) {
            try {
                int width, height;
                width = 800;
                height = 1000;
                if(barcode.equals("")){
                    barcode = "BARCODE MISSING";
                    barcode_imageView.setImageResource(R.drawable.ic_qr_code);
                }else {
                    BitMatrix matrix = writer.encode(barcode, BarcodeFormat.CODE_128, width, height);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    barcode_imageView.setImageBitmap(bitmap);
                    barcode_imageView.setColorFilter(Color.argb(255, 255, 255, 255));

                    barcode_text.setText(barcode);
                    barcode_text.setTextColor(R.color.black);
                }
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            barcode_imageView.setImageResource(R.drawable.ic_qr_code);
            barcode_text.setTextColor(R.color.gray);
            barcode_text.setText("BARCODE MISSING");
        }
        if (barcode_text.getText().length() > 12) {
            float textSize = (float) (12.0 / (float) barcode_text.getText().length());
            barcode_text.setTextScaleX(textSize);
        }
    }

    private void generateLogoImage() {
        if (logo != null) {
            logo_imageView.setVisibility(ImageView.VISIBLE);
            logo_imageView.setImageBitmap(logo);
        } else {
            logo_imageView.setVisibility(ImageView.GONE);
        }
    }

    private void findViews() {
        barcode_imageView = findViewById(R.id.barcode_Barcode_ImageView);
        logo_imageView = findViewById(R.id.logo_Barcode_ImageView);
        editData_button = findViewById(R.id.editData_Barcode_Button);
        close_button = findViewById(R.id.closeActivity_Barcode_Button);
        barcode_text = findViewById(R.id.barcodeText_Barcode_TextView);
        layout = findViewById(R.id.showBarcode_Layout);
    }

    private void startUpdateActivity() {
        Intent intent = new Intent(ShowBarcodeActivity.this, UpdateStoreActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void startMainActivity() {
        Intent intent = new Intent(ShowBarcodeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void changeBackgroundColor(Bitmap bitmap) {
        if (logo != null) {
            Palette.from(bitmap).generate(palette -> {
                assert palette != null;
                layout.setBackgroundColor(palette.getDominantColor(ContextCompat
                        .getColor(ShowBarcodeActivity.this, R.color.white)));
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.barcode_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.brightness_Barcode_Menu) {
            changeScreenBrightness();
        }
        changeScreenBrightness();
        return super.onOptionsItemSelected(item);
    }

    private void changeScreenBrightness() {
        //TODO Change brightness via Menu Switch
    }

    private void animateBarcode() {
        if (barcode != null) {
            Animation animation = AnimationUtils.loadAnimation(ShowBarcodeActivity.this, R.anim.show_barcode_animation);
            animation.setFillAfter(true);
            barcode_imageView.startAnimation(animation);
        }
    }

    @Override
    public void onBackPressed() {
        startMainActivity();
    }
}