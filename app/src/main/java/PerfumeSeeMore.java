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
import com.example.perfume.PerfumeSeeMoreAdapter;
import com.example.perfume.PerfumeDatabase;
import com.example.perfume.PerfumeEntity;

public class PerfumeSeeMore extends AppCompatActivity {

    private RecyclerView recyclerView; // RecyclerView hien thi danh sach nuoc hoa
    private EditText searchEditText;   // EditText de nguoi dung nhap tu khoa tim kiem
    private PerfumeSeeMoreAdapter adapter; // Adapter de gan du lieu vao RecyclerView
    private PerfumeDatabase perfumeDatabase; // CSDL Room chua danh sach nuoc hoa
    private List<PerfumeEntity> perfumeEntityList; // Danh sach nuoc hoa dang hien thi
    private List<PerfumeEntity> fullPerfumeList;   // Danh sach tat ca nuoc hoa tu CSDL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfume_more); // Gan layout perfume_more.xml

        // Khoi tao cac view
        recyclerView = findViewById(R.id.recyclerViewParent);
        searchEditText = findViewById(R.id.searchEditText);

        // Thiet lap layout dang Grid cho RecyclerView (2 cot)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Khoi tao danh sach va adapter
        perfumeEntityList = new ArrayList<>();
        fullPerfumeList = new ArrayList<>();
        adapter = new PerfumeSeeMoreAdapter(this, perfumeEntityList);
        recyclerView.setAdapter(adapter); // Gan adapter cho RecyclerView

        // Lay instance cua database
        perfumeDatabase = PerfumeDatabase.getInstance(this);

        // Tai du lieu nuoc hoa tu Room vao danh sach
        loadPerfumesFromRoom();

        // Bat su kien tim kiem theo tu khoa
        setupSearchListener();
    }

    // Ham load tat ca nuoc hoa tu Room Database
    private void loadPerfumesFromRoom() {
        new Thread(() -> {
            // Goi DAO de lay danh sach tat ca nuoc hoa
            List<PerfumeEntity> perfumes = perfumeDatabase.perfumeDao().getAllPerfumes();

            // Cap nhat du lieu tren giao dien
            runOnUiThread(() -> {
                fullPerfumeList.clear();              // Xoa danh sach cu
                fullPerfumeList.addAll(perfumes);     // Them toan bo vao danh sach goc
                perfumeEntityList.clear();            // Xoa danh sach dang hien thi
                perfumeEntityList.addAll(perfumes);   // Hien thi toan bo luc dau
                adapter.notifyDataSetChanged();       // Cap nhat giao dien
            });
        }).start();
    }

    // Bat su kien thay doi noi dung EditText de thuc hien tim kiem
    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            // Khi nguoi dung dang go tim kiem
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPerfumes(s.toString()); // Loc du lieu theo tu khoa
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Ham loc danh sach nuoc hoa dua theo tu khoa
    private void filterPerfumes(String keyword) {
        List<PerfumeEntity> filteredList = new ArrayList<>();

        // Duyet qua danh sach tat ca va loc theo ten
        for (PerfumeEntity perfume : fullPerfumeList) {
            if (perfume.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(perfume); // Neu ten chua tu khoa thi them vao danh sach moi
            }
        }

        // Cap nhat danh sach hien thi va thong bao adapter
        perfumeEntityList.clear();
        perfumeEntityList.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }
}
