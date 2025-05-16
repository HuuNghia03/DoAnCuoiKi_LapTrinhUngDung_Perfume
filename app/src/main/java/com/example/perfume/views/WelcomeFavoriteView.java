package com.example.perfume.views;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.perfume.AppDatabase;
import com.example.perfume.R;
import com.example.perfume.entities.PerfumeEntity;
import com.example.perfume.entities.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class WelcomeFavoriteView extends LinearLayout {
    private LinearLayout favoriteItemsContainer;
    private EditText searchEditText;
    private AppDatabase appDatabase;
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
        btnConfirm = findViewById(R.id.confirm_button);
        searchEditText = findViewById(R.id.perfumeFind);
        appDatabase = AppDatabase.getInstance(context);

        btnConfirm.setOnClickListener(v -> {
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
            postDelayed(onNext::run, 200);
        });

        setupSearchListener(context);
    }

    private void setupSearchListener(Context context) {
        new Thread(() -> {
            List<PerfumeEntity> perfumes = appDatabase.perfumeDao().getAllPerfumes();
            post(() -> {
                fullPerfumeList = new ArrayList<>(perfumes);
                favoriteItemsContainer.removeAllViews();

                final Handler handler = new Handler();
                searchEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        handler.removeCallbacksAndMessages(null);
                        handler.postDelayed(() -> filterPerfumes(s.toString(), context), 300);
                    }
                });
            });
        }).start();
    }

    private void filterPerfumes(String keyword, Context context) {
        favoriteItemsContainer.removeAllViews();

        if (keyword.trim().isEmpty()) {
            return; // Không hiển thị gì nếu chưa nhập
        }

        List<PerfumeEntity> filteredList = new ArrayList<>();
        for (PerfumeEntity perfume : fullPerfumeList) {
            if (perfume.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(perfume);
            }
        }

        displayPerfume(filteredList, context);
    }

    private void displayPerfume(List<PerfumeEntity> perfumes, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);

        for (PerfumeEntity perfume : perfumes) {
            View view = inflater.inflate(R.layout.favorite_item, favoriteItemsContainer, false);

            TextView perfumeName = view.findViewById(R.id.perfumeName);
            ImageView perfumeImage = view.findViewById(R.id.perfumeImage);
            TextView brand = view.findViewById(R.id.brand);
            CheckBox checkBox = view.findViewById(R.id.selected);

            perfumeName.setText(perfume.getName());
            brand.setText(perfume.getBrand());
            Glide.with(context).load(perfume.getImg()).into(perfumeImage);

            view.setOnClickListener((v) -> {
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(true);
                userEntity.setFavoritePerfume(perfume.getName());
                Toast.makeText(context, "Favorite: " + userEntity.getFavoritePerfume(), Toast.LENGTH_SHORT).show();
                favoriteItemsContainer.removeAllViews();
                favoriteItemsContainer.addView(view);

            });

            favoriteItemsContainer.addView(view);
        }
    }
}
