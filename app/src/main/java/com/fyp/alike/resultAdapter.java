package com.fyp.alike;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class resultAdapter extends RecyclerView.Adapter<resultAdapter.CourseViewHolder> {

    private static LinkedList<User> showList;
    resultAdapter(LinkedList<User> showList){
        resultAdapter.showList = showList;
    }

    @NonNull
    @Override
    public resultAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout,parent,false);
        return new CourseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final resultAdapter.CourseViewHolder holder, int position) {

        holder.name.setText(showList.get(position).getName());
        holder.percent.setText(showList.get(position).getTempPercent() +"%");
        holder.percent.setTextColor(Color.GREEN);
        FirebaseOptions opts = FirebaseApp.getInstance().getOptions();
        StorageReference ref = holder.storage.getReferenceFromUrl("gs://"+ opts.getStorageBucket());
        try {
            final File localFile = File.createTempFile("Images", "bmp");
            ref.child("images/"+showList.get(position).getPhotoName()+".jpg")
                    .getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap my_img = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    if (my_img != null) {
                        holder.imageView.setImageBitmap(my_img);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(holder.itemView.getContext(), "error: "+ e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            Toast.makeText(holder.itemView.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return showList.size();
    }
    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView name,percent;
        ImageView imageView;
        FirebaseStorage storage;
        CourseViewHolder(@NonNull final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameListItemLayout);
            percent = itemView.findViewById(R.id.percentListItemLayout);
            imageView = itemView.findViewById(R.id.imgListItemLayout);
            storage = FirebaseStorage.getInstance();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {/*
                    Intent i = new Intent(itemView.getContext(), CourseDetailActivity.class);
                    i.putExtra("ClickedId",showList.get(getAdapterPosition()).getId());
                    itemView.getContext().startActivity(i);*/
                }
            });

        }
    }
}