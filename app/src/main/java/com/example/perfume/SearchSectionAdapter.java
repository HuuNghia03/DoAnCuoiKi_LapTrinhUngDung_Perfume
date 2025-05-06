package com.example.perfume;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchSectionAdapter extends RecyclerView.Adapter<SearchSectionAdapter.SectionViewHolder> {
    private List<com.example.perfume.SearchSection<?>> searchSectionList;
    private Context context;
    private FragmentManager fragmentManager;

    public SearchSectionAdapter(Context context, FragmentManager fragmentManager, List<com.example.perfume.SearchSection<?>> searchSectionList) {
        this.context = context;
        this.fragmentManager = fragmentManager;
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
        com.example.perfume.SearchSection<?> section = searchSectionList.get(position);
        holder.title.setText(section.getSectionTitle());

        RecyclerView.Adapter<?> adapter;

        if (!section.getItemList().isEmpty()) {
            Object firstItem = section.getItemList().get(0);
            if (firstItem instanceof com.example.perfume.PerfumeEntity) {
                adapter = new com.example.perfume.PerfumeAdapter(context, (List<com.example.perfume.PerfumeEntity>) section.getItemList());
            } else if (firstItem instanceof com.example.perfume.BrandEntity) {
                adapter = new com.example.perfume.BrandAdapter(context, (List<com.example.perfume.BrandEntity>) section.getItemList());
            } else if (firstItem instanceof com.example.perfume.Note) {
                adapter = new com.example.perfume.NoteAdapter(context, (List<com.example.perfume.Note>) section.getItemList());
            } else {
                throw new IllegalArgumentException("Unknown type in SearchSection");
            }

            holder.recyclerView.setLayoutManager(
                    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setAdapter(adapter);

            holder.textSeemore.setOnClickListener(v -> {
                if (firstItem instanceof com.example.perfume.PerfumeEntity) {
                    openPerfumeList((List<com.example.perfume.PerfumeEntity>) section.getItemList());
                } else if (firstItem instanceof com.example.perfume.BrandEntity) {
                    openBrandList((List<com.example.perfume.BrandEntity>) section.getItemList());
                } else if (firstItem instanceof com.example.perfume.Note) {
                    openNoteList((List<com.example.perfume.Note>) section.getItemList());
                }


            });
        }
    }

    @Override
    public int getItemCount() {
        return searchSectionList.size();
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView title, textSeemore;
        RecyclerView recyclerView;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            recyclerView = itemView.findViewById(R.id.itemRecyclerView);
            textSeemore = itemView.findViewById(R.id.textSeemore);
        }
    }

    private void openPerfumeList(List<com.example.perfume.PerfumeEntity> perfumes) {

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new com.example.perfume.PerfumeSeeMore())
                .addToBackStack(null)
                .commit();
    }

    private void openBrandList(List<com.example.perfume.BrandEntity> brands) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new com.example.perfume.BrandSeeMore())
                .addToBackStack(null)
                .commit();
    }

    private void openNoteList(List<com.example.perfume.Note> notes) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new com.example.perfume.NoteSeeMore())
                .addToBackStack(null)
                .commit();
    }
}
