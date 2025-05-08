package com.example.perfume;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Navigator {

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
        bundle.putString("id", brand.getId());
        bundle.putString("name", brand.getName());
        bundle.putString("banner", brand.getBanner());
        bundle.putString("logo", brand.getLogo());
        bundle.putString("perfumes", brand.getPerfumes());
        bundle.putString("founded", brand.getFounded());
        bundle.putString("founder", brand.getFounder());
        bundle.putString("country", brand.getCountry());
        bundle.putString("notablePerfumes", brand.getNotablePerfumes());
        bundle.putString("segment", brand.getSegment());
        bundle.putString("popularity", brand.getPopularity());
        bundle.putString("style", brand.getStyle());
        bundle.putString("link", brand.getLink());
        bundle.putString("description", brand.getDescription());

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
