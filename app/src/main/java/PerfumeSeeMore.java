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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PerfumeSeeMore extends Fragment {

    private RecyclerView recyclerView;
    private EditText searchEditText;
    private com.example.perfume.PerfumeSeeMoreAdapter adapter;
    private com.example.perfume.PerfumeDatabase perfumeDatabase;
    private List<com.example.perfume.PerfumeEntity> perfumeEntityList;
    private List<com.example.perfume.PerfumeEntity> fullPerfumeList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout perfume_more.xml
        View view = inflater.inflate(R.layout.perfume_more, container, false);

        // Khởi tạo view
        recyclerView = view.findViewById(R.id.recyclerViewParent);
        searchEditText = view.findViewById(R.id.searchEditText);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        perfumeEntityList = new ArrayList<>();
        fullPerfumeList = new ArrayList<>();
        adapter = new com.example.perfume.PerfumeSeeMoreAdapter(getContext(),perfumeEntityList, getParentFragmentManager() );
        recyclerView.setAdapter(adapter);

        perfumeDatabase = com.example.perfume.PerfumeDatabase.getInstance(requireContext());

        loadPerfumesFromRoom();
        setupSearchListener();

        return view;
    }

    private void loadPerfumesFromRoom() {
        new Thread(() -> {
            List<com.example.perfume.PerfumeEntity> perfumes = perfumeDatabase.perfumeDao().getAllPerfumes();
            requireActivity().runOnUiThread(() -> {
                fullPerfumeList.clear();
                fullPerfumeList.addAll(perfumes);
                perfumeEntityList.clear();
                perfumeEntityList.addAll(perfumes);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPerfumes(s.toString());
            }
        });
    }

    private void filterPerfumes(String keyword) {
        List<com.example.perfume.PerfumeEntity> filteredList = new ArrayList<>();
        for (com.example.perfume.PerfumeEntity perfume : fullPerfumeList) {
            if (perfume.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(perfume);
            }
        }

        perfumeEntityList.clear();
        perfumeEntityList.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }
}
