package com.example.perfume;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.perfume.NoteSeeMoreAdapter;
import com.example.perfume.PerfumeDatabase;
import com.example.perfume.Note;

public class NoteSeeMore extends Fragment {

    private RecyclerView recyclerView;
    private EditText searchEditText;
    private NoteSeeMoreAdapter adapter;
    private PerfumeDatabase perfumeDatabase;
    private List<Note> noteList;
    private List<Note> fullnoteList;

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
        adapter = new NoteSeeMoreAdapter(requireContext(), noteList);
        recyclerView.setAdapter(adapter);

        perfumeDatabase = PerfumeDatabase.getInstance(requireContext());

        loadNotessFromRoom();
        setupSearchListener();
    }

    private void loadNotessFromRoom() {
        new Thread(() -> {
            List<Note> notes = perfumeDatabase.noteDao().getAllNotes();

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
        List<Note> filteredList = new ArrayList<>();

        for (Note note : fullnoteList) {
            if (note.getCategory().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(note);
            }
        }

        noteList.clear();
        noteList.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }
}