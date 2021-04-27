package com.openclassrooms.realestatemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context getContext;
    private String uri;
    private ArrayList<ImageItem> mImages = new ArrayList<>();

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ImageViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.imageItem);
        }
    }

    public ImageAdapter(Context context, String imageUri) {
        getContext = context;
        uri = imageUri;
        ImageItem image = new ImageItem(imageUri);
        mImages.add(image);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        ImageViewHolder ivh = new ImageViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageItem imageItem = new ImageItem(uri);
        mImages.add(imageItem);
        holder.imageView.setImageURI(Uri.parse(uri));
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }


}
