package com.example.learninlife;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class category_adapter extends RecyclerView.Adapter<category_adapter.myClass> {
    MainActivity mainActivity;
    ArrayList<items> item_list;
    ArrayList<HoriModel> horiModels = new ArrayList<>();

    public category_adapter(MainActivity mainActivity, ArrayList<items> item_list, ArrayList<HoriModel> horiModels) {
        Log.e("log1", "category_adapter: itemlist.size(): "+item_list.size() );
        this.mainActivity = mainActivity;
        this.item_list = item_list;
        this.horiModels = horiModels;
        Log.e("TAG", "category_adapter: this.item_list.size(): "+this.item_list.size() );
    }

    @NonNull
    @Override
    public category_adapter.myClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mainActivity).inflate(R.layout.vartiview,parent,false);
        myClass m=new myClass(view);
        return m;
    }

    @Override
    public void onBindViewHolder(@NonNull category_adapter.myClass holder, int position) {
        Log.e("TAG", "onBindViewHolder: position: "+position );


        Log.e("log", "onBindViewHolder: "+item_list.get(position).item_Name);
        holder.itemImage.setImageResource(horiModels.get(position).getImage());
        holder.itemText.setText(item_list.get(position).item_Name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.sendPosition(position);
            }
        });

//        holder.itemImage.setImageBitmap(item_list.get(position).getBitmap_img());

    }

    @Override
    public int getItemCount() {

        Log.e("TAG", "getItemCount: "+item_list.size() );
        return item_list.size();
    }

    public class myClass extends RecyclerView.ViewHolder {
        CircleImageView itemImage;
        TextView itemText;
        public myClass(@NonNull View itemView) {
            super(itemView);
            itemText=itemView.findViewById(R.id.title2);
            itemImage=itemView.findViewById(R.id.img2);
        }
    }
}
