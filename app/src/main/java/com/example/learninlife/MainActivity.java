package com.example.learninlife;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.varunest.sparkbutton.SparkButton;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView horizontalview, recyclerView;
    My_database myDatabase;
    category_adapter adapter;
    SparkButton favourite;
    ArrayList<items> item_list = new ArrayList<>();
    ArrayList<HoriModel> models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        favourite = findViewById(R.id.favourite);
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Quotes_screen_activity.class);
                intent.putExtra("ArrayFoundOrNot", 1);
                startActivity(intent);
                finish();
            }
        });

        for (int i : new int[]{R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6, R.drawable.img7, R.drawable.img8, R.drawable.img9, R.drawable.img10, R.drawable.img11, R.drawable.img12, R.drawable.img13, R.drawable.img14, R.drawable.img15, R.drawable.img16, R.drawable.img17, R.drawable.img18, R.drawable.img19, R.drawable.img20, R.drawable.img21, R.drawable.img22, R.drawable.img23, R.drawable.img24, R.drawable.img25, R.drawable.img26, R.drawable.img27, R.drawable.img28, R.drawable.img29, R.drawable.img30, R.drawable.img31, R.drawable.img32, R.drawable.img33, R.drawable.img34, R.drawable.img35, R.drawable.img36}) {
            models.add(new HoriModel(i));
        }
//        mDatBaseHelper = new DatBaseHelper(this);
        horizontalview = findViewById(R.id.horizontalview);
        recyclerView = findViewById(R.id.vartiview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        horizontalview.setAdapter(new HoriAdapter(this, models));

        myDatabase = new My_database(MainActivity.this);
        myDatabase.openDataBase();
        try {
            item_list = myDatabase.getDetails();
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new category_adapter(MainActivity.this, item_list, models);
        recyclerView.setAdapter(adapter);

    }

    public void sendPosition(int position) {
        Intent intent = new Intent(MainActivity.this, Quotes_screen_activity.class);
        intent.putExtra("category_id", item_list.get(position).item_id);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}