package com.example.perfume;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.perfume.views.WelcomeAgeView;
import com.example.perfume.views.WelcomeFavoriteView;
import com.example.perfume.views.WelcomeGenderView;
import com.example.perfume.views.WelcomeLookView;
import com.example.perfume.views.WelcomeOlfactiveView;
import com.example.perfume.views.WelcomeOtherView;
import com.example.perfume.views.WelcomeScentView;

public class WelcomeActivity extends AppCompatActivity {

    private LinearLayout container;
    private ImageView btnBack;
    private TextView welcome_title;
    private UserEntity userEntity;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        userId  = Navigator.getUserId(this);


        container = findViewById(R.id.welcome_items_container);
        btnBack = findViewById(R.id.back_icon);
        welcome_title = findViewById(R.id.welcome_title);
        userEntity = new UserEntity();
        showLookView();
    }

    private void showLookView() {
        welcome_title.setText("Looking for");
        btnBack.setVisibility(ImageView.INVISIBLE);
        WelcomeLookView lookView = new WelcomeLookView(this, this::showGenderView);
        container.removeAllViews();
        container.addView(lookView);
    }

    private void showGenderView() {
        welcome_title.setText("About you");
        btnBack.setVisibility(ImageView.VISIBLE);
        WelcomeGenderView genderView = new WelcomeGenderView(this,userEntity, this::showAgeView);
        container.removeAllViews();
        container.addView(genderView);
    }

    private void showAgeView() {
        WelcomeAgeView ageView = new WelcomeAgeView(this, userEntity, this::showFavoriteView);
        container.removeAllViews();
        container.addView(ageView);
    }

    private void showFavoriteView() {
        WelcomeFavoriteView favoriteView = new WelcomeFavoriteView(this,userEntity, this::showScentView);
        container.removeAllViews();
        container.addView(favoriteView);
    }

    private void showScentView() {
        WelcomeScentView scentView = new WelcomeScentView(this, userEntity, this::showOlfactiveView);
        container.removeAllViews();
        container.addView(scentView);
    }

    private void showOlfactiveView() {
        WelcomeOlfactiveView olfactiveView = new WelcomeOlfactiveView(this,userEntity, this::showOtherView);
        container.removeAllViews();
        container.addView(olfactiveView);
    }

    private void showOtherView() {
        Runnable onNext = () -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish(); // Đóng WelcomeActivity nếu cần
        };

        WelcomeOtherView otherView = new WelcomeOtherView(this, userEntity, userId,onNext);
        container.removeAllViews();
        container.addView(otherView);
    }

}

