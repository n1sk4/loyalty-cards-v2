package com.example.diplomski;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class StoresDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Stores.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME      = "Stores";
    private static final String COLUMN_ID       = "_id";
    private static final String COLUMN_STORE    = "store_name";
    private static final String COLUMN_BARCODE  = "barcode";
    private static final String COLUMN_LOGO     = "logo";


    StoresDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =  "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COLUMN_STORE + " TEXT," +
                            COLUMN_BARCODE + " TEXT," +
                            COLUMN_LOGO + " BLOB);";
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
        return db.insert(TABLE_NAME, null, cv);
    }

    @SuppressLint("Range")
    String getStoreName(String row_id){
        if(row_id != null) {
            if (Integer.parseInt(row_id) > 0) {
                String query = "SELECT " + row_id + ", " + COLUMN_STORE + " FROM " + TABLE_NAME
                        + " WHERE " + COLUMN_ID + "=?";
                SQLiteDatabase db = this.getReadableDatabase();

                Cursor cursor = db.rawQuery(query, new String[]{row_id + ""});
                cursor.moveToFirst();
                if (cursor.getCount() > 0 && !cursor.isNull(1)) {
                    return cursor.getString(1);
                }
                cursor.close();
            }
        }
        return null;
    }

    long addStoreBarcode(String row_id, String storeBarcode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BARCODE, storeBarcode);
        return db.update(TABLE_NAME, cv,"_id=?", new String[]{row_id});
    }

    String getStoreBarcode(String row_id){
        if(row_id != null) {
            if (Integer.parseInt(row_id) > 0) {
                String query = "SELECT " + row_id + ", " + COLUMN_BARCODE + " FROM " + TABLE_NAME
                        + " WHERE " + COLUMN_ID + "=?";
                SQLiteDatabase db = this.getReadableDatabase();

                Cursor cursor = db.rawQuery(query, new String[]{row_id + ""});
                cursor.moveToFirst();
                if (cursor.getCount() > 0 && !cursor.isNull(1)) {
                    return cursor.getString(1);
                }
                cursor.close();
            }
        }
        return null;
    }

    long addStoreLogo(String row_id, Bitmap storeLogo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        byte[] image = getBitmapAsByteArray(storeLogo);
        cv.put(COLUMN_LOGO, image);
        return db.update(TABLE_NAME, cv,"_id=?", new String[]{row_id});
    }

    Bitmap getStoreLogo(String row_id){
        if(row_id != null) {
            if (Integer.parseInt(row_id) > 0) {
                String query = "SELECT " + row_id + ", " + COLUMN_LOGO + " FROM " + TABLE_NAME
                        + " WHERE " + COLUMN_ID + "=?";
                SQLiteDatabase db = this.getReadableDatabase();

                Cursor cursor = db.rawQuery(query, new String[]{row_id + ""});
                cursor.moveToFirst();
                if (cursor.getCount() > 0 && !cursor.isNull(1)) {
                    return BitmapFactory.decodeByteArray((cursor.getBlob(1)),
                            0, (cursor.getBlob(1).length));
                }
                cursor.close();
            }
        }
        return null;
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

    long updateData(String row_id, String storeName, String storeBarcode, Bitmap storeLogo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(storeName != null) cv.put(COLUMN_STORE, storeName);
        if(storeBarcode != null) cv.put(COLUMN_BARCODE, storeBarcode);
        if(storeLogo != null){
            byte[] image = getBitmapAsByteArray(storeLogo);
            cv.put(COLUMN_LOGO, image);
        }

        return db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
    }

    long deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");
    }

    @NonNull
    private static byte[] getBitmapAsByteArray(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
