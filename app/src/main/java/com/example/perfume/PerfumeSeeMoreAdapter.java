package com.example.perfume;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PerfumeSeeMoreAdapter extends RecyclerView.Adapter<PerfumeSeeMoreAdapter.ChildViewHolder> {
    private List<com.example.perfume.PerfumeEntity> childItemList;
    private Context context;
    private FragmentManager fragmentManager;
    private int layoutType;
    private int lastPosition = -1;

    public PerfumeSeeMoreAdapter(Context context, List<com.example.perfume.PerfumeEntity> childItemList, FragmentManager fragmentManager, int layoutType) {
        this.context = context;
        this.childItemList = childItemList;
        this.fragmentManager = fragmentManager;
        this.layoutType = layoutType;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (layoutType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.perfume_more_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.brand_perfume_item, parent, false);
        }

        return new ChildViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        com.example.perfume.PerfumeEntity perfume = childItemList.get(position);
        // Cập nhật tên, thương hiệu, giá cả và năm phát hành
        holder.name.setText(perfume.getName());
        holder.brand.setText(perfume.getBrand());
        List<Float> priceList = new ArrayList<>();
        for (String s : perfume.getPrices().split(",")) {
            priceList.add(Float.parseFloat(s.trim()));
        }

        if (!priceList.isEmpty()) {
            float minPrice = priceList.get(0);
            float maxPrice = priceList.get(priceList.size() - 1);
            holder.price.setText("$" + minPrice + " - $" + maxPrice);
        }

        if(layoutType==1){
            List<Integer> volumeList = new ArrayList<>();
            for (String s : perfume.getVolumes().split(",")) {
                volumeList.add(Integer.parseInt(s.trim()));
            }

            if (!priceList.isEmpty()) {
                Integer minVol = volumeList.get(0);
                Integer maxVol = volumeList.get(volumeList.size() - 1);
                holder.volume.setText("Vol: "+minVol + " - " + maxVol+ "ml");
            }
        }
        // Cập nhật đánh giá

        // Kiểm tra và gán icon giới tính
        String gender = perfume.getGender();
        if (TextUtils.isEmpty(gender)) {
            holder.gender.setImageResource(R.drawable.ic_unisex);
        } else {
            switch (gender) {
                case "Men":
                    holder.gender.setImageResource(R.drawable.ic_male);
                    break;
                case "Women":
                    holder.gender.setImageResource(R.drawable.ic_female);
                    break;
                case "Unisex":
                case "Uniex":
                    holder.gender.setImageResource(R.drawable.ic_unisex);
                    break;
                default:
                    holder.gender.setImageResource(R.drawable.ic_unisex);
                    break;
            }
        }

        List<Integer> volumeList = new ArrayList<>();
        for (String s : perfume.getVolumes().split(",")) {
            volumeList.add(Integer.parseInt(s.trim()));
        }
        // Load ảnh
        Glide.with(context).load(perfume.getImg()).into(holder.image);

        // Xử lý khi click vào item
        holder.itemView.setOnClickListener(v -> {
           Navigator.openPerfumeDetail((AppCompatActivity) v.getContext(), perfume);
        });
        holder.btnAddCart.setOnClickListener(v -> {
            CartManager.showAddToCartDialog(context, perfume, volumeList, priceList);
        });

    }


    @Override
    public int getItemCount() {
        return childItemList.size();
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView name, brand, volume, price;
        RatingBar ratingBar;
        ImageView image, gender;
        Button btnAddCart;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            brand = itemView.findViewById(R.id.brand);
            gender = itemView.findViewById(R.id.gender);
           volume = itemView.findViewById(R.id.volume);
            image = itemView.findViewById(R.id.image);
            price = itemView.findViewById(R.id.price);
            btnAddCart=itemView.findViewById(R.id.btAddCart);
        }
    }
}
