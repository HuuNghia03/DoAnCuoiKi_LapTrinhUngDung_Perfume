package com.example.perfume.views;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.perfume.R;

public class WelcomeLookView extends LinearLayout {

    public WelcomeLookView(Context context, Runnable onNext) {
        super(context);
        init(context, onNext);
    }

    private void init(Context context, Runnable onNext) {
        LayoutInflater.from(context).inflate(R.layout.welcome_look, this, true);

        LinearLayout forme = findViewById(R.id.forme);
        LinearLayout gift = findViewById(R.id.gift);

        View.OnClickListener listener = v -> {
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
            postDelayed(onNext::run, 200);
        };

        forme.setOnClickListener(listener);
        gift.setOnClickListener(listener);
    }
}
