package com.example.perfume.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.perfume.R;
import com.example.perfume.entity.UserEntity;

public class WelcomeLookView extends LinearLayout {
    private UserEntity userEntity;

    public WelcomeLookView(Context context, Runnable onNext, UserEntity userEntity) {
        super(context);
        this.userEntity = userEntity;
        init(context, onNext);
    }

    private void init(Context context, Runnable onNext) {
        LayoutInflater.from(context).inflate(R.layout.welcome_look, this, true);

        LinearLayout forme = findViewById(R.id.forme);
        LinearLayout gift = findViewById(R.id.gift);

        forme.setOnClickListener(v -> {
            userEntity.setPerfumePurpose("For me");
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
            postDelayed(onNext::run, 300);
        });

        gift.setOnClickListener(v -> {
            userEntity.setPerfumePurpose("For gift");
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
            postDelayed(onNext::run, 300);
        });
    }

}
