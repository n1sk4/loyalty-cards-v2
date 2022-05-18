package com.example.diplomski;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    Button next_button;
    Button generate_button;
    Button back_button;
    EditText barcodeNumber_editText;
    TextView barcode_textView;
    ImageView barcode_imageView;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch barcodeQR_switch;

    String barcodeNumber;
    boolean switchSelection = false; //false = barcode; true = QR code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barcode);

        findViews();

        generate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateBarcodeImage();
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(barcodeNumber_editText.getText().length() <= 0){
                    confirmNoBarcodeDialog();
                }
                else{
                    startAddLogoActivity();
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBarcodeActivity.this, AddNameActivity.class);
                startActivity(intent);
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
        if(barcodeNumber_editText.getText().toString().trim().length() <= 0) {
            barcodeNumber_editText.setError("Enter barcode manually field cannot be empty!");
            Toast.makeText(AddBarcodeActivity.this, "Enter barcode manually field cannot be empty!", Toast.LENGTH_SHORT).show();
        }
        else if(barcodeNumber_editText.getText().toString().trim().length() != 12 && !switchSelection){
            Toast.makeText(AddBarcodeActivity.this, "Barcode must be 12 numbers long!", Toast.LENGTH_SHORT).show();
        }
        else {
            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                if (switchSelection) {
                    BitMatrix matrix = writer.encode(barcodeNumber_editText.getText().toString().trim(), BarcodeFormat.QR_CODE, 350, 350);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    barcode_imageView.setImageBitmap(bitmap);
                } else {
                    BitMatrix matrix = writer.encode(barcodeNumber_editText.getText().toString().trim(), BarcodeFormat.CODE_128, 500, 350);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    barcode_imageView.setImageBitmap(bitmap);
                }
                InputMethodManager manager = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE
                );
                manager.hideSoftInputFromWindow(barcode_textView.getApplicationWindowToken(), 0);
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
            barcode_textView.setText("Generate QR code");
        }else{
            barcode_textView.setText("Generate Barcode");
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
                    getBarcodeFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getBarcodeFromImage(Bitmap bitmap){
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

                    String returnValue = barcode.getDisplayValue();
                    Toast.makeText(AddBarcodeActivity.this, rawValue, Toast.LENGTH_SHORT).show();
                    barcodeNumber_editText.setText(returnValue);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddBarcodeActivity.this, "Barcode not recognized!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmNoBarcodeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Barcode missing!");
        builder.setMessage("Are you sure you want to continue without " + "StoreName" + " store barcode?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAddLogoActivity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void startAddLogoActivity(){
        Intent intent = new Intent(AddBarcodeActivity.this, AddLogoActivity.class);
        startActivity(intent);
    }

    private void findViews(){
        captureImage_button = findViewById(R.id.captureImage_AddBarcode_Button);
        next_button = findViewById(R.id.confirm_AddBarcode_Button);
        back_button = findViewById(R.id.back_AddBarcode_Button);
        barcodeNumber_editText = findViewById(R.id.barcodeInput_AddBarcode_editText);
        barcode_textView = findViewById(R.id.switchHint_AddBarcode_TextView);
        barcodeQR_switch = findViewById(R.id.barcodeQR_switch);
        generate_button = findViewById(R.id.generateBarcode_AddBarcode_Button);
        barcode_imageView = findViewById(R.id.barcodePreview_AddBarcode_ImageView);
    }
}