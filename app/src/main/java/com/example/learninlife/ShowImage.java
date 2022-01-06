package com.example.learninlife;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class ShowImage extends AppCompatActivity {
    private static final int REQUEST_CODE = 100;
    ImageView imageView, download;
    ArrayList<HoriModel> models = new ArrayList<>();
    OutputStream outputStream;
    int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image2);

        image = getIntent().getIntExtra("image_pos", 0);

        Log.d("on", "onCreate: " + image);

        imageView = findViewById(R.id.image);
        download = findViewById(R.id.download);

        imageView.setImageResource(image);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ShowImage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    saveImage();
                } else {
                    ActivityCompat.requestPermissions(ShowImage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                }
            }
        });
    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        if (requestCode == REQUEST_CODE) {
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                saveImage();
//
//            } else {
//                Toast.makeText(ShowImage.this, "Please provide the required permissions", Toast.LENGTH_SHORT).show();
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    private void saveImage() {

        ContextWrapper wrapper = new ContextWrapper(ShowImage.this);

        // 1 BitmapDrawable drawable = (BitmapDrawable) image1.getDrawable();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), image);

        // 2 File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SaveImage");

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SaveImage");
        File file = new File(dir, System.currentTimeMillis() + ".jpg");
        if (!dir.exists()) {
            dir.mkdir();
        }

        Log.e("TAG", "PATH: " + file.getAbsolutePath());

        try {
            outputStream = new FileOutputStream(file);

        } catch (FileNotFoundException e) {
            Log.e("TAG", "saveImage: " + e.getMessage());
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        Toast.makeText(ShowImage.this, "SuccessFully Saved", Toast.LENGTH_SHORT).show();

        try {

            outputStream.flush();

        } catch (IOException e) {
            Log.e("TAG2", "saveImage: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            outputStream.close();
            bitmap.recycle();

        } catch (IOException e) {
            Log.e("TAG3", "saveImage: " + e.getMessage());
            e.printStackTrace();
        }
    }
}