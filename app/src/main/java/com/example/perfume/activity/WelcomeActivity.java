package com.example.perfume.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.perfume.Navigator;
import com.example.perfume.R;
import com.example.perfume.entity.UserEntity;
import com.example.perfume.view.WelcomeAgeView;
import com.example.perfume.view.WelcomeFavoriteView;
import com.example.perfume.view.WelcomeGenderView;
import com.example.perfume.view.WelcomeLookView;
import com.example.perfume.view.WelcomeOlfactiveView;
import com.example.perfume.view.WelcomeOtherView;
import com.example.perfume.view.WelcomeScentView;

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
        userId = Navigator.getUserId(this);


        container = findViewById(R.id.welcome_items_container);
        btnBack = findViewById(R.id.back_icon);
        welcome_title = findViewById(R.id.welcome_title);
        userEntity = new UserEntity();
        showLookView();
    }

    private void showLookView() {
        welcome_title.setText("Looking for");
        btnBack.setVisibility(ImageView.INVISIBLE);
        WelcomeLookView lookView = new WelcomeLookView(this, this::showGenderView, userEntity);
        container.removeAllViews();
        container.addView(lookView);

    }

    private void showGenderView() {
        welcome_title.setText("About you");
        btnBack.setVisibility(ImageView.VISIBLE);
        WelcomeGenderView genderView = new WelcomeGenderView(this, userEntity, this::showAgeView);
        container.removeAllViews();
        container.addView(genderView);
    }

    private void showAgeView() {
        WelcomeAgeView ageView = new WelcomeAgeView(this, userEntity, this::showFavoriteView);
        container.removeAllViews();
        container.addView(ageView);
    }

    private void showFavoriteView() {
        WelcomeFavoriteView favoriteView = new WelcomeFavoriteView(this, userEntity, this::showScentView);
        container.removeAllViews();
        container.addView(favoriteView);
    }

    private void showScentView() {
        WelcomeScentView scentView = new WelcomeScentView(this, userEntity, this::showOlfactiveView);
        container.removeAllViews();
        container.addView(scentView);
    }

    private void showOlfactiveView() {
        WelcomeOlfactiveView olfactiveView = new WelcomeOlfactiveView(this, userEntity, this::showOtherView);
        container.removeAllViews();
        container.addView(olfactiveView);
    }

    private void showOtherView() {
        Runnable onNext = () -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);

            //  Hiệu ứng chuyển cảnh
           overridePendingTransition(R.anim.zoom_in, android.R.anim.fade_out);

            finish();
        };

        WelcomeOtherView otherView = new WelcomeOtherView(this, userEntity, userId, onNext);
        container.removeAllViews();
        container.addView(otherView);
    }


}

