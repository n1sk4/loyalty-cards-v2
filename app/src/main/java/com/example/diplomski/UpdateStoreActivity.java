package com.example.diplomski;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class UpdateStoreActivity extends AppCompatActivity {

    EditText name_editText;
    EditText barcode_editText;
    Button update_button;
    Button delete_button;
    Button back_button;
    Button scanBarcode_button;
    ImageView logo_imageView;
    View layout;

    String id, name, barcode;
    Bitmap logo;

    StoresDB myDB;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_store);

        myDB = new StoresDB(UpdateStoreActivity.this);
        pref = getApplicationContext().
                getSharedPreferences("store_id_barcode", MODE_PRIVATE);
        editor = pref.edit();

        findViews();

        getIntentData();

        changeBackgroundColor(logo);

        ActionBar ab = getSupportActionBar();
        if(name != null){
            assert ab != null;
            ab.setTitle(name + " - edit store data");
        }

        update_button.setOnClickListener(v -> {
            name = name_editText.getText().toString().trim();
            barcode = barcode_editText.getText().toString().trim();
            myDB.updateData(id, name, barcode, logo);
            ActionBar ab1 = getSupportActionBar();
            if(name != null){
                assert ab1 != null;
                ab1.setTitle(name);
            }
            startShowBarcodeActivity();
        });

        delete_button.setOnClickListener(v -> confirmDialog());

        back_button.setOnClickListener(v -> startShowBarcodeActivity());

        logo_imageView.setOnClickListener(v -> selectAndPlaceLogo());
    }

    @Override
    public void onBackPressed() {
        startShowBarcodeActivity();
    }



    void getIntentData(){
        if(pref.getString("id", null) != null){
            id = pref.getString("id", null);

            name = myDB.getStoreName(id);
            barcode = myDB.getStoreBarcode(id);
            logo = myDB.getStoreLogo(id);

            name_editText.setText(name);
            barcode_editText.setText(barcode);
            if(logo == null){
                logo_imageView.setImageResource(R.drawable.ic_shopping_bag);
            }else{
                logo_imageView.setImageBitmap(logo);
            }
        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            startMainActivity();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + "?");
        builder.setMessage("Are you sure you want to delete " + name + "?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            myDB.deleteOneRow(id);
            myDB.updateData(id, name, barcode, logo);
            startMainActivity();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            //TODO Do something
        });
        builder.create().show();
    }

    private void selectAndPlaceLogo(){
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(
                UpdateStoreActivity.this);
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), resultUri);
                    logo = bitmap;
                    logo_imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void findViews(){
        name_editText = findViewById(R.id.storeName_Update_EditText);
        barcode_editText = findViewById(R.id.barcodeInput_Update_EditText);
        update_button = findViewById(R.id.update_Update_Button);
        delete_button = findViewById(R.id.delete_Update_Button);
        back_button = findViewById(R.id.return_Update_Button);
        scanBarcode_button = findViewById(R.id.scanBarcode_Update_Button);
        logo_imageView = findViewById(R.id.addLogo_Update_ImageView);
        layout = findViewById(R.id.layout_Update);
    }

    private void startMainActivity(){
        Intent intent = new Intent(UpdateStoreActivity.this,
                com.example.diplomski.MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void startShowBarcodeActivity(){
        Intent intent = new Intent(UpdateStoreActivity.this, ShowBarcodeActivity.class);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void changeBackgroundColor(Bitmap bitmap){
        if(logo != null){
            Palette.from(bitmap).generate(palette -> {
                assert palette != null;
                layout.setBackgroundColor(palette.getDominantColor(ContextCompat
                        .getColor(UpdateStoreActivity.this, R.color.white)));
            });
        }
    }
}