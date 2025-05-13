package com.example.perfume;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class PerfumeSeeMore extends Fragment {

    private RecyclerView recyclerView;
    private EditText searchEditText;
    private PerfumeSeeMoreAdapter adapter;
    private AppDatabase appDatabase;
    private List<PerfumeEntity> perfumeEntityList;
    private List<PerfumeEntity> fullPerfumeList;
    private ShimmerFrameLayout shimmerLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout perfume_more.xml
        View view = inflater.inflate(R.layout.perfume_more, container, false);
         shimmerLayout = view.findViewById(R.id.shimmerLayout);
        // Khởi tạo view
        recyclerView = view.findViewById(R.id.recyclerViewParent);
        // Bắt đầu shimmer
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();
        recyclerView.setVisibility(View.GONE);
        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        perfumeEntityList = new ArrayList<>();
        fullPerfumeList = new ArrayList<>();
        adapter = new com.example.perfume.PerfumeSeeMoreAdapter(getContext(), perfumeEntityList, getParentFragmentManager(),0);
        recyclerView.setAdapter(adapter);
        appDatabase = AppDatabase.getInstance(requireContext());

        loadPerfumesFromRoom();
        setupSearchListener();


        return view;
    }

    private void loadPerfumesFromRoom() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }, 300);
        new Thread(() -> {
            List<PerfumeEntity> perfumes = appDatabase.perfumeDao().getAllPerfumes();
            requireActivity().runOnUiThread(() -> {
                fullPerfumeList.clear();
                fullPerfumeList.addAll(perfumes);
                perfumeEntityList.clear();
                perfumeEntityList.addAll(perfumes);
                adapter.notifyDataSetChanged();

                // Gán animation cho từng item
                Context context = recyclerView.getContext();
                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(
                        requireContext(), R.anim.layout_animation_fade_in);
                recyclerView.setLayoutAnimation(animation);
                recyclerView.scheduleLayoutAnimation();



                // RecyclerView animation tổng thể
                recyclerView.setAlpha(0f);
                recyclerView.setTranslationY(100f);
                recyclerView.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(500)
                        .start();
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
