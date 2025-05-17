package com.example.perfume.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfume.AppDatabase;
import com.example.perfume.R;
import com.example.perfume.adapter.NoteAdapter;
import com.example.perfume.entity.NoteEntity;

import java.util.ArrayList;
import java.util.List;

public class NoteSeeMoreFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText searchEditText;
    private NoteAdapter adapter;
    private AppDatabase appDatabase;
    private List<NoteEntity> noteList;
    private List<NoteEntity> fullnoteList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.note_more, container, false); // Dùng layout hiện tại
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewParent);
        searchEditText = view.findViewById(R.id.searchEditText);

        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3)); // 3 cột

        noteList = new ArrayList<>();
        fullnoteList = new ArrayList<>();
        adapter = new NoteAdapter(requireContext(), noteList, this, true);
        recyclerView.setAdapter(adapter);

        appDatabase = AppDatabase.getInstance(requireContext());

        loadNotessFromRoom();
        setupSearchListener();
    }

    private void loadNotessFromRoom() {
        new Thread(() -> {
            List<NoteEntity> notes = appDatabase.noteDao().getAllNotes();

            requireActivity().runOnUiThread(() -> {
                fullnoteList.clear();
                fullnoteList.addAll(notes);

                noteList.clear();
                noteList.addAll(notes);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString());
            }
        });
    }

    private void filterNotes(String keyword) {
        List<NoteEntity> filteredList = new ArrayList<>();

        for (NoteEntity note : fullnoteList) {
            if (note.getNote().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(note);
            }
        }

        noteList.clear();
        noteList.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }
}