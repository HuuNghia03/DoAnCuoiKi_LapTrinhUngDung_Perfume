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
import com.example.perfume.BrandSeeMoreAdapter;
import com.example.perfume.PerfumeDatabase;
import com.example.perfume.BrandWithImage;

public class BrandSeeMore extends AppCompatActivity {

    private RecyclerView recyclerView; // RecyclerView hien thi danh sach thuong hieu
    private EditText searchEditText;   // EditText cho nguoi dung nhap tu khoa tim kiem
    private BrandSeeMoreAdapter adapter; // Adapter gan du lieu vao RecyclerView
    private PerfumeDatabase perfumeDatabase; // Room database
    private List<BrandWithImage> brandList;      // Danh sach hien thi
    private List<BrandWithImage> fullBrandList;  // Danh sach day du tu database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brand_more); // XML co RecyclerView va EditText

        recyclerView = findViewById(R.id.recyclerViewParent);
        searchEditText = findViewById(R.id.searchEditText); // Phai co trong XML

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 cot

        brandList = new ArrayList<>();
        fullBrandList = new ArrayList<>();
        adapter = new BrandSeeMoreAdapter(this, brandList);
        recyclerView.setAdapter(adapter);

        perfumeDatabase = PerfumeDatabase.getInstance(this);

        loadBrandsFromRoom();
        setupSearchListener(); // Lang nghe su kien tim kiem
    }

    // Load tat ca brand tu database
    private void loadBrandsFromRoom() {
        new Thread(() -> {
            List<BrandWithImage> brands = perfumeDatabase.perfumeDao().getAllBrandsWithImage();

            runOnUiThread(() -> {
                fullBrandList.clear();
                fullBrandList.addAll(brands);

                brandList.clear();
                brandList.addAll(brands);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    // Bat su kien go tu khoa trong EditText
    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            // Khi go tu khoa
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBrands(s.toString()); // Loc theo tu khoa
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Loc thuong hieu theo tu khoa
    private void filterBrands(String keyword) {
        List<BrandWithImage> filteredList = new ArrayList<>();

        for (BrandWithImage brand : fullBrandList) {
            if (brand.getBrand().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(brand);
            }
        }

        brandList.clear();
        brandList.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }
}