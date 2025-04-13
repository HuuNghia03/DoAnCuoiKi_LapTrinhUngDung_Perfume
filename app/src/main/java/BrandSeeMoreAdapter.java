package com.example.perfume;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BrandSeeMoreAdapter extends RecyclerView.Adapter<BrandSeeMoreAdapter.BrandViewHolder> {
    private List<com.example.perfume.BrandWithImage> brandList;
    private Context context;

    public BrandSeeMoreAdapter(Context context, List<com.example.perfume.BrandWithImage> brandList) {
        this.context = context;
        this.brandList = brandList;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.brand_more_item, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        com.example.perfume.BrandWithImage brand = brandList.get(position);
        holder.textBrand.setText(brand.getBrand()); // Hiển thị tên brand
        Glide.with(context).load(brand.getBrandImg()).into(holder.imageBrand); // Tải ảnh bằng Glide
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBrand;
        TextView textBrand;

        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBrand = itemView.findViewById(R.id.imageBrand);
            textBrand = itemView.findViewById(R.id.textBrand);
        }
    }
}
