package com.example.perfume;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
                R.anim.zoom_in,
                0,
                R.anim.pop_zoom_in,
                0
        );

        transaction.replace(R.id.fragment_container, perfumeDetail)
                .addToBackStack(null)
                .commit();
    }


    public static void openPerfumeDetail1(AppCompatActivity activity, PerfumeEntity perfume, Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("perfume", perfume);

        Fragment perfumeDetail = new PerfumeDetail();
        perfumeDetail.setArguments(bundle);


        if (activity instanceof HomeActivity) {
            ((HomeActivity) activity).setDetailFragment(perfumeDetail);

        }


        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.setCustomAnimations(
                R.anim.zoom_in,
                0,
                R.anim.pop_zoom_in,
                0
        );

        if (fragment != null) {
            transaction.hide(fragment); // Ẩn fragment gọi đến
        }

        transaction.add(R.id.fragment_container, perfumeDetail);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public static void openBrandDetail(AppCompatActivity activity, BrandEntity brand, Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("brand", brand);

        Fragment brandDetail = new BrandDetail();
        brandDetail.setArguments(bundle);
        if (activity instanceof HomeActivity) {
            ((HomeActivity) activity).setDetailFragment(brandDetail);
        }
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.setCustomAnimations(
                R.anim.zoom_in,
                0,
                R.anim.pop_zoom_in,
                0
        );
        if (fragment != null) {
            transaction.hide(fragment); // Ẩn fragment gọi đến
        }

        transaction.add(R.id.fragment_container, brandDetail)
                .addToBackStack(null)
                .commit();
    }

    public static void openNoteDetail(AppCompatActivity activity, Note note, Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("category", note.getCategory());
        bundle.putString("notes", note.getNotes());
        bundle.putString("image", note.getImageUrl());
        bundle.putString("description", note.getDescription());
        // Chuyển đến Fragment chi tiết nước hoa
        Fragment noteDetail = new com.example.perfume.NoteDetail();
        noteDetail.setArguments(bundle);
        if (activity instanceof HomeActivity) {
            ((HomeActivity) activity).setDetailFragment(noteDetail);
        }
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.setCustomAnimations(
                R.anim.zoom_in,
                0,
                R.anim.pop_zoom_in,
                0
        );
        if (fragment != null) {
            transaction.hide(fragment); // Ẩn fragment gọi đến
        }
        // Thực hiện chuyển fragment
        transaction.add(R.id.fragment_container, noteDetail)
                .addToBackStack(null)
                .commit();
    }
}
