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
import com.example.perfume.adapter.BrandAdapter;
import com.example.perfume.entity.BrandEntity;

import java.util.ArrayList;
import java.util.List;

public class BrandSeeMoreFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText searchEditText;
    private BrandAdapter adapter;
    private AppDatabase appDatabase;
    private List<BrandEntity> brandList;
    private List<BrandEntity> fullBrandList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.brand_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewParent);
        searchEditText = view.findViewById(R.id.searchEditText);

        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        brandList = new ArrayList<>();
        fullBrandList = new ArrayList<>();
        adapter = new BrandAdapter(requireContext(), brandList, this, true);
        recyclerView.setAdapter(adapter);

        appDatabase = AppDatabase.getInstance(requireContext());

        loadBrandsFromRoom();
        setupSearchListener();
    }

    private void loadBrandsFromRoom() {
        new Thread(() -> {
            List<BrandEntity> brands = appDatabase.BrandDao().getAllBrandsWithImage();

            requireActivity().runOnUiThread(() -> {
                fullBrandList.clear();
                fullBrandList.addAll(brands);

                brandList.clear();
                brandList.addAll(brands);
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
                filterBrands(s.toString());
            }
        });
    }

    private void filterBrands(String keyword) {
        List<BrandEntity> filteredList = new ArrayList<>();

        for (BrandEntity brand : fullBrandList) {
            if (brand.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(brand);
            }
        }

        brandList.clear();
        brandList.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }
}