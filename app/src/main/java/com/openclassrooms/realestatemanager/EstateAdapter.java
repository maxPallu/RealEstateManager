package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EstateAdapter extends RecyclerView.Adapter<EstateAdapter.EstateViewHolder> {

    private List<EstateItem> mEstateList;

    public static class EstateViewHolder extends RecyclerView.ViewHolder {

        public ImageView estateImage;
        public TextView estateType;
        public TextView estateCity;
        public TextView estatePrice;
        public TextView estateSurface;

        public EstateViewHolder(View itemView) {
            super(itemView);

            estateImage = itemView.findViewById(R.id.estateImage);
            estateType = itemView.findViewById(R.id.estateType);
            estateCity = itemView.findViewById(R.id.estateCity);
            estatePrice = itemView.findViewById(R.id.estatePrice);
        }
    }

    public EstateAdapter(List<EstateItem> estateList) {
        mEstateList = estateList;
    }

    @Override
    public EstateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.estate_item, parent, false);
        EstateViewHolder evh = new EstateViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EstateViewHolder holder, int position) {
        final EstateItem currentItem = mEstateList.get(position);

        holder.estateType.setText(currentItem.getEstateType());
        holder.estateCity.setText(currentItem.getEstateCity());
        holder.estatePrice.setText("$ "+currentItem.getEstatePrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);

                intent.putExtra("estateType", currentItem.getEstateType());
                intent.putExtra("estateCity", currentItem.getEstateCity());
                intent.putExtra("estatePrice", currentItem.getEstatePrice());
                intent.putExtra("estateRoom", currentItem.getEstateRoom());
                intent.putExtra("estateSurface", currentItem.getEstateSurface());
                intent.putExtra("estateAdress", currentItem.getEstateAdress());

                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEstateList.size();
    }
}
