package com.example.perfume.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.perfume.R;
import com.example.perfume.adapter.PageAdapter;

import java.util.Arrays;
import java.util.List;

public class FirstActivity extends AppCompatActivity {
    private Button btRegister, btLogin;
    private View indicator1, indicator2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        initViews();
        setupViewPager();
        setupButtonClicks();
    }

    private void initViews() {
        indicator1 = findViewById(R.id.indicator1);
        indicator2 = findViewById(R.id.indicator2);
        btRegister = findViewById(R.id.button_register);
        btLogin = findViewById(R.id.button_login);
    }

    private void setupViewPager() {
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        List<Integer> layouts = Arrays.asList(R.layout.home_page1, R.layout.home_page2);
        PageAdapter adapter = new PageAdapter(layouts);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicators(position);
            }
        });

        // Khởi tạo trạng thái indicator ban đầu
        updateIndicators(0);
    }

    private void updateIndicators(int position) {
        if (position == 0) {
            indicator1.setBackgroundResource(R.drawable.indicator_active);
            indicator2.setBackgroundResource(R.drawable.indicator_inactive);
        } else {
            indicator1.setBackgroundResource(R.drawable.indicator_inactive);
            indicator2.setBackgroundResource(R.drawable.indicator_active);
        }
    }

    private void setupButtonClicks() {
        btRegister.setOnClickListener(v -> startActivity(new Intent(FirstActivity.this, RegisterActivity.class)));
        btLogin.setOnClickListener(v -> startActivity(new Intent(FirstActivity.this, LoginActivity.class)));
    }
}
