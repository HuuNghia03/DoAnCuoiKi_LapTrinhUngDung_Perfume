package com.example.perfume;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class DepthPageTransformer implements ViewPager2.PageTransformer {
    public void transformPage(@NonNull View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) {
            view.setAlpha(0f);
        } else if (position <= 0) {
            view.setAlpha(1f);
            view.setTranslationX(0f);
            view.setScaleX(1f);
            view.setScaleY(1f);
        } else if (position <= 1) {
            view.setAlpha(1 - position);
            view.setTranslationX(pageWidth * -position);
            float scaleFactor = 0.75f + (1 - 0.75f) * (1 - position);
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else {
            view.setAlpha(0f);
        }
    }
}
