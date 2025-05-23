package com.example.perfume.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfume.AppDatabase;
import com.example.perfume.R;
import com.example.perfume.adapter.PerfumeAdapter;
import com.example.perfume.entity.PerfumeEntity;

import java.util.ArrayList;
import java.util.List;

public class BrandDetailPerfumeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<PerfumeEntity> perfumeEntityList;
    private PerfumeAdapter adapter;
    private AppDatabase appDatabase;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brand_detail_perfume, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewPerfume);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);  // Thêm dòng này để cải thiện hiệu suất

        perfumeEntityList = new ArrayList<>();
        adapter = new PerfumeAdapter(
                getContext(),
                perfumeEntityList,
                1,
                getParentFragment(),
                true
        );

        recyclerView.setAdapter(adapter);
        appDatabase = AppDatabase.getInstance(requireContext());
        Bundle args = getArguments();
        String brandName=args.getString("name");
        loadPerfumesFromRoom(brandName);
        return view;
    }

    public void loadPerfumesFromRoom(String brandName) {
        new Thread(() -> {
          List<PerfumeEntity> perfumes = appDatabase.perfumeDao().getPerfumesByBrand(brandName);
            requireActivity().runOnUiThread(() -> {
                perfumeEntityList.clear();
                perfumeEntityList.addAll(perfumes);
                adapter.notifyDataSetChanged();
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
}
