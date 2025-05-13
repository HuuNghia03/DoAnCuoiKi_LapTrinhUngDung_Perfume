package com.example.perfume;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private int userId;
    UserRepository userRepository;
    private AppDatabase db;
    private LinearLayout ItemsContainer;
    private PerfumeAdapter perfumeAdapter;
    private BrandAdapter brandAdapter;
    private NoteAdapter noteAdapter;
    private AppDatabase appDatabase;
    private ShimmerFrameLayout shimmerLayout;
    private ViewGroup contentContainer;
    private TextView nameUser;
    private UserEntity userEntity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        nameUser = view.findViewById(R.id.nameUser);
        db = AppDatabase.getInstance(getContext());
        userId = Navigator.getUserId(getContext());
        new Thread(() -> {
            userEntity = db.userDao().getUserById(userId);
            requireActivity().runOnUiThread(() -> {
                String fullName = userEntity.getFirstname() + " " + userEntity.getLastname();
                nameUser.setText(fullName);
            });
        }).start();


        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        contentContainer = view.findViewById(R.id.contentContainer);
        ItemsContainer = view.findViewById(R.id.items_container);

        appDatabase = AppDatabase.getInstance(requireContext());

        // Luôn load lại dữ liệu mỗi khi fragment được tạo
        shimmerLayout.setVisibility(View.VISIBLE);
        contentContainer.setVisibility(View.GONE);
        shimmerLayout.startShimmer();


        loadPerfume();


        return view;
    }

    private void loadPerfume() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        new Thread(() -> {
            // Load dữ liệu từ Room
            List<PerfumeEntity> perfumeList = appDatabase.perfumeDao().getAllPerfumes();
            List<BrandEntity> brandList = appDatabase.BrandDao().getAllBrandsWithImage();
            List<Note> noteList = appDatabase.noteDao().getAllNotes();

            List<PerfumeEntity> topPerfumes = perfumeList.size() > 10 ? perfumeList.subList(0, 10) : perfumeList;
            List<BrandEntity> topBrands = brandList.size() > 10 ? brandList.subList(0, 10) : brandList;
            List<Note> topNotes = noteList.size() > 5 ? noteList.subList(0, 5) : noteList;

            // Tạo adapter
            perfumeAdapter = new PerfumeAdapter(requireContext(), topPerfumes, -1, this, false);
            brandAdapter = new BrandAdapter(requireContext(), topBrands, this, false);
            noteAdapter = new NoteAdapter(requireContext(), topNotes, this, false);

            requireActivity().runOnUiThread(() -> {
                // PHẦN 1: Perfume
                View perfumeSection = inflater.inflate(R.layout.search_section, ItemsContainer, false);
                TextView perfumeTitle = perfumeSection.findViewById(R.id.tvTitle);
                perfumeTitle.setText("FRAGRANCE COLLECTION");
                RecyclerView perfumeRecycler = perfumeSection.findViewById(R.id.itemRecyclerView);
                perfumeRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                perfumeRecycler.setAdapter(perfumeAdapter);
                TextView seeMorePerfume = perfumeSection.findViewById(R.id.textSeemore);
                seeMorePerfume.setOnClickListener(v -> openPerfumeSeeMore());

                ItemsContainer.addView(perfumeSection);

                // PHẦN 2: Brand
                View brandSection = inflater.inflate(R.layout.search_section, ItemsContainer, false);
                TextView brandTitle = brandSection.findViewById(R.id.tvTitle);
                brandTitle.setText("FRAGRANCE BRANDS");
                RecyclerView brandRecycler = brandSection.findViewById(R.id.itemRecyclerView);
                brandRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                brandRecycler.setAdapter(brandAdapter);
                TextView seeMoreBrand = brandSection.findViewById(R.id.textSeemore);
                seeMoreBrand.setOnClickListener(v -> openBrandSeeMore());
                ItemsContainer.addView(brandSection);

                // PHẦN 3: Note
                View noteSection = inflater.inflate(R.layout.search_section, ItemsContainer, false);
                TextView noteTitle = noteSection.findViewById(R.id.tvTitle);
                noteTitle.setText("FRAGRANCE NOTES");
                RecyclerView noteRecycler = noteSection.findViewById(R.id.itemRecyclerView);
                noteRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                noteRecycler.setAdapter(noteAdapter);
                TextView seeMoreNote = noteSection.findViewById(R.id.textSeemore);
                seeMoreNote.setOnClickListener(v -> openNoteSeeMore());
                ItemsContainer.addView(noteSection);

                // Dừng shimmer
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);
                    contentContainer.setVisibility(View.VISIBLE);
                }, 500);
            });

        }).start();
    }


    private void openPerfumeSeeMore() {
        Fragment PerfumeSeeMore = new PerfumeSeeMore();
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        ((HomeActivity) getActivity()).setDetailFragment(PerfumeSeeMore);


        transaction.setCustomAnimations(
                R.anim.zoom_in,
                0,
                R.anim.pop_zoom_in,
                0
        );
        transaction.hide(this);
        transaction.add(R.id.fragment_container, PerfumeSeeMore, "PerfumeSeeMore");
        transaction.addToBackStack(null); // Cho phép bấm back để quay lại
        transaction.commit();
    }

    private void openBrandSeeMore() {
        Fragment BrandSeeMore = new BrandSeeMore();
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        ((HomeActivity) getActivity()).setDetailFragment(BrandSeeMore);
        transaction.setCustomAnimations(
                R.anim.zoom_in,
                0,
                R.anim.pop_zoom_in,
                0
        );
        transaction.hide(this);
        transaction.add(R.id.fragment_container, BrandSeeMore);
        transaction.addToBackStack(null); // Cho phép bấm back để quay lại
        transaction.commit();
    }

    private void openNoteSeeMore() {
        Fragment NoteSeeMore = new NoteSeeMore();
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        ((HomeActivity) getActivity()).setDetailFragment(NoteSeeMore);
        transaction.setCustomAnimations(
                R.anim.zoom_in,
                0,
                R.anim.pop_zoom_in,
                0
        );
        transaction.hide(this);
        transaction.add(R.id.fragment_container, NoteSeeMore);
        transaction.addToBackStack(null); // Cho phép bấm back để quay lại
        transaction.commit();
    }


}

