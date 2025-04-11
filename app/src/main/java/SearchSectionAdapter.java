package com.example.perfume;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchSectionAdapter extends RecyclerView.Adapter<SearchSectionAdapter.SectionViewHolder> {
    private List<com.example.perfume.SearchSection> searchSectionList;
    private Context context; // Thêm context

    public SearchSectionAdapter(Context context, List<com.example.perfume.SearchSection> searchSectionList) {
        this.context = context;
        this.searchSectionList = searchSectionList;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        com.example.perfume.SearchSection section = searchSectionList.get(position);
        holder.title.setText(section.getSectionTitle());

        // 👇 Sửa ở đây: truyền context và list
        com.example.perfume.PerfumeAdapter itemAdapter = new com.example.perfume.PerfumeAdapter(context, section.getItemList());

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(itemAdapter);
    }

    @Override
    public int getItemCount() {
        return searchSectionList.size();
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView recyclerView;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            recyclerView = itemView.findViewById(R.id.itemRecyclerView);
        }
    }
}

