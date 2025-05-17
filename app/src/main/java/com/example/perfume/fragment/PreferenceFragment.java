package com.example.perfume.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.perfume.AppDatabase;
import com.example.perfume.R;
import com.example.perfume.UserRepository;
import com.example.perfume.activity.HomeActivity;
import com.example.perfume.activity.LoginActivity;
import com.example.perfume.entity.UserEntity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.concurrent.Executors;

public class PreferenceFragment extends Fragment {
    private UserEntity userEntity;
    private EditText firstName, lastName, Gender, Age;
    private TextView logOut, deleteAccount;
    private AppDatabase appDatabase;
    private ImageView editFirstNameIcon, editLastNameIcon, editGenderIcon, editAgeIcon, avatarUser, btnBack;
    private UserRepository userRepository;

    public PreferenceFragment(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preference, container, false);
        appDatabase = AppDatabase.getInstance(requireContext());
        userRepository = new UserRepository(requireContext());
        bindViews(view);
        setProfile(userEntity);
        deleteAccount.setOnClickListener(v -> showDeleteAccountDialog(userEntity));
        logOut.setOnClickListener(v -> logOutAccount());
        editLastNameIcon.setOnClickListener(v -> enableEditText(lastName, editLastNameIcon));
        editFirstNameIcon.setOnClickListener(v -> enableEditText(firstName, editFirstNameIcon));
        editGenderIcon.setOnClickListener(v -> enableEditText(Gender, editGenderIcon));
        editAgeIcon.setOnClickListener(v -> enableEditText(Age, editAgeIcon));
        btnBack.setOnClickListener(v -> {
            ((HomeActivity) requireActivity()).setDetailFragment(
                    getParentFragmentManager().findFragmentByTag("4"));
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        return view;
    }

    private void bindViews(View view) {
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        Gender = view.findViewById(R.id.Gender);
        Age = view.findViewById(R.id.Age);
        logOut = view.findViewById(R.id.logOut);
        deleteAccount = view.findViewById(R.id.deleteAccount);
        editLastNameIcon = view.findViewById(R.id.editLastName);
        editFirstNameIcon = view.findViewById(R.id.editFirstName);
        editGenderIcon = view.findViewById(R.id.editGender);
        editAgeIcon = view.findViewById(R.id.editAge);
        avatarUser = view.findViewById(R.id.avatarUser);
        btnBack = view.findViewById(R.id.btnBack);

    }

    private void setProfile(UserEntity userEntity) {
        firstName.setText(userEntity.getFirstname());
        lastName.setText(userEntity.getLastname());
        Gender.setText(userEntity.getGender());
        Age.setText(String.valueOf(userEntity.getAge()));
        if ("Male".equalsIgnoreCase(userEntity.getGender())) {
            avatarUser.setImageResource(R.drawable.avatar_male1); // thay bằng ảnh nam
        } else if ("Female".equalsIgnoreCase(userEntity.getGender())) {
            avatarUser.setImageResource(R.drawable.avatar_female1); // thay bằng ảnh nữ
        } else {
            avatarUser.setImageResource(R.drawable.avatar_female1); // ảnh mặc định nếu không xác định được
        }

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
                    startActivity(new Intent(requireContext(), LoginActivity.class));
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
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });

        txtCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void enableEditText(EditText editText, ImageView icon) {
        if (editText.isFocusable()) {
            // Nếu đang mở edit => lưu, khóa lại, cập nhật user
            String value = editText.getText().toString().trim();

            if (editText == firstName) {
                userEntity.setFirstname(value);
            } else if (editText == lastName) {
                userEntity.setLastname(value);
            } else if (editText == Gender) {
                userEntity.setGender(value);
                if ("Male".equalsIgnoreCase(userEntity.getGender())) {
                    avatarUser.setImageResource(R.drawable.avatar_male1);
                } else if ("Female".equalsIgnoreCase(userEntity.getGender())) {
                    avatarUser.setImageResource(R.drawable.avatar_female1);
                } else {
                    avatarUser.setImageResource(R.drawable.avatar_female1);
                }
            } else if (editText == Age) {
                try {
                    int age = Integer.parseInt(value);
                    userEntity.setAge(age);
                } catch (NumberFormatException e) {
                    userEntity.setAge(0); // hoặc thông báo lỗi
                }
            }

            disableEditText(editText);
            updateUser(userEntity);
            icon.setImageResource(R.drawable.ic_edit);
            //  Ẩn bàn phím sau khi lưu
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && getView() != null) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }


        } else {
            // Nếu đang khóa => bật cho nhập
            editText.setEnabled(true);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setCursorVisible(true);
            editText.setClickable(true);
            editText.setLongClickable(true);
            icon.setImageResource(R.drawable.ic_edit_bold);
            editText.requestFocus();
            editText.setSelection(editText.getText().length());


            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }


    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setCursorVisible(false);
        editText.setLongClickable(false);
        editText.setClickable(false);
    }


    private void updateUser(UserEntity userEntity) {
        userRepository.updateUserProfile(userEntity.getId(), userEntity.getFirstname(), userEntity.getLastname(), userEntity.getGender(), userEntity.getAge());


    }


}
