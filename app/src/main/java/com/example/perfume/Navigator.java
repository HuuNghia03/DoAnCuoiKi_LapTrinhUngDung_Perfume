package com.example.perfume;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Navigator {
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USER_ID = "userId";

    // Hàm lưu userId
    public static void saveUserId(Context context, int userId) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    // Hàm lấy userId
    public static int getUserId(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getInt(KEY_USER_ID, -1); // Trả về -1 nếu không tìm thấy
    }
    public static void openPerfumeDetail(AppCompatActivity activity, PerfumeEntity perfume) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("perfume", perfume);

        Fragment perfumeDetail = new PerfumeDetail();
        perfumeDetail.setArguments(bundle);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(
                R.anim.zoom_in,  // hiệu ứng vào
                0,
                R.anim.zoom_out, // hiệu ứng popEnter
                0
        );

        transaction.replace(R.id.fragment_container, perfumeDetail)
                .addToBackStack(null)
                .commit();
    }
    public static void openBrandDetail(AppCompatActivity activity, BrandEntity brand){
        Bundle bundle = new Bundle();
        bundle.putSerializable("brand", brand);

        Fragment brandDetail = new BrandDetail();
        brandDetail.setArguments(bundle);
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.zoom_in,
                0,
                R.anim.zoom_out,
                0
        );
        transaction.replace(R.id.fragment_container, brandDetail)
                .addToBackStack(null)
                .commit();
    }

    public static void openNoteDetail(AppCompatActivity activity, Note note){
        Bundle bundle = new Bundle();
        bundle.putString("category", note.getCategory());
        bundle.putString("notes", note.getNotes());
        bundle.putString("image", note.getImageUrl());
        bundle.putString("description",note.getDescription());
        // Chuyển đến Fragment chi tiết nước hoa
        Fragment noteDetail = new com.example.perfume.NoteDetail();
        noteDetail.setArguments(bundle);

        // Tạo giao dịch Fragment
        FragmentTransaction transaction = activity
                .getSupportFragmentManager()
                .beginTransaction();

        // Thêm hiệu ứng thu phóng (scale) vào fragment transition
        transaction.setCustomAnimations(
                R.anim.zoom_in,   // enter: mượt mà khi mở fragment
                0,                // exit: không hiệu ứng
                R.anim.zoom_out,                // popEnter: phóng to khi quay lại
                0               // popExit: thu nhỏ khi thoát
        );

        // Thực hiện chuyển fragment
        transaction.replace(R.id.fragment_container, noteDetail)
                .addToBackStack(null)
                .commit();
    }
}
