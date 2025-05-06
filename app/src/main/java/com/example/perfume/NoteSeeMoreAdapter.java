package com.example.perfume;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NoteSeeMoreAdapter extends RecyclerView.Adapter<NoteSeeMoreAdapter.BrandViewHolder> {
    private List<com.example.perfume.Note> noteList;
    private Context context;

    public NoteSeeMoreAdapter(Context context, List<com.example.perfume.Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_more_item, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        com.example.perfume.Note note = noteList.get(position);
        holder.textNote.setText(note.getCategory()); // Hiển thị tên brand
        Glide.with(context).load(note.getImageUrl()).into(holder.imageNote); // Tải ảnh bằng Glide
        holder.itemView.setOnClickListener(v -> {
            com.example.perfume.Navigator.openNoteDetail((AppCompatActivity) v.getContext(), note);
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder {
        ImageView imageNote;
        TextView textNote;

        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);
            imageNote = itemView.findViewById(R.id.imageNote);
            textNote = itemView.findViewById(R.id.textNote);
        }
    }
}
