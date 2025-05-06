package com.example.perfume;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<com.example.perfume.NewsItem> newsList;

    public NewsAdapter(List<com.example.perfume.NewsItem> newsList) {
        this.newsList = newsList;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, productLogo, imageView3;
        TextView productTitle, productDiscription, uploadDate, productDiscover;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productLogo = itemView.findViewById(R.id.productLogo);
            imageView3 = itemView.findViewById(R.id.imageView3);
            productTitle = itemView.findViewById(R.id.productTitle);
            productDiscription = itemView.findViewById(R.id.productDiscription);
            uploadDate = itemView.findViewById(R.id.uploadDate);
            productDiscover = itemView.findViewById(R.id.productDiscover);
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
        holder.productImage.setImageResource(item.getImageResId());
        holder.productLogo.setImageResource(item.getLogoResId());
        holder.productDiscover.setText("DISCOVER MORE");
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
