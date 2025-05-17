package com.example.perfume.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.perfume.CartManager;
import com.example.perfume.Navigator;
import com.example.perfume.R;
import com.example.perfume.entity.PerfumeEntity;

import java.util.ArrayList;
import java.util.List;

public class PerfumeAdapter extends RecyclerView.Adapter<PerfumeAdapter.ChildViewHolder> {
    private List<PerfumeEntity> childItemList;
    private Context context;
    private int layoutType;
    private Fragment fragment;
    private boolean isMore;

    public PerfumeAdapter(Context context, List<PerfumeEntity> childItemList, int layoutType, Fragment fragment, boolean isMore) {
        this.context = context;
        this.childItemList = childItemList;
        this.fragment = fragment;
        this.isMore = isMore;
        this.layoutType = layoutType;

    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (!isMore) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.perfume_item, parent, false);
        } else {
            if (layoutType == 0) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.perfume_more_item, parent, false);
            } else {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.home_item, parent, false);
            }
        }
        return new ChildViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
      PerfumeEntity perfume = childItemList.get(position);
        holder.name.setText(perfume.getName());
        holder.brand.setText(perfume.getBrand().toUpperCase());

        List<Float> priceList = new ArrayList<>();
        for (String s : perfume.getPrices().split(",")) {
            priceList.add(Float.parseFloat(s.trim()));
        }
        List<Integer> volumeList = new ArrayList<>();
        for (String s : perfume.getVolumes().split(",")) {
            volumeList.add(Integer.parseInt(s.trim()));

            if (!priceList.isEmpty()) {
            float minPrice = priceList.get(0);
            float maxPrice = priceList.get(priceList.size() - 1);
            if(isMore && layoutType==0) {
                holder.price.setText("$" + minPrice + " - $" + maxPrice);
            }
        }

        Glide.with(context).load(perfume.getImg()).into(holder.image);

      }
        holder.concentration.setText(perfume.getConcentration().toUpperCase());
        if (isMore) {
            if (layoutType == 1) {
                if (!priceList.isEmpty()) {
                    Integer minVol = volumeList.get(0);
                    Integer maxVol = volumeList.get(volumeList.size() - 1);
                }
                holder.btnAddCart.setText("ADD TO CART");
                Glide.with(context).load(perfume.getImgs()).into(holder.bannerImage);
            }
        }

        holder.btnAddCart.setOnClickListener(v -> {
            CartManager.showAddToCartDialog(context, perfume, volumeList, priceList);
        });
        holder.itemView.setOnClickListener(v -> {
            Navigator.openPerfumeDetail1((AppCompatActivity) v.getContext(), perfume, fragment);
        });


    }


    @Override
    public int getItemCount() {
        return childItemList.size();
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView name, brand, volume, price, concentration;

        ImageView image,bannerImage;

        Button btnAddCart;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            brand = itemView.findViewById(R.id.brand);
            volume = itemView.findViewById(R.id.volume);
            image = itemView.findViewById(R.id.image);
            price = itemView.findViewById(R.id.price);
            btnAddCart = itemView.findViewById(R.id.btAddCart);
            concentration=itemView.findViewById(R.id.concentration);
            bannerImage=itemView.findViewById(R.id.bannerImage);
        }
    }

}
