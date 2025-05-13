    package com.example.perfume;

    import android.content.Intent;
    import android.os.Bundle;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentManager;
    import androidx.fragment.app.FragmentTransaction;

    import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
        private Fragment discoverFragment = new DiscoverFragment();
        private Fragment homeFragment=null ;//= new HomeFragment();
        private Fragment searchFragment = new SearchFragment();
        private Fragment profileFragment = new ProfileFragment();

    private Fragment detailFragment = null;

        private Fragment activeFragment = searchFragment; // Mặc định là Search
        private int currentTabIndex = 2; // nav_search mặc định

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.home_layout);

            FragmentManager fm = getSupportFragmentManager();


            // Add các fragment tab
            fm.beginTransaction().add(R.id.fragment_container, profileFragment, "4").hide(profileFragment).commit();
       //     fm.beginTransaction().add(R.id.fragment_container, homeFragment, "1").commit();
            fm.beginTransaction().add(R.id.fragment_container, discoverFragment, "0").hide(discoverFragment).commit();
            fm.beginTransaction().add(R.id.fragment_container, searchFragment, "2").commit(); // Hiện mặc định

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.nav_search);

            bottomNavigationView.setOnItemSelectedListener(item -> {
                FragmentManager fm2 = getSupportFragmentManager();
                FragmentTransaction transaction = fm2.beginTransaction();

                Fragment selectedFragment = null;
                int selectedIndex = currentTabIndex;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_discover) {
                    selectedFragment = discoverFragment;
                    selectedIndex = 0;
                } else if (itemId == R.id.nav_home) {
                    if (homeFragment == null) {
                        homeFragment = new HomeFragment();
                        selectedFragment = homeFragment;
                        selectedIndex = 1;

                        // Hiệu ứng khi thêm mới
                        if (selectedIndex > currentTabIndex) {
                            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                        }

                        transaction.hide(activeFragment).add(R.id.fragment_container, homeFragment, "1").commit();
                        activeFragment = homeFragment;
                        currentTabIndex = selectedIndex;
                        return true;
                    } else {
                        selectedFragment = homeFragment;
                        selectedIndex = 1;
                    }
                } else if (itemId == R.id.nav_search) {
                    selectedFragment = searchFragment;
                    selectedIndex = 2;
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(this, CartActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = profileFragment;
                    selectedIndex = 4;
                }

                // Trở về từ detail
                if (detailFragment != null && detailFragment.isAdded() && detailFragment.isVisible()) {
                    FragmentTransaction tx = fm2.beginTransaction();
                    tx.setCustomAnimations(R.anim.pop_zoom_in, 0);
                    tx.hide(detailFragment).show(selectedFragment).commit();

                    activeFragment = selectedFragment;
                    currentTabIndex = selectedIndex;
                    return true;
                }

                // Chuyển tab bình thường
                if (selectedFragment != null && selectedFragment != activeFragment) {
                    if (selectedIndex > currentTabIndex) {
                        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    }

                    transaction.hide(activeFragment).show(selectedFragment).commit();

                    activeFragment = selectedFragment;
                    currentTabIndex = selectedIndex;
                }

                return true;
            });

        }

        // Được gọi khi mở chi tiết nước hoa
        public void setDetailFragment(Fragment fragment) {
            this.detailFragment = fragment;
        }
    }

