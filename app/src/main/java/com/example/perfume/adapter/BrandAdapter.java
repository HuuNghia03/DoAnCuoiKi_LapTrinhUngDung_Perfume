package com.example.perfume.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.perfume.Navigator;
import com.example.perfume.R;
import com.example.perfume.entity.BrandEntity;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder> {
    private List<BrandEntity> brandList;
    private Context context;
    private Fragment fragment;
    private boolean isMore;

    public BrandAdapter(Context context, List<BrandEntity> brandList, Fragment fragment, boolean isMore) {
        this.context = context;
        this.brandList = brandList;
        this.fragment=fragment;
        this.isMore=isMore;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(isMore){
            view= LayoutInflater.from(context).inflate(R.layout.brand_more_item, parent, false);
        }  else{
            view= LayoutInflater.from(context).inflate(R.layout.brand_item, parent, false);
        }
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        BrandEntity brand = brandList.get(position);
        holder.textBrand.setText(brand.getName()); // Hiển thị tên brand
        Glide.with(context).load(brand.getLogo()).into(holder.imageBrand); // Tải ảnh bằng Glide
        holder.itemView.setOnClickListener(v -> {
            Navigator.openBrandDetail((AppCompatActivity) v.getContext(), brand, fragment);
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
