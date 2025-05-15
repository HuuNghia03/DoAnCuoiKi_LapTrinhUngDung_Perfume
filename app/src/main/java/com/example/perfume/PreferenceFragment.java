package com.example.perfume;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.concurrent.Executors;

public class PreferenceFragment extends Fragment {
    private UserEntity userEntity;
    private TextView firstName, lastName, Gender, Age, logOut, deleteAccount;
    private AppDatabase appDatabase;

    public PreferenceFragment(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preference, container, false);
        appDatabase=AppDatabase.getInstance(requireContext());
        bindViews(view);
        setProfile(userEntity);
        deleteAccount.setOnClickListener(v -> showDeleteAccountDialog(userEntity));
        logOut.setOnClickListener(v -> logOutAccount());
        return view;
    }

    private void bindViews(View view) {
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        Gender = view.findViewById(R.id.Gender);
        Age = view.findViewById(R.id.Age);
        logOut = view.findViewById(R.id.logOut);
        deleteAccount = view.findViewById(R.id.deleteAccount);

    }

    private void setProfile(UserEntity userEntity) {
        firstName.setText(userEntity.getFirstname());
        lastName.setText(userEntity.getLastname());
        Gender.setText(userEntity.getGender());
        Age.setText(String.valueOf(userEntity.getAge()));

    }

    private void showDeleteAccountDialog(UserEntity userEntity) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_delete_confirm, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(view);

        TextView txtTitle = view.findViewById(R.id.txtDeleteTitle);
        TextView txtCancel = view.findViewById(R.id.txtCancel);


        txtTitle.setText("DELETE YOUR ACCOUNT?");

        txtTitle.setOnClickListener(confirm -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                appDatabase.userDao().deleteUser(userEntity);

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Đã xóa tài khoản", Toast.LENGTH_SHORT).show();
                    // Ví dụ: quay về màn hình đăng nhập
                     startActivity(new Intent(requireContext(), login_activity.class));
                     requireActivity().finish();
                });
            });
            dialog.dismiss();
        });

        txtCancel.setOnClickListener(cancel -> dialog.dismiss());

        dialog.show();
    }
    private void logOutAccount() {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_delete_confirm, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(view);

        TextView txtTitle = view.findViewById(R.id.txtDeleteTitle);
        TextView txtCancel = view.findViewById(R.id.txtCancel);

        txtTitle.setText("LOG OUT?");

        txtTitle.setOnClickListener(v -> {
            dialog.dismiss();
            // Chuyển về màn hình đăng nhập
            startActivity(new Intent(requireContext(), login_activity.class));
            requireActivity().finish();
        });

        txtCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }



}
