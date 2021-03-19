package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    public void onBindViewHolder(final EstateViewHolder holder, int position) {

        final EstateItem currentItem = mEstateList.get(position);

        holder.estateType.setText(currentItem.getEstateType());
        holder.estateCity.setText(currentItem.getEstateCity());
        holder.estatePrice.setText("$ "+currentItem.getEstatePrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean is_tablet = view.getResources().getBoolean(R.bool.is_tablet);

                if(is_tablet) {
                    Bundle bundle = new Bundle();
                    bundle.putString("estateType", currentItem.getEstateType());
                    bundle.putString("estateCity", currentItem.getEstateCity());
                    bundle.putString("estatePrice", currentItem.getEstatePrice());
                    bundle.putString("estateRoom", currentItem.getEstateRoom());
                    bundle.putString("estateSurface", currentItem.getEstateSurface());
                    bundle.putString("estateAdress", currentItem.getEstateAdress());
                    createFragment(view.getContext(), bundle);

                } else {
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);

                    intent.putExtra("estateType", currentItem.getEstateType());
                    intent.putExtra("estateCity", currentItem.getEstateCity());
                    intent.putExtra("estatePrice", currentItem.getEstatePrice());
                    intent.putExtra("estateRoom", currentItem.getEstateRoom());
                    intent.putExtra("estateSurface", currentItem.getEstateSurface());
                    intent.putExtra("estateAdress", currentItem.getEstateAdress());

                    view.getContext().startActivity(intent);
                }

                holder.itemView.setBackgroundColor(view.getResources().getColor(R.color.background));
            }
        });
    }

    private void createFragment(Context context, Bundle bundle) {
        DetailFragment detailFragment = new DetailFragment();

        detailFragment = (DetailFragment) ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("DetailFragment");

        if(detailFragment != null) {
            detailFragment.setArguments(bundle);
            detailFragment.onResume();
        }

        if(detailFragment == null && ((FragmentActivity) context).findViewById(R.id.frame_layout_detail) != null) {
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(bundle);
            FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.frame_layout_detail, fragment);
            ft.commit();
        }
    }

    @Override
    public int getItemCount() {
        return mEstateList.size();
    }
}
