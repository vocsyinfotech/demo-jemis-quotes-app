package com.example.learninlife;


import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatBaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "quotes.db";
    public static final String DBLocation = "/databases/";
    private final Context context;
    ArrayList<items> itemList = new ArrayList<>();
    ArrayList<Quotes_list> quotesList = new ArrayList<>();

    public DatBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    public ArrayList<items> getDetails() throws IOException {

        itemList.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categories", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Bitmap bm = null;
                try {
                    bm = getBitmapFromAsset("category_images/category" + itemList.size() + ".png");
                    Log.e("bmName", "bmh==>" + bm.getHeight() + " " + "bmw==>" + bm.getWidth() + "=====" + itemList.size() + "");
                } catch (IOException e) {
                    e.printStackTrace();

                    Log.e("IOException", "" + "=====" + itemList.size() + "");
                }

                Log.e("itemslist", cursor.getString(1));
                items itm = new items(cursor.getInt(0), cursor.getString(1), bm);
                itemList.add(itm);
            }
            cursor.close();
            db.close();

            return itemList;
        }
        Log.e("return", "return" + itemList.size());
        return null;
    }

    private Bitmap getBitmapFromAsset(String strName) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream istr = assetManager.open(strName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        istr.close();
        return bitmap;
    }

    public ArrayList<Quotes_list> getQuotes(int category_id) throws IOException {
        quotesList.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contents where category_id=" + category_id, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Quotes_list q = new Quotes_list(cursor.getInt(0), cursor.getInt(2), cursor.getString(1), cursor.getInt(3));
                quotesList.add(q);

            }
            cursor.close();
            db.close();
            return quotesList;
        }
        return null;
    }

    public void CopyDataBaseFromAsset() throws IOException {
        InputStream myInput = context.getAssets().open(DB_NAME);
// Path to the just created empty db
        String outFileName = getDatabasePath();

// if the path doesn't exist first, create it
        File f = new File(context.getApplicationInfo().dataDir + DBLocation);
        if (!f.exists()) {
            f.mkdir();
        }

// Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

// transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);

            Log.e("myOutput", "====" + length);
        }
// Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public String getDatabasePath() {

        return context.getApplicationInfo().dataDir + DBLocation
                + DB_NAME;
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    public SQLiteDatabase openDataBase() throws SQLException {

        Log.e("dbFile", "dbFile");
        File dbFile = context.getDatabasePath(DB_NAME);


        Log.e("openDataBase", "openDataBase");
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Log.e("CopyDataBaseFromAsset", "CopyDataBaseFromAsset");
            } catch (IOException e) {

                Log.e("IOException", "IOException" + e.toString());
                throw new RuntimeException("Error creating source database", e);
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public void liked_list_update(int pos, int fav) {

        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE contents SET favorite=" + fav + " where id=" + pos;
        db.execSQL(strSQL);

    }

    public ArrayList<Quotes_list> get_like_list() {
        quotesList.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        int i = 1;
        Cursor cursor = db.rawQuery("SELECT * FROM contents WHERE favorite=" + i, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Quotes_list q = new Quotes_list(cursor.getInt(0), cursor.getInt(2), cursor.getString(1), cursor.getInt(3));
                quotesList.add(q);

            }
            cursor.close();
            db.close();
            return quotesList;
        }
        return null;
    }


    public int get_like_quotes(int i) {
        quotesList.clear();
        SQLiteDatabase db = this.getReadableDatabase();

        int fav = 0;
        Cursor cursor = db.rawQuery("SELECT favorite FROM contents WHERE id=" + i, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
               /* Quotes_list q=new Quotes_list(cursor.getInt(0),cursor.getInt(2),cursor.getString(1),cursor.getInt(3));
                quotesList.add(q);*/

                fav = cursor.getInt(0);

            }
            cursor.close();
            db.close();
            return fav;
        }
        return fav;
    }

    public ArrayList<Quotes_list> getQuotesOfTheDay(int quote_id) {
        //ArrayList qod=new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contents where id=" + quote_id, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Quotes_list q = new Quotes_list(cursor.getInt(0), cursor.getInt(2), cursor.getString(1), cursor.getInt(3));
                quotesList.add(q);
            }
            cursor.close();
            db.close();
            return quotesList;
        }
        return null;
    }

    public String getCategoryName(int catId) {
        String s = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT category_name FROM categories where id=" + catId, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                s = cursor.getString(0);
            }
            cursor.close();
            db.close();
            return s;
        }
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}


