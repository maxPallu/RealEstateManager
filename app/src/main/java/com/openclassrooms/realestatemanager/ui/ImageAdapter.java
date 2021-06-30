package com.openclassrooms.realestatemanager.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context getContext;
    private String uri;
    private ArrayList<ImageItem> mImages = new ArrayList<>();

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private Context context;

        public ImageViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.imageItem);
            context = view.getContext();

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
       // holder.imageView.setImageURI(Uri.parse(uri));
        Glide.with(holder.context).load(uri).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }


}
