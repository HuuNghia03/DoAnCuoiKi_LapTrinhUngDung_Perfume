package com.example.perfume.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.perfume.Navigator;
import com.example.perfume.entity.NoteEntity;
import com.example.perfume.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.BrandViewHolder> {
    private List<NoteEntity> noteList;
    private Context context;
    private Fragment fragment;
    private boolean isMore;

    public NoteAdapter(Context context, List<NoteEntity> noteList, Fragment fragment, boolean isMore) {
        this.context = context;
        this.noteList = noteList;
        this.fragment=fragment;
        this.isMore=isMore;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(isMore){
             view = LayoutInflater.from(context).inflate(R.layout.note_more_item, parent, false);
        } else {
             view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        }
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
       NoteEntity note = noteList.get(position);
        holder.textNote.setText(note.getCategory()); // Hiển thị tên brand
        Glide.with(context).load(note.getImageUrl()).into(holder.imageNote); // Tải ảnh bằng Glide
        holder.itemView.setOnClickListener(v -> {
            Navigator.openNoteDetail((AppCompatActivity) v.getContext(), note, fragment);
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
