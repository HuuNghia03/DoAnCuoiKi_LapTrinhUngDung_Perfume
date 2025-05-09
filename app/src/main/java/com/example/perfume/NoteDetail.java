package com.example.perfume;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class NoteDetail extends Fragment {

    private TextView tvName, tvDescription;
    private ImageView imageNote;
    private PerfumeSeeMoreAdapter adapter;
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
            String category = args.getString("category");
            String notes = args.getString("notes");
            String image = args.getString("image");
            String description = args.getString("description");

            // Set UI elements
            tvName.setText(category);
            tvDescription.setText(description);
            Glide.with(this).load(image).into(imageNote);

            // Split the notes string into an array (handling spaces after commas)
            String[] noteArray = notes.split(",\\s*");

            loadPerfumesFromRoom(noteArray);
        }


        return view;
    }
    public void loadPerfumesFromRoom(String[] noteArray) {
        new Thread(() -> {
            Set<PerfumeEntity> result = new HashSet<>();
            for (String note : noteArray) {
                // Query the database for each note in Top Note, Heart Note, or Base Note
                List<PerfumeEntity> perfumesByTop = appDatabase.perfumeDao().getPerfumesByTopNote(note);
                List<PerfumeEntity> perfumesByHeart = appDatabase.perfumeDao().getPerfumesByHeartNote(note);
                List<PerfumeEntity> perfumesByBase = appDatabase.perfumeDao().getPerfumesByBaseNote(note);

                // Add the results to the HashSet (to remove duplicates)
                result.addAll(perfumesByTop);
                result.addAll(perfumesByHeart);
                result.addAll(perfumesByBase);
            }
            // Convert the Set to a List and update the adapter on the main thread
            perfumeEntityList = new ArrayList<>(result);

            // Update RecyclerView on the main thread
            requireActivity().runOnUiThread(() -> {
                adapter = new PerfumeSeeMoreAdapter(getContext(), perfumeEntityList, getParentFragmentManager(), 1);
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
