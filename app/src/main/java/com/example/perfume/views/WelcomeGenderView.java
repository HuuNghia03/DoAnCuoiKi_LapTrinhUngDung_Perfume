package com.example.perfume.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.perfume.R;
import com.example.perfume.entities.UserEntity;

public class WelcomeGenderView extends LinearLayout {
    private UserEntity userEntity;
    public WelcomeGenderView(Context context, UserEntity userEntity, Runnable onNext) {
        super(context);
        this.userEntity = userEntity;
        init(context, onNext);
    }

    private void init(Context context, Runnable onNext) {
        LayoutInflater.from(context).inflate(R.layout.welcome_gender, this, true);

        LinearLayout man = findViewById(R.id.man);
        LinearLayout woman = findViewById(R.id.woman);

        View.OnClickListener listener = v -> {
            // Lưu thông tin giới tính vào userEntity
            userEntity.setGender(v.getId() == R.id.man ? "Male" : "Female");
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
            postDelayed(onNext::run, 200);
        };

        man.setOnClickListener(listener);
        woman.setOnClickListener(listener);
    }
}
