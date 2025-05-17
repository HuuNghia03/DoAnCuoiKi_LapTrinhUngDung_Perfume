package com.example.perfume.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.example.perfume.R;
import com.example.perfume.entity.UserEntity;
import com.example.perfume.UserRepository;
import com.google.android.material.slider.RangeSlider;

import java.util.List;
import java.util.Locale;

public class WelcomeOtherView extends LinearLayout {
    private Button btnConfirm;
    private ImageView imgNoSeason, imgColdSeason, imgHotSeason, imgAllSeason;
    private ImageView imgNoUsage, imgWorkUsage, imgOutUsage, imgAllUsage;
    private SwitchCompat switch1, switch2;
    private RangeSlider priceRangeSlider;
    private TextView pricePer50mlText;
    private String favoriteSeason, purposeUsage;
    private boolean requiresUniqueScent, requiresLongLasting;
    private float pricePer50ml;
    private UserEntity userEntity;
    private int userId;
    UserRepository userRepository;

    public WelcomeOtherView(Context context, UserEntity userEntity, int userId, Runnable onNext) {
        super(context);
        this.userEntity = userEntity;
        this.userId = userId; // Lưu userId vào đối tượng
        init(context, onNext);
    }


    private void init(Context context, Runnable onNext) {
        userRepository = new UserRepository(context);

        LayoutInflater.from(context).inflate(R.layout.welcome_other, this, true);

        imgNoSeason = findViewById(R.id.noSeason);
        imgColdSeason = findViewById(R.id.coldSeason);
        imgHotSeason = findViewById(R.id.hotSeason);
        imgAllSeason = findViewById(R.id.allSeason);

        imgNoUsage = findViewById(R.id.noUsage);
        imgWorkUsage = findViewById(R.id.workUsage);
        imgOutUsage = findViewById(R.id.outUsage);
        imgAllUsage = findViewById(R.id.allUsage);

        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);

        priceRangeSlider = findViewById(R.id.price_range_slider);
        pricePer50mlText = findViewById(R.id.pricePer50ml);
        // Xử lý switch1: mùi hương độc đáo
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            requiresUniqueScent = isChecked;
        });

// Xử lý switch2: độ lưu hương lâu
        switch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            requiresLongLasting = isChecked;
        });

        btnConfirm = findViewById(R.id.confirm_button);

        setupSeasonSelection();
        setupUsageSelection();
        setupPriceRangeSlider();


        btnConfirm.setOnClickListener(v -> {
            userRepository.updateUserPreferences(userId, false, userEntity.getGender(), userEntity.getAge(),userEntity.getPerfumePurpose(), userEntity.getCategoryList(),
                    favoriteSeason, purposeUsage, requiresUniqueScent, requiresLongLasting,
                    pricePer50ml, userEntity.getFavoritePerfume());
            postDelayed(onNext::run, 300);

        });
    }

    private void setupSeasonSelection() {
        View.OnClickListener seasonClickListener = v -> {
            imgNoSeason.setSelected(false);
            imgColdSeason.setSelected(false);
            imgHotSeason.setSelected(false);
            imgAllSeason.setSelected(false);

            v.setSelected(true);
            if (v == imgNoSeason) {
                favoriteSeason = "No Season";
            } else if (v == imgColdSeason) {
                favoriteSeason = "Cold Season";
            } else if (v == imgHotSeason) {
                favoriteSeason = "Hot Season";
            } else if (v == imgAllSeason) {
                favoriteSeason = "All Season";
            }

        };

        imgNoSeason.setOnClickListener(seasonClickListener);
        imgColdSeason.setOnClickListener(seasonClickListener);
        imgHotSeason.setOnClickListener(seasonClickListener);
        imgAllSeason.setOnClickListener(seasonClickListener);
    }

    private void setupUsageSelection() {
        View.OnClickListener seasonClickListener = v -> {
            imgNoUsage.setSelected(false);
            imgOutUsage.setSelected(false);
            imgWorkUsage.setSelected(false);
            imgAllUsage.setSelected(false);

            v.setSelected(true);
            if (v == imgNoUsage) {
                purposeUsage = "No Usage";
            } else if (v == imgOutUsage) {
                purposeUsage = "Going Out";
            } else if (v == imgWorkUsage) {
                purposeUsage = "Work";
            } else if (v == imgAllUsage) {
                purposeUsage = "All Purpose";
            }
        };

        imgNoUsage.setOnClickListener(seasonClickListener);
        imgOutUsage.setOnClickListener(seasonClickListener);
        imgWorkUsage.setOnClickListener(seasonClickListener);
        imgAllUsage.setOnClickListener(seasonClickListener);
    }

    private void setupPriceRangeSlider() {
        priceRangeSlider = findViewById(R.id.price_range_slider);
        pricePer50mlText = findViewById(R.id.pricePer50ml);

        // Thiết lập giới hạn giá trị
        priceRangeSlider.setValueFrom(0f);
        priceRangeSlider.setValueTo(200f);
        priceRangeSlider.setValues(0f, 100f); // Giá trị mặc định

        priceRangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> values = priceRangeSlider.getValues();
            float min = values.get(0);
            float max = values.get(1);

            // Cập nhật văn bản hiển thị
            String priceText = String.format(Locale.US, "%.0f$ - %.0f$", min, max);
            pricePer50mlText.setText(priceText);

            // Gán giá trị tối đa cho biến dùng sau
            pricePer50ml = max;
        });
    }


}
