package com.example.perfume;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder> {
    private List<com.example.perfume.BrandEntity> brandList;
    private Context context;

    public BrandAdapter(Context context, List<com.example.perfume.BrandEntity> brandList) {
        this.context = context;
        this.brandList = brandList;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.brand_item, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        com.example.perfume.BrandEntity brand = brandList.get(position);
        holder.textBrand.setText(brand.getName()); // Hiển thị tên brand
        Glide.with(context).load(brand.getLogo()).into(holder.imageBrand); // Tải ảnh bằng Glide
        holder.itemView.setOnClickListener(v -> {
            com.example.perfume.Navigator.openBrandDetail((AppCompatActivity) v.getContext(), brand);
        });
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
