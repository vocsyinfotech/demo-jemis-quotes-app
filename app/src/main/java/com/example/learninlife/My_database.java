package com.example.learninlife;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class My_database extends SQLiteOpenHelper {

    int[] imageArray = new int[]{R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6, R.drawable.img7, R.drawable.img8, R.drawable.img9, R.drawable.img10, R.drawable.img11, R.drawable.img12, R.drawable.img13, R.drawable.img14, R.drawable.img15, R.drawable.img16, R.drawable.img17, R.drawable.img18, R.drawable.img19, R.drawable.img20, R.drawable.img21, R.drawable.img22, R.drawable.img23, R.drawable.img24, R.drawable.img25, R.drawable.img26, R.drawable.img27, R.drawable.img28, R.drawable.img29, R.drawable.img30, R.drawable.img31, R.drawable.img32, R.drawable.img33, R.drawable.img34, R.drawable.img35, R.drawable.img36};

    private static final String DB_PATH_SUFFIX = "/databases/";
    private static final String MY_DB_NAME = "quotes.db";
    Context context;
    ArrayList<Items> itemList = new ArrayList<>();
    ArrayList<Quotes_list> quotesList = new ArrayList<>();

    public My_database(@Nullable Context context) {
        super(context, MY_DB_NAME, null, 1);
        this.context = context;
    }

    public ArrayList<Items> getDetails() throws IOException {

        int pos = 0;
        itemList.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categories", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
//                Bitmap bm = null;
//                try {
//                    bm = getBitmapFromAsset("category_images/category" + itemList.size() + ".png");
//                    Log.e("bmName", "bmh==>" + bm.getHeight() + " " + "bmw==>" + bm.getWidth() + "=====" + itemList.size() + "");
//                } catch (IOException e) {
//                    e.printStackTrace();
//
//                    Log.e("IOException", "" + "=====" + itemList.size() + "");
//                }

                Log.e("itemslist", cursor.getString(1));
                Items itm = new Items(cursor.getInt(0), cursor.getString(1), imageArray[pos]);
                itemList.add(itm);
                pos++;
            }
            cursor.close();
            db.close();

            return itemList;
        }
        Log.e("return", "return" + itemList.size());
        return null;
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
        InputStream myInput = context.getAssets().open(MY_DB_NAME);
// Path to the just created empty db
        String outFileName = getDatabasePath();

// if the path doesn't exist first, create it
        File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
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

        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + MY_DB_NAME;
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    public SQLiteDatabase openDataBase() throws SQLException {

        Log.e("dbFile", "dbFile");
        File dbFile = context.getDatabasePath(MY_DB_NAME);


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

                fav = cursor.getInt(0);

            }
            cursor.close();
            db.close();
            return fav;
        }
        return fav;
    }

//    public ArrayList<Quotes_list> getQuotesOfTheDay(int quote_id) {
//        //ArrayList qod=new ArrayList();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM contents where id=" + quote_id, null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                Quotes_list q = new Quotes_list(cursor.getInt(0), cursor.getInt(2), cursor.getString(1), cursor.getInt(3));
//                quotesList.add(q);
//            }
//            cursor.close();
//            db.close();
//            return quotesList;
//        }
//        return null;
//    }

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

