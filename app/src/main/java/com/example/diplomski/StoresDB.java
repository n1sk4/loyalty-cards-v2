package com.example.diplomski;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class StoresDB extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Stores.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME      = "Stores";
    private static final String COLUMN_ID       = "_id";
    private static final String COLUMN_STORE    = "store_name";
    private static final String COLUMN_BARCODE  = "barcode";


    StoresDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =  "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COLUMN_STORE + " TEXT," +
                            COLUMN_BARCODE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    long addStoreName(String storeName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_STORE, storeName);

        long result = db.insert(TABLE_NAME, null, cv);

        //TODO Delete in the future *Debugging helper*
        if(result == -1){
            Toast.makeText(context, "Failed to add store name", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    long addStoreBarcode(String storeBarcode, String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_BARCODE, storeBarcode);

        long result = db.update(TABLE_NAME,cv, "_id?", new String[]{row_id});

        //TODO Delete in the future *Debugging helper*
        if(result == -1){
            Toast.makeText(context, "Failed to add store barcode", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query,null);
        }

        return cursor;
    }

    void updateData(String row_id, String storeName, String storeBarcode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_STORE, storeName);
        cv.put(COLUMN_BARCODE, storeBarcode);

        long result_name = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});

        if (result_name == -1){
            Toast.makeText(context, "Failed to update!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});

        if(result == -1){
            Toast.makeText(context, "Failed to Delete!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
