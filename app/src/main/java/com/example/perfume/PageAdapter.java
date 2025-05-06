package com.example.perfume;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class PageAdapter extends RecyclerView.Adapter<PageAdapter.ViewHolder> {
    List<Integer> layouts;

    public PageAdapter(List<Integer> layouts) {
        this.layouts = layouts;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layouts.get(viewType), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { }

    @Override
    public int getItemCount() {
        return layouts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
