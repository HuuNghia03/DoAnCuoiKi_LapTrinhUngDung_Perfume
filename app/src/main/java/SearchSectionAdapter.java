package com.example.perfume;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchSectionAdapter extends RecyclerView.Adapter<SearchSectionAdapter.SectionViewHolder> {
    private List<com.example.perfume.SearchSection<?>> searchSectionList;
    private Context context;

    public SearchSectionAdapter(Context context, List<com.example.perfume.SearchSection<?>> searchSectionList) {
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
        com.example.perfume.SearchSection<?> section = searchSectionList.get(position);
        holder.title.setText(section.getSectionTitle());

        RecyclerView.Adapter<?> adapter;

        if (!section.getItemList().isEmpty()) {
            Object firstItem = section.getItemList().get(0);
            if (firstItem instanceof com.example.perfume.PerfumeEntity) {
                adapter = new com.example.perfume.PerfumeAdapter(context, (List<com.example.perfume.PerfumeEntity>) section.getItemList());
            } else if (firstItem instanceof com.example.perfume.BrandWithImage) {
                adapter = new com.example.perfume.BrandAdapter(context, (List<com.example.perfume.BrandWithImage>) section.getItemList());
            }else if (firstItem instanceof com.example.perfume.Note) {
                adapter = new com.example.perfume.NoteAdapter(context, (List<com.example.perfume.Note>) section.getItemList());
            }
            else {
                throw new IllegalArgumentException("Unknown type in SearchSection");
            }

            holder.recyclerView.setLayoutManager(
                    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setAdapter(adapter);

            // 👉 Thêm xử lý khi nhấn vào "See more"
            holder.textSeemore.setOnClickListener(v -> {
                if (firstItem instanceof com.example.perfume.PerfumeEntity) {
                    openPerfumeList((List<com.example.perfume.PerfumeEntity>) section.getItemList());
                } else if (firstItem instanceof com.example.perfume.BrandWithImage) {
                    openBrandList((List<com.example.perfume.BrandWithImage>) section.getItemList());
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
        TextView title,textSeemore;
        RecyclerView recyclerView;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            recyclerView = itemView.findViewById(R.id.itemRecyclerView);
            textSeemore=itemView.findViewById(R.id.textSeemore);
        }
    }

    private void openPerfumeList(List<com.example.perfume.PerfumeEntity> perfumes) {
        // Ví dụ mở PerfumeListActivity và truyền danh sách hoặc tiêu đề section
        Intent intent = new Intent(context, com.example.perfume.PerfumeSeeMore.class);
        // Bạn có thể truyền danh sách bằng Parcelable hoặc dùng ViewModel/shared DB
        context.startActivity(intent);
    }

    private void openBrandList(List<com.example.perfume.BrandWithImage> brands) {
        Intent intent = new Intent(context, com.example.perfume.BrandSeeMore.class);
        context.startActivity(intent);
    }
    private void openNoteList(List<com.example.perfume.Note> notes) {
        Intent intent = new Intent(context, com.example.perfume.NoteSeeMore.class);
        context.startActivity(intent);
    }

}
