package com.example.learninlife;

import android.graphics.Bitmap;

public class items {
    int item_id;
    String item_Name;
    Bitmap bitmap_img;

    public items(int item_id, String item_Name, Bitmap bitmap_img) {
        this.item_id = item_id;
        this.item_Name = item_Name;
        this.bitmap_img = bitmap_img;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_Name() {
        return item_Name;
    }

    public void setItem_Name(String item_Name) {
        this.item_Name = item_Name;
    }
    public Bitmap getBitmap_img() {
        return bitmap_img;
    }

    public void setBitmap_img(Bitmap bitmap_img) {
        this.bitmap_img = bitmap_img;
    }

}
