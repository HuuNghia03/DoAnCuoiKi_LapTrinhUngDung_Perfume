package com.example.perfume.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.perfume.AppDatabase;
import com.example.perfume.R;
import com.example.perfume.adapter.PerfumeAdapter;
import com.example.perfume.entity.NoteEntity;
import com.example.perfume.entity.PerfumeEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NoteDetailFragment extends Fragment {

    private TextView tvName, tvDescription;
    private ImageView imageNote;
    private PerfumeAdapter adapter;
    private List<PerfumeEntity> perfumeEntityList;
    private AppDatabase appDatabase;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_detail, container, false);
        ImageView btnBack = view.findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(v -> {
            // Quay lại fragment trước đó
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerViewNote);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1)); // Use LinearLayoutManager if you want a list
        tvName = view.findViewById(R.id.tvName);
        tvDescription = view.findViewById(R.id.tvDescription);
        imageNote = view.findViewById(R.id.imageNote);

        // Get instance of the database
        appDatabase = AppDatabase.getInstance(requireContext());

        // Get arguments passed to the Fragment
        Bundle args = getArguments();
        if (args != null) {
            int noteId = getArguments().getInt("noteId");
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                NoteEntity note = AppDatabase.getInstance(requireContext()).noteDao().findNoteByNoteId(noteId);

                if (note != null) {
                    requireActivity().runOnUiThread(() -> {
                        // Load UI
                        tvName.setText(note.getNote());
                        tvDescription.setText(note.getDescription());
                        Glide.with(requireContext()).load(note.getImageUrl()).into(imageNote);
                        loadPerfumesFromRoom(noteId);
                    });
                }
            });

        }


        return view;
    }
    public void loadPerfumesFromRoom(int noteId) {
        new Thread(() -> {
            Set<PerfumeEntity> result = new HashSet<>();

                // Query the database for each note in Top Note, Heart Note, or Base Note
                List<PerfumeEntity> perfumesByTop = appDatabase.perfumeDao().getPerfumesByTopNoteId(noteId);
                List<PerfumeEntity> perfumesByHeart = appDatabase.perfumeDao().getPerfumesByHeartNoteId(noteId);
                List<PerfumeEntity> perfumesByBase = appDatabase.perfumeDao().getPerfumesByBaseNoteId(noteId);

                // Add the results to the HashSet (to remove duplicates)
                result.addAll(perfumesByTop);
                result.addAll(perfumesByHeart);
                result.addAll(perfumesByBase);

            // Convert the Set to a List and update the adapter on the main thread
            perfumeEntityList = new ArrayList<>(result);

            // Update RecyclerView on the main thread
            requireActivity().runOnUiThread(() -> {
                adapter = new PerfumeAdapter(getContext(), perfumeEntityList, 1, this, true);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                recyclerView.setAlpha(0f);
                recyclerView.setTranslationY(100f);
                recyclerView.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(500)
                        .start();// Notify the adapter of data change
            });
        }).start();
    }
}
