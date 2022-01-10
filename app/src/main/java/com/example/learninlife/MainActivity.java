package com.example.learninlife;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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
    NewCategoryAdapter adapter;
    SparkButton favourite;
    EditText edit_query;
    ArrayList<Items> item_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_query = findViewById(R.id.edit_query);

        edit_query.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        favourite = findViewById(R.id.favourite);

//        toggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.app_name, R.string.app_name);
//        toolbar.setNavigationIcon(R.color.black);
//        toggle.setDrawerIndicatorEnabled(false);


        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Quotes_screen_activity.class);
                intent.putExtra("ArrayFoundOrNot", 1);
                startActivity(intent);
            }
        });


//        mDatBaseHelper = new DatBaseHelper(this);

        recyclerView = findViewById(R.id.vartiview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        myDatabase = new My_database(MainActivity.this);
        myDatabase.openDataBase();
        try {
            item_list = myDatabase.getDetails();
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new NewCategoryAdapter(MainActivity.this, item_list, new NewCategoryAdapter.MyClick() {
            @Override
            public void onClick(int position, Items items) {
                startActivity(new Intent(MainActivity.this, Quotes_screen_activity.class).putExtra("category_id", item_list.get(position).item_id));
            }
        });
        recyclerView.setAdapter(adapter);

        edit_query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    filter(edit_query.getText().toString());
                    return true;
                }
                return false;
            }
        });

        edit_query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }

        });

    }


    public void filter(String text) {
        ArrayList<Items> temp = new ArrayList();
        for (Items mItem : item_list) {
            if (mItem.getItem_Name().toLowerCase().contains(text.toLowerCase())) {
                temp.add(mItem);
            }
        }
        adapter.updateList(temp);
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
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}