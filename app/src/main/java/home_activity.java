package com.example.perfume;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home_activity extends AppCompatActivity {

    private int currentTabIndex = 0; // Mặc định là tab đầu tiên (Discover)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Mở fragment mặc định
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new com.example.perfume.DiscoverFragment())
                .commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int selectedIndex = 0;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_discover) {
                selectedFragment = new com.example.perfume.DiscoverFragment();
                selectedIndex = 0;
            } else if (itemId == R.id.nav_home) {
                selectedFragment = new com.example.perfume.HomeFragment();
                selectedIndex = 1;
            } else if (itemId == R.id.nav_search) {
                selectedFragment = new com.example.perfume.SearchFragment();
                selectedIndex = 2;
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new com.example.perfume.ProfileFragment();
                selectedIndex = 3;
            }

            if (selectedFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Chọn animation theo hướng
                if (selectedIndex > currentTabIndex) {
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (selectedIndex < currentTabIndex) {
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                }

                transaction.replace(R.id.fragment_container, selectedFragment).commit();

                // Cập nhật tab hiện tại
                currentTabIndex = selectedIndex;
            }

            return true;
        });
    }
}
