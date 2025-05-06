package com.example.perfume;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfume.R;

import java.util.List;

public class BannerItemAdapter extends RecyclerView.Adapter<BannerItemAdapter.BannerViewHolder> {

    private List<com.example.perfume.BannerItem> bannerItemList;

    public BannerItemAdapter(List<com.example.perfume.BannerItem> bannerItemList) {
        this.bannerItemList = bannerItemList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_item, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        com.example.perfume.BannerItem item = bannerItemList.get(position);
        holder.imageBanner.setImageResource(item.getImageResId());
    }

    @Override
    public int getItemCount() {
        return bannerItemList.size();
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBanner;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBanner = itemView.findViewById(R.id.imageBanner);
        }
    }
}
