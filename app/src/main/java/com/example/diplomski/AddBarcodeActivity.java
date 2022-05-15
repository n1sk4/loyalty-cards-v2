package com.example.diplomski;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.List;

public class AddBarcodeActivity extends AppCompatActivity {
    Button captureImage_button;
    Button confirm_button;
    Button generate_button;
    EditText barcodeNumber_text;
    TextView barcode_text;
    ImageView barcode_image;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch barcodeQR_switch;

    String barcodeNumber;
    boolean switchSelection = false; //false = barcode; true = QR code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barcode);

        captureImage_button = findViewById(R.id.capturePicture_button);
        confirm_button = findViewById(R.id.confirm_button);
        barcodeNumber_text = findViewById(R.id.barcodeNumber_editText);
        barcode_text = findViewById(R.id.barcode_textView);
        barcodeQR_switch = findViewById(R.id.barcodeQR_switch);
        generate_button = findViewById(R.id.generate_button);
        barcode_image = findViewById(R.id.addBarcode_imageView);

        generate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateBarcodeImage();
            }
        });

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(barcodeNumber_text.getText().length() <= 0){
                    barcodeNumber_text.setError("Barcode field cannot be empty!");
                    Toast.makeText(AddBarcodeActivity.this, "Barcode field cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(AddBarcodeActivity.this, AddActivity.class);
                    startActivity(intent);
                    Toast.makeText(AddBarcodeActivity.this, "Barcode added successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        barcodeQR_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBarcodeStates();
            }
        });

        captureImage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAndPlaceBarcode();
            }
        });
    }

    private void generateBarcodeImage(){
        if(barcodeNumber_text.getText().toString().trim().length() <= 0) {
            barcodeNumber_text.setError("Enter barcode manually field cannot be empty!");
            Toast.makeText(AddBarcodeActivity.this, "Enter barcode manually field cannot be empty!", Toast.LENGTH_SHORT).show();
        }
        else if(barcodeNumber_text.getText().toString().trim().length() != 13 && !switchSelection){
            Toast.makeText(AddBarcodeActivity.this, "Barcode must be 13 numbers long!", Toast.LENGTH_SHORT).show();
        }
        else {
            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                if (switchSelection) {
                    BitMatrix matrix = writer.encode(barcodeNumber_text.getText().toString().trim(), BarcodeFormat.QR_CODE, 350, 350);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    barcode_image.setImageBitmap(bitmap);
                } else {
                    BitMatrix matrix = writer.encode(barcodeNumber_text.getText().toString().trim(), BarcodeFormat.CODE_128, 500, 350);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    barcode_image.setImageBitmap(bitmap);
                }
                InputMethodManager manager = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE
                );
                manager.hideSoftInputFromWindow(barcode_text.getApplicationWindowToken(), 0);
                if(switchSelection){
                    Toast.makeText(AddBarcodeActivity.this, "QR code generated successfully!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddBarcodeActivity.this, "Barcode generated successfully!", Toast.LENGTH_SHORT).show();
                }
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    private void switchBarcodeStates(){
        switchSelection = !switchSelection;
        if(switchSelection){
            barcode_text.setText("Generate QR code");
        }else{
            barcode_text.setText("Generate Barcode");
        }
    }

    private void selectAndPlaceBarcode(){
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(AddBarcodeActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    barcode_image.setImageBitmap(bitmap);
                    getTextFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getTextFromImage(Bitmap bitmap){
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if(!recognizer.isOperational()){
            Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show();
        }
        else{
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            scanBarcodes(image);
        }
    }


    private void scanBarcodes(InputImage image){
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_ALL_FORMATS)
                        .build();

        BarcodeScanner scanner = BarcodeScanning.getClient();

        Task<List<Barcode>> result = scanner.process(image).addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
            @Override
            public void onSuccess(List<Barcode> barcodes) {
                for(Barcode barcode : barcodes){
                    Rect bounds = barcode.getBoundingBox();
                    Point[] corners = barcode.getCornerPoints();

                    String rawValue = barcode.getRawValue();

                    int valueType = barcode.getValueType();

                    switch (1){
                        default:
                        String returnValue = barcode.getDisplayValue();
                        Toast.makeText(AddBarcodeActivity.this, rawValue, Toast.LENGTH_SHORT).show();
                        barcodeNumber_text.setText(returnValue);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}