//    private final Context context;
//    ArrayList<Model> quotesList = new ArrayList<>();
//
//    public DatBaseHelper(@Nullable Context context) {
//        super(context, DB_NAME, null, 1);
//        this.context = context;
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//
//    }
//
//    public SQLiteDatabase openDataBase() throws SQLException {
//
//        Log.e("dbFile", "dbFile");
//        File dbFile = context.getDatabasePath(DB_NAME);
//
//
//        Log.e("openDataBase", "openDataBase");
////        if (!dbFile.exists()) {
//        try {
//            copydatabse();
//            Log.e("CopyDataBaseFromAsset", "CopyDataBaseFromAsset");
//        } catch (IOException e) {
//
//            Log.e("IOException", "IOException" + e.toString());
//            throw new RuntimeException("Error creating source database", e);
//        }
////        }
//        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
//    }
//
//    public List<Model> getModelList() {
//
//        try {
//            quotesList.clear();
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            int i = 1;
//            Cursor cursor = db.rawQuery("SELECT * FROM categories", null);
//
//            if (cursor != null) {
//                while (cursor.moveToNext()) {
//                    Model q = new Model(cursor.getInt(0), cursor.getString(1));
//                    quotesList.add(q);
//                }
//                cursor.close();
//                db.close();
//                return quotesList;
//            }
//        } catch (Exception e) {
//            Log.e("JSFVJHBXCJ", "getModelList: " + e.getMessage());
//        }
//
//
//        return null;
//    }
//
//    public boolean copydatabse() throws IOException {
//        InputStream inputStream = context.getAssets().open(DatBaseHelper.DB_NAME);
//// Path to the just created empty db
//        String outFileName = DatBaseHelper.DBLocation + DatBaseHelper.DB_NAME;
//
//// if the path doesn't exist first, create it
//        File f = new File(context.getApplicationInfo().dataDir + DatBaseHelper.DBLocation);
//        if (!f.exists()) {
//            f.mkdirs();
//        }
//
//// Open the empty db as the output stream
//        OutputStream myOutput = new FileOutputStream(outFileName);
//
//// transfer bytes from the inputfile to the outputfile
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = inputStream.read(buffer)) > 0) {
//            myOutput.write(buffer, 0, length);
//
//            Log.e("myOutput", "====" + length);
//        }
//// Close the streams
//        myOutput.flush();
//        myOutput.close();
//        inputStream.close();
//        return false;
//    }
//
////    public void openDatabse() {
////        String dbpath = context.getDatabasePath(DB_NAME).getPath();
////        if (context != null && mDatabase.isOpen()) {
////            return;
////        }
////        mDatabase = SQLiteDatabase.openDatabase(dbpath, null, SQLiteDatabase.OPEN_READWRITE);
////    }
//
//    public void closeDatabse() {
////        if (mDatabase != null) {
////            mDatabase.close();
////        }
//    }
//
//    public String getDatabasePath() {
//
//        return context.getApplicationInfo().dataDir + DBLocation
//                + DB_NAME;
//    }
//}
////        Model model = null;
////        List<Model> modelList = new ArrayList<>();
////        openDatabse();
////        Cursor cursor = mDatabase.rawQuery("SELECT * FROM categories", null);
////        while (!cursor.isAfterLast()) {
////            model = new Model(cursor.getInt(0), cursor.getString(1));
////            modelList.add(model);
////            cursor.moveToNext();
////        }
////        cursor.close();
////        closeDatabse();
////        return  modelList;
////    }
//
//
//