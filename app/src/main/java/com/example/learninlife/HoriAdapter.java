package com.example.learninlife;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HoriAdapter extends RecyclerView.Adapter<HoriAdapter.myclass> {
    MainActivity mainActivity;
    ArrayList<HoriModel> horiModels;

    public HoriAdapter(MainActivity mainActivity, ArrayList<HoriModel> horiModels) {
        this.mainActivity = mainActivity;
        this.horiModels = horiModels;
    }

    @NonNull
    @Override
    public myclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myclass(LayoutInflater.from(mainActivity).inflate(R.layout.horizontal,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myclass holder, @SuppressLint("RecyclerView") int position) {
        holder.imageView.setImageResource(horiModels.get(position).getImage());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity,ShowImage.class);
                intent.putExtra("image_pos",horiModels.get(position).getImage());
                mainActivity.startActivity(intent);
                Toast.makeText(mainActivity, "Success :-", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return horiModels.size();
    }

    public class myclass extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        public myclass(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imagehori);
        }
    }
}
