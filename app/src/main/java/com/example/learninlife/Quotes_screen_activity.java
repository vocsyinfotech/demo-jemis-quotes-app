package com.example.learninlife;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Quotes_screen_activity extends AppCompatActivity implements Quote_interface {

    My_database database;
    ArrayList<Quotes_list> quotes_lists;
    RecyclerView recyclerView;
    ImageView back_bt_img;
    TextView category_name_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes_screen);

        back_bt_img = findViewById(R.id.back_bt_img);
        category_name_tv = findViewById(R.id.category_title);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.font);

        category_name_tv.setTypeface(typeface);

        back_bt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.quotes_recyclerView);
        database = new My_database(this);
        database.openDataBase();

        //database.getCategoryName(getIntent().getIntExtra("category_id",0));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent().getExtras().getInt("ArrayFoundOrNot") == 1) {
            int category_id = getIntent().getIntExtra("category_id", 0);

//            Toast.makeText(this, "array Found quotes", Toast.LENGTH_SHORT).show();
            quotes_lists = database.get_like_list();
            Quotes_list_adapter adapter0 = new Quotes_list_adapter(this, quotes_lists);
            recyclerView.setAdapter(adapter0);
        } else {
            int category_id = getIntent().getIntExtra("category_id", 0);
            try {
                quotes_lists = database.getQuotes(category_id);
            } catch (IOException e) {
                e.printStackTrace();
            }

            category_name_tv.setTypeface(typeface);
            category_name_tv.setText(database.getCategoryName(category_id));
            Quotes_list_adapter adapter1 = new Quotes_list_adapter(this, quotes_lists);
            recyclerView.setAdapter(adapter1);
        }
    }

    @Override
    public void shareQuote(String content, LinearLayout textview, CardView imageView) {
        //   showMenu(content,textview,imageView);
        PopupMenu menu = new PopupMenu(this, textview);
        menu.getMenu().add("As Text");
        menu.getMenu().add("As Image");

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getTitle() == "As Text") {
                    Intent txtIntent = new Intent(Intent.ACTION_SEND);
                    txtIntent.setType("text/plain");
                    txtIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Quote");
                    txtIntent.putExtra(Intent.EXTRA_TEXT, content);
                    startActivity(Intent.createChooser(txtIntent, "Share"));
                }
                if (item.getTitle() == "As Image") {
                    loadView(imageView);
                }
                return false;
            }
        });
        menu.show();
    }


    public void loadView(CardView cardView) {
        View view = cardView;

        try {
            Log.e("loadView", "true");
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = getBitmapFromView(view);
            view.setDrawingCacheEnabled(false);

            Log.e("loadView", "false");

            File imageFile = createImageFile();

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            Log.e("compress", "compress");
            outputStream.flush();
            outputStream.close();

            Toast.makeText(Quotes_screen_activity.this, "Share", Toast.LENGTH_SHORT).show();

            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".FileProvider", imageFile);
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("image/jpeg");
            sharingIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(sharingIntent, "Share image using"));

        } catch (Exception e) {

            Log.e("Exception", "" + e.toString());
            e.printStackTrace();
        }
    }


    public static File createImageFile() throws IOException {
        // Create an image file name
        Log.e("createImageFile", "createImageFile");
        File storageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TEMP/");
        if (!storageDir.exists()) {
            Log.e("mkdirs", "mkdirs========" + storageDir.getAbsolutePath());
            storageDir.mkdirs();
        }

        File image = File.createTempFile(
                "temp",                   /* prefix */
                ".jpeg",                 /* suffix */
                storageDir                    /* directory */
        );
        Log.e("image", "image===========" + image.getAbsolutePath());
        return image;
    }


    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }



}