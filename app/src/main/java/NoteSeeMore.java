package com.example.perfume;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.perfume.NoteSeeMoreAdapter;
import com.example.perfume.PerfumeDatabase;
import com.example.perfume.Note;

public class NoteSeeMore extends AppCompatActivity {

    private RecyclerView recyclerView; // RecyclerView hien thi danh sach thuong hieu
    private EditText searchEditText;   // EditText cho nguoi dung nhap tu khoa tim kiem
    private NoteSeeMoreAdapter adapter; // Adapter gan du lieu vao RecyclerView
    private PerfumeDatabase perfumeDatabase; // Room database
    private List<Note> noteList;
    private List<Note> fullnoteList;  // Danh sach hien thi


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_more); // XML co RecyclerView va EditText

        recyclerView = findViewById(R.id.recyclerViewParent);
        searchEditText = findViewById(R.id.searchEditText); // Phai co trong XML

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 2 cot

        noteList = new ArrayList<>();
        fullnoteList = new ArrayList<>();
        adapter = new NoteSeeMoreAdapter(this, noteList);
        recyclerView.setAdapter(adapter);

        perfumeDatabase = PerfumeDatabase.getInstance(this);

        loadNotessFromRoom();
        setupSearchListener(); // Lang nghe su kien tim kiem
    }

    // Load tat ca brand tu database
    private void loadNotessFromRoom() {
        new Thread(() -> {
            List<Note> notes = perfumeDatabase.noteDao().getAllNotes();

            runOnUiThread(() -> {
                fullnoteList.clear();
                fullnoteList.addAll(notes);

                noteList.clear();
                noteList.addAll(notes);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    // Bat su kien go tu khoa trong EditText
    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // Khi go tu khoa
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString()); // Loc theo tu khoa
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // Loc thuong hieu theo tu khoa
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