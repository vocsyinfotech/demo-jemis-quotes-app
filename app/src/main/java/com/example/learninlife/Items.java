package com.example.learninlife;

public class Items {
    int item_id;
    String item_Name;
    int imgId;

    public Items(int item_id, String item_Name, int imgId) {
        this.item_id = item_id;
        this.item_Name = item_Name;
        this.imgId = imgId;
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
    public int getImageId() {
        return imgId;
    }

    public void setBitmap_img(int imgId) {
        this.imgId = imgId;
    }

}
