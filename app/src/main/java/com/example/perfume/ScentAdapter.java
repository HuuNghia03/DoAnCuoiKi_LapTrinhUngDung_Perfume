package com.example.perfume;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScentAdapter extends RecyclerView.Adapter<ScentAdapter.ScentViewHolder> {

    private List<ScentEntity> scentList;

    public ScentAdapter(List<ScentEntity> scentList) {
        this.scentList = scentList;
    }

    public static class ScentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageScent;
        TextView textTitle, textSubTitle;

        public ScentViewHolder(View itemView) {
            super(itemView);
            imageScent = itemView.findViewById(R.id.imageScent);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubTitle = itemView.findViewById(R.id.textSubTitle);
        }
    }

    @Override
    public ScentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scent, parent, false);
        return new ScentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScentViewHolder holder, int position) {
        ScentEntity scent = scentList.get(position);
        holder.imageScent.setImageResource(scent.getImageResId());
        holder.textTitle.setText(scent.getTitle());
        holder.textSubTitle.setText(scent.getDescription());
    }

    @Override
    public int getItemCount() {
        return scentList.size();
    }
}
