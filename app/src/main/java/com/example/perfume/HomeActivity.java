package com.example.perfume;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private Fragment discoverFragment = new DiscoverFragment();
    private Fragment homeFragment = new HomeFragment();
    private Fragment searchFragment = new SearchFragment();
    private Fragment profileFragment = new ProfileFragment();
    private Fragment activeFragment = searchFragment;

    private int currentTabIndex = 2; // nav_search mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        FragmentManager fm = getSupportFragmentManager();

        // Thêm các fragment vào fragment_container nhưng chỉ hiện fragment mặc định
        fm.beginTransaction().add(R.id.fragment_container, profileFragment, "4").hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, homeFragment, "1").hide(homeFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, discoverFragment, "0").hide(discoverFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, searchFragment, "2").commit(); // hiện mặc định

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int selectedIndex = 2;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_discover) {
                selectedFragment = discoverFragment;
                selectedIndex = 0;
            } else if (itemId == R.id.nav_home) {
                selectedFragment = homeFragment;
                selectedIndex = 1;
            } else if (itemId == R.id.nav_search) {
                selectedFragment = searchFragment;
                selectedIndex = 2;
            } else if (itemId == R.id.nav_cart) {
                Intent intent = new Intent(this, CartActivity.class);
                startActivityForResult(intent, 100);
                return true;
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = profileFragment;
                selectedIndex = 4;
            }

            if (selectedFragment != null && selectedFragment != activeFragment) {
                // Thêm animation theo hướng
                if (selectedIndex > currentTabIndex) {
                    fm.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                            .hide(activeFragment)
                            .show(selectedFragment)
                            .commit();
                } else if (selectedIndex < currentTabIndex) {
                    fm.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .hide(activeFragment)
                            .show(selectedFragment)
                            .commit();
                } else {
                    // Trường hợp không đổi hướng, chỉ show/hide
                    fm.beginTransaction()
                            .hide(activeFragment)
                            .show(selectedFragment)
                            .commit();
                }

                activeFragment = selectedFragment;
                currentTabIndex = selectedIndex;
            }

            return true;
        });
    }
}
