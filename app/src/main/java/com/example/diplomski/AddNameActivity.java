package com.example.diplomski;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNameActivity extends AppCompatActivity {

    Button next_button;
    Button cancel_button;
    EditText storeName_editText;

    StoresDB myDB;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);

        myDB = new StoresDB(AddNameActivity.this);
        pref = getApplicationContext().
                getSharedPreferences("store_id", MODE_PRIVATE);
        editor = pref.edit();

        findViews();

        getIntentData();

        changeNextButtonVisibility();

        storeName_editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                changeNextButtonVisibility();
                return false;
            }
        });

        next_button.setOnClickListener(v -> {
            if(storeName_editText.getText().toString().trim().length() <= 0){
                confirmNoStoreNameDialog();
            }else{
                if(pref.getString("id", null) == null){
                    storeNameToDatabase();
                }
                if(pref.getString("id", null) != null){
                    startAddBarcodeActivity();
                }
                else{
                    Toast.makeText(this, "Something went wrong with Name creation",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel_button.setOnClickListener(v -> confirmExitDialog());
    }

    private void confirmNoStoreNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Store name missing!");
        builder.setMessage("Are you sure you want to continue without store name?");
        builder.setPositiveButton("Yes", (dialog, which) -> startAddBarcodeActivity());
        builder.setNegativeButton("No", (dialog, which) -> {

        });
        builder.create().show();
    }

    private void confirmExitDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Are you sure you want to cancel new loyalty card input?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                if (storeName_editText.getText().length() > 0) {
                    discardOrSaveDataDialog();
                }
                else {
                    startMainActivity();
                }
            });
            builder.setNegativeButton("No", (dialog, which) -> {/*do nothing*/});
            builder.create().show();
    }

    private void discardOrSaveDataDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save or discard");
        builder.setMessage("Do you want to save or discard input data?");
        builder.setPositiveButton("Save", (dialog, which) ->{
            if(myDB.getStoreName(pref.getString("id", null)) == null){
                myDB.addStoreName(storeName_editText.getText().toString().trim());
            }
            startMainActivity();
        });
        builder.setNegativeButton("Discard", (dialog, which) -> {
            myDB.deleteOneRow(pref.getString("id", null));
            startMainActivity();
        });
        builder.create().show();
    }

    private void startAddBarcodeActivity(){
        Intent intent = new Intent(AddNameActivity.this, AddBarcodeActivity.class);
        if(pref.getString("id", null) != null){
            intent.putExtra("id", pref.getString("id", null));
        }
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

    private void storeNameToDatabase(){
        if(storeName_editText.getText().toString().length() <= 0){
            storeName_editText.setError("Store name field cannot be empty!");
            Toast.makeText(AddNameActivity.this,
                    "Store name field cannot be empty!", Toast.LENGTH_SHORT).show();
        }else{
            editor.putString("id",
                    Long.toString(myDB.addStoreName(
                            storeName_editText.getText().toString().trim())));
            editor.commit();
        }
    }

    private void getIntentData(){
        if(pref.getString("id", null) != null){
            storeName_editText.setText(myDB.getStoreName(pref.getString("id", null)));
        }
    }

    private void changeNextButtonVisibility(){
        if(storeName_editText.getText().length() <= 0){
            next_button.setVisibility(Button.GONE);
        }else{
            next_button.setVisibility(Button.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        confirmExitDialog();
    }
}