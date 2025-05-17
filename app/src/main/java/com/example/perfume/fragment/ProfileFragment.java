package com.example.perfume.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.perfume.AppDatabase;
import com.example.perfume.Navigator;
import com.example.perfume.R;
import com.example.perfume.UserViewModel;
import com.example.perfume.activity.HomeActivity;
import com.example.perfume.activity.WelcomeActivity;
import com.example.perfume.entity.PerfumeEntity;
import com.example.perfume.entity.UserEntity;
import com.example.perfume.view.WelcomeOlfactiveView;
import com.github.mikephil.charting.charts.RadarChart;

import java.util.Arrays;
import java.util.List;

public class ProfileFragment extends Fragment {
    private LinearLayout userOrder, favPerfume, userPreference, newProfile, userAddress;
    private AppDatabase appDatabase;
    private int userId;
    private UserEntity userEntity;
    private PerfumeEntity perfumeEntity;
    private RadarChart olfactiveChart;
    private UserViewModel userViewModel;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        bindViews(view);

        appDatabase = AppDatabase.getInstance(getContext());
        userId = Navigator.getUserId(requireContext());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setUserId(userId);
        //khi có thay đổi trong room
        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                displayUserInfo(user);

            }
        });


        fetchUserData();

        userOrder.setOnClickListener(v -> openOrderFragment());
        userPreference.setOnClickListener(v -> openPreferenceFragment(userEntity));
        newProfile.setOnClickListener(v -> showNewProfileDialog());
        userAddress.setOnClickListener(v -> openAddressActivity());


        return view;
    }



    private void bindViews(View view) {
        userOrder = view.findViewById(R.id.userOrder);
        favPerfume = view.findViewById(R.id.favPerfume);
        olfactiveChart = view.findViewById(R.id.radarChart);
        userPreference = view.findViewById(R.id.userPreference);
        newProfile = view.findViewById(R.id.newProfile);
        userAddress = view.findViewById(R.id.userAddress);
    }

    private void fetchUserData() {
        new Thread(() -> {
            userEntity = appDatabase.userDao().getUserById(userId);

            requireActivity().runOnUiThread(() -> {
                if (userEntity != null) {
                    displayUserInfo(userEntity);
                    setupRadarChart();
                    loadFavoritePerfume();
                } else {
                    Log.e("ProfileFragment", "userEntity is null");
                }
            });
        }).start();
    }

    private void displayUserInfo(UserEntity userEntity) {
        View view = getView();
        if (view == null) return;

        TextView nameText = view.findViewById(R.id.Name);
        TextView genderText = view.findViewById(R.id.Gender);
        TextView yearText = view.findViewById(R.id.Year);
        ImageView Avatar = view.findViewById(R.id.avatarUser);
        SwitchCompat switch1=view.findViewById(R.id.switch1);
        SwitchCompat switch2=view.findViewById(R.id.switch2);

        String fullName = userEntity.getFirstname() + " " + userEntity.getLastname();
        nameText.setText(fullName);
        genderText.setText(userEntity.getGender());
        yearText.setText(String.valueOf(userEntity.getAge())+" Years old");
        if ("Male".equalsIgnoreCase(userEntity.getGender())) {
            Avatar.setImageResource(R.drawable.avatar_male1); // thay bằng ảnh nam
        } else if ("Female".equalsIgnoreCase(userEntity.getGender())) {
            Avatar.setImageResource(R.drawable.avatar_female1); // thay bằng ảnh nữ
        } else {
            Avatar.setImageResource(R.drawable.avatar_female1); // ảnh mặc định nếu không xác định được
        }
        if(userEntity.isRequiresLongLasting()){
            switch2.setChecked(true);
        }
        if(userEntity.isRequiresUniqueScent()){
            switch1.setChecked(true);
        }

    }

    private void setupRadarChart() {
        String olfactive = userEntity.getCategoryList();
        if (olfactive != null && !olfactive.isEmpty()) {
            List<String> selectedNotes = Arrays.asList(olfactive.split(",\\s*"));
            WelcomeOlfactiveView view = new WelcomeOlfactiveView(requireContext(), userEntity, () -> {
            });
            view.setupRadarChart(requireContext(), selectedNotes, olfactiveChart, Color.parseColor("#ffffff"));
        } else {
            Log.w("ProfileFragment", "Olfactive category list is empty or null");
        }
    }

    private void loadFavoritePerfume() {
        new Thread(() -> {
            String favoriteName = userEntity.getFavoritePerfume();
            perfumeEntity = appDatabase.perfumeDao().getPerfumesByName(favoriteName);

            requireActivity().runOnUiThread(() -> {
                if (perfumeEntity != null) {
                    addFavoritePerfumeView();
                } else {
                    Log.e("ProfileFragment", "perfumeEntity is null");
                }
            });
        }).start();
    }

    private void addFavoritePerfumeView() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View itemView = inflater.inflate(R.layout.favorite_item, favPerfume, false);

        TextView perfumeName = itemView.findViewById(R.id.perfumeName);
        TextView brand = itemView.findViewById(R.id.brand);
        ImageView perfumeImage = itemView.findViewById(R.id.perfumeImage);

        perfumeName.setText(perfumeEntity.getName());
        brand.setText(perfumeEntity.getBrand());

        Glide.with(requireContext())
                .load(perfumeEntity.getImg())
                .into(perfumeImage);

        favPerfume.addView(itemView);
    }

    private void openOrderFragment() {
        Fragment orderFragment = new OrderFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        ((HomeActivity) getActivity()).setDetailFragment(orderFragment);
        transaction.setCustomAnimations(R.anim.zoom_in, 0, R.anim.pop_zoom_in, 0);
        transaction.hide(this);
        transaction.add(R.id.fragment_container, orderFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openPreferenceFragment(UserEntity userEntity) {
        Fragment preferenceFragment = new PreferenceFragment(userEntity);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        ((HomeActivity) getActivity()).setDetailFragment(preferenceFragment);
        transaction.setCustomAnimations(R.anim.zoom_in, 0, R.anim.pop_zoom_in, 0);
       transaction.hide(this);
        transaction.add(R.id.fragment_container, preferenceFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void openAddressActivity() {
        Fragment addressFragment = new AddressFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        ((HomeActivity) getActivity()).setDetailFragment(addressFragment);
        transaction.setCustomAnimations(R.anim.zoom_in, 0, R.anim.pop_zoom_in, 0);
        transaction.hide(this);
        transaction.add(R.id.fragment_container, addressFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showNewProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_profile, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Ánh xạ view
        TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
        TextView txtMessage = dialogView.findViewById(R.id.txtMessage);
        Button btnContinue = dialogView.findViewById(R.id.btnContinue);
        ImageView btnClose = dialogView.findViewById(R.id.btnClose);

        // Xử lý nút
        btnContinue.setOnClickListener(v -> {


            dialog.dismiss();
            startActivity(new Intent(requireContext(), WelcomeActivity.class));
            requireActivity().finish();
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

}
