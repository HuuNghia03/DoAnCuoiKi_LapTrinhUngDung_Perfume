package com.example.perfume;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;

    public NewsAdapter(List<NewsItem> newsList) {
        this.newsList = newsList;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, productLogo;
        TextView productTitle, productDiscription, uploadDate;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productLogo = itemView.findViewById(R.id.productLogo);
            productTitle = itemView.findViewById(R.id.productTitle);
            productDiscription = itemView.findViewById(R.id.productDiscription);
            uploadDate = itemView.findViewById(R.id.uploadDate);
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        com.example.perfume.NewsItem item = newsList.get(position);
        holder.productTitle.setText(item.getTitle());
        holder.productDiscription.setText(item.getDescription());
        holder.uploadDate.setText(item.getUploadDate());
        Glide.with(holder.itemView.getContext())
                .load(item.getImageResId())
                .into(holder.productImage);

        Glide.with(holder.itemView.getContext())
                .load(item.getLogoResId())
                .into(holder.productLogo);

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
