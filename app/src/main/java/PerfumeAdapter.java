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
        holder.brand.setText(perfume.getBrand());
        Integer year=perfume.getYear();
        String gender = perfume.getGender();
        if (gender != null) {
            switch (gender) {
                case "Men":
                    holder.gender.setImageResource(R.drawable.ic_male); // thay icon theo bạn có
                    break;
                case "Women":
                    holder.gender.setImageResource(R.drawable.ic_female);
                    break;
                case "Unisex":
                    holder.gender.setImageResource(R.drawable.ic_unisex);
                    break;
                default:
                    holder.gender.setImageResource(R.drawable.ic_unisex); // icon mặc định nếu không khớp
                    break;
            }
        } else {
            holder.gender.setImageResource(R.drawable.ic_unisex);
        }
        if(year!=null){
            holder.year.setText(String.valueOf(year));
        } else {
            holder.year.setText("2025");
        }


        Glide.with(context).load(perfume.getImg()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return childItemList.size();
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView name, brand,year;
        ImageView image,gender;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            brand = itemView.findViewById(R.id.brand);
            gender = itemView.findViewById(R.id.gender);
            year = itemView.findViewById(R.id.year);
            image = itemView.findViewById(R.id.image); // đảm bảo ID là đúng từ layout search_item.xml
        }
    }

}
