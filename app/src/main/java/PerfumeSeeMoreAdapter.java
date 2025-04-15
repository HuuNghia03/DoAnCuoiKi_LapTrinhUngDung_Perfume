package com.example.perfume;

import static android.text.TextUtils.replace;

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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PerfumeSeeMoreAdapter extends RecyclerView.Adapter<PerfumeSeeMoreAdapter.ChildViewHolder> {
    private List<com.example.perfume.PerfumeEntity> childItemList;
    private Context context;
    private FragmentManager fragmentManager;

    public PerfumeSeeMoreAdapter(Context context, List<com.example.perfume.PerfumeEntity> childItemList, FragmentManager fragmentManager) {
        this.context = context;
        this.childItemList = childItemList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.perfume_more_item, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        com.example.perfume.PerfumeEntity perfume = childItemList.get(position);
        holder.name.setText(perfume.getName());
        holder.brand.setText(perfume.getBrand());

        Integer year = perfume.getYear();
        String gender = perfume.getGender();

        // Gán icon giới tính
        if (gender != null) {
            switch (gender) {
                case "Men":
                    holder.gender.setImageResource(R.drawable.ic_male);
                    break;
                case "Women":
                    holder.gender.setImageResource(R.drawable.ic_female);
                    break;
                case "Uniex":
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

        // Gán năm phát hành
        holder.year.setText(year != null ? String.valueOf(year) : "2025");

        // Load hình ảnh
        Glide.with(context).load(perfume.getImg()).into(holder.image);

        // Xử lý khi click vào item
        // 👇 Thêm xử lý click tại đây
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("perfumeName", perfume.getName());
            bundle.putString("brand", perfume.getBrand());
            bundle.putString("gender", perfume.getGender());
            bundle.putString("img", perfume.getImg());
            bundle.putString("imgs", perfume.getImgs());
            bundle.putFloat("price", perfume.getPrice());

            bundle.putInt("year", perfume.getYear());
            bundle.putString("olfactory", perfume.getOlfactory());
            bundle.putString("top", perfume.getTop());
            bundle.putString("heart", perfume.getHeart());
            bundle.putString("base", perfume.getBase());
            bundle.putString("description", perfume.getDescription());
            bundle.putString("perfumer", perfume.getDesigners());

            Fragment perfumeDetail = new com.example.perfume.PerfumeDetail();
            perfumeDetail.setArguments(bundle);

            ((AppCompatActivity) v.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, perfumeDetail)
                    .addToBackStack(null)
                    .commit();
        });

    }

    @Override
    public int getItemCount() {
        return childItemList.size();
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView name, brand, year;
        ImageView image, gender;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            brand = itemView.findViewById(R.id.brand);
            gender = itemView.findViewById(R.id.gender);
            year = itemView.findViewById(R.id.year);
            image = itemView.findViewById(R.id.image); // ID từ layout perfume_more_item.xml
        }
    }
}
