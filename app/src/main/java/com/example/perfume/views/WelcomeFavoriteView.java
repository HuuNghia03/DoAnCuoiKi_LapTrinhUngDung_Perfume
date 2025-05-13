package com.example.perfume.views;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.perfume.AppDatabase;
import com.example.perfume.PerfumeEntity;
import com.example.perfume.R;
import com.example.perfume.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class WelcomeFavoriteView extends LinearLayout {
    private LinearLayout favoriteItemsContainer;
    private EditText searchEditText;
    private AppDatabase appDatabase;
    private List<PerfumeEntity> perfumeEntityList;
    private List<PerfumeEntity> fullPerfumeList;

    private Button btnConfirm;
    private UserEntity userEntity;
    public WelcomeFavoriteView(Context context, UserEntity userEntity, Runnable onNext) {
        super(context);
        this.userEntity = userEntity;
        init(context, onNext);
    }
    private void init(Context context, Runnable onNext) {
        LayoutInflater.from(context).inflate(R.layout.welcome_favorite, this, true);
        favoriteItemsContainer = findViewById(R.id.favorite_items_container);
        btnConfirm=findViewById(R.id.confirm_button);
        searchEditText = findViewById(R.id.perfumeFind);
        appDatabase = AppDatabase.getInstance(context);
        btnConfirm.setOnClickListener(v -> {
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
            postDelayed(onNext::run, 200);
        });
        // Ở đây bạn có thể thêm xử lý sự kiện nếu cần
        setupSearchListener(context);
    }

    private void displayPerfume(List<PerfumeEntity> perfumes, Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        for (PerfumeEntity perfume : perfumes) {
            View view = inflater.inflate(R.layout.favorite_item, favoriteItemsContainer, false);
            TextView perfumeName=view.findViewById(R.id.perfumeName);
            ImageView perfumeImage=view.findViewById(R.id.perfumeImage);
            TextView brand=view.findViewById(R.id.brand);
            perfumeName.setText(perfume.getName());
            Glide.with(context).load(perfume.getImg()).into(perfumeImage);
            brand.setText(perfume.getBrand());
            view.setOnClickListener(v -> {
                String namePerfume=perfume.getName();
                userEntity.setFavoritePerfume(namePerfume);
                Toast.makeText(v.getContext(), "Favorite: " + userEntity.getFavoritePerfume(), Toast.LENGTH_SHORT).show();
            });

            favoriteItemsContainer.addView(view);
        }

    }
    private void setupSearchListener(Context context) {
        new Thread(() -> {
            List<PerfumeEntity> perfumes = appDatabase.perfumeDao().getAllPerfumes();

            ((WelcomeFavoriteView) this).post(() -> {
                fullPerfumeList = new ArrayList<>(perfumes);
                perfumeEntityList = new ArrayList<>(perfumes);
                favoriteItemsContainer.removeAllViews();

                final Handler handler = new Handler();
                searchEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Hủy bỏ các tìm kiếm cũ nếu có
                        handler.removeCallbacksAndMessages(null);
                        handler.postDelayed(() -> filterPerfumes(s.toString(), context), 300); // 300ms delay
                    }
                });
            });
        }).start();
    }



    private void filterPerfumes(String keyword, Context context) {
        favoriteItemsContainer.removeAllViews(); // Xóa item cũ

        if (keyword.trim().isEmpty()) {
            // Nếu EditText trống, không hiển thị gì cả
            return;
        }
        List<PerfumeEntity> filteredList = new ArrayList<>();
        for (PerfumeEntity perfume : fullPerfumeList) {
            if (perfume.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(perfume);
            }
        }

        // So sánh lại để kiểm tra có thay đổi danh sách không
        if (!perfumeEntityList.equals(filteredList)) {
            perfumeEntityList.clear();
            perfumeEntityList.addAll(filteredList);
            favoriteItemsContainer.removeAllViews(); // Xóa item cũ
            displayPerfume(perfumeEntityList, context); // Hiển thị lại
        }
    }


}
