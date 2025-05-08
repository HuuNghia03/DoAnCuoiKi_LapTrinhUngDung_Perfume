package com.example.perfume;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PerfumeAdapter extends RecyclerView.Adapter<PerfumeAdapter.ChildViewHolder> {
    private List<com.example.perfume.PerfumeEntity> childItemList;
    private Context context;

    public PerfumeAdapter(Context context, List<com.example.perfume.PerfumeEntity> childItemList) {
        this.context = context;
        this.childItemList = childItemList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        com.example.perfume.PerfumeEntity perfume = childItemList.get(position);
        holder.name.setText(perfume.getName());
       // float price=perfume.getPrice();


        String gender = perfume.getGender();
        if (gender != null) {
            switch (gender) {
                case "Men":
                    holder.gender.setImageResource(R.drawable.ic_male);
                    break;
                case "Women":
                    holder.gender.setImageResource(R.drawable.ic_female);
                    break;
                case "Unisex":
                    holder.gender.setImageResource(R.drawable.ic_unisex);
                    break;
                default:
                    holder.gender.setImageResource(R.drawable.ic_unisex);
                    break;
            }
        } else {
            holder.gender.setImageResource(R.drawable.ic_unisex);
        }
        List<Float> priceList = new ArrayList<>();
        for (String s : perfume.getPrices().split(",")) {
            priceList.add(Float.parseFloat(s.trim()));
        }

        if (!priceList.isEmpty()) {
            float minPrice = priceList.get(0);
            float maxPrice = priceList.get(priceList.size() - 1);
            holder.price.setText("$" + minPrice + " - $" + maxPrice);
        }

        Glide.with(context).load(perfume.getImg()).into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            com.example.perfume.Navigator.openPerfumeDetail((AppCompatActivity) v.getContext(), perfume);
        });
        List<Integer> volumeList = new ArrayList<>();
        for (String s : perfume.getVolumes().split(",")) {
            volumeList.add(Integer.parseInt(s.trim()));
        }
        holder.btnAddCart.setOnClickListener(v -> {
            CartManager.showAddToCartDialog(context, perfume, volumeList, priceList);

        });





    }


    @Override
    public int getItemCount() {
        return childItemList.size();
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView name,year,price;
        RatingBar ratingBar;
        ImageView image,gender;

        Button btnAddCart;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            gender = itemView.findViewById(R.id.gender);
            image = itemView.findViewById(R.id.image);
            price=itemView.findViewById(R.id.price);
            btnAddCart=itemView.findViewById(R.id.btAddCart);
        }
    }

}
