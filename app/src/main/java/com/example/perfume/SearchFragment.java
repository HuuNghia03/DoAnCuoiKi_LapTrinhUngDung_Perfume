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

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView parentRecyclerView;
    private List<SearchSection<?>> parentItems = new ArrayList<>();
    private SearchSectionAdapter sectionAdapter;
    private AppDatabase appDatabase;
    private int currentPosition = 0;
    private boolean isForward = true;
    final Handler handler = new Handler();


    // Flag to check if the fragment is loaded
    private static boolean isLoaded = false;
    private NestedScrollView contentContainer;
    private ShimmerFrameLayout shimmerLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        contentContainer = view.findViewById(R.id.contentContainer);
        parentRecyclerView = view.findViewById(R.id.recyclerViewParent);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        appDatabase = AppDatabase.getInstance(requireContext());

        sectionAdapter = new SearchSectionAdapter(getContext(), getParentFragmentManager(), parentItems);
        parentRecyclerView.setAdapter(sectionAdapter);


        // Load dữ liệu từ Room
        shimmerLayout.setVisibility(View.VISIBLE); // Đảm bảo shimmer được hiển thị
        contentContainer.setVisibility(View.GONE); // Ẩn RecyclerView
        shimmerLayout.startShimmer();
        if (!isLoaded) {

            // Log thông báo khi bắt đầu load dữ liệu
            Log.e("TAG", "Data not loaded yet. Loading perfumes from Room...");
            loadPerfumeFromRoom();  // Gọi hàm load dữ liệu từ Room
            isLoaded = true;  // Đánh dấu là đã load
            Log.e("TAG", "Data loaded. Setting isLoaded to true.");
        } else {
            // Log khi không cần load lại dữ liệu
            Log.e("TAG", "Data already loaded. Skipping reload.");
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                shimmerLayout.setVisibility(View.GONE);
                contentContainer.setVisibility(View.VISIBLE);
                shimmerLayout.stopShimmer();
                Log.e("TAG", "UI updated after skipping data load.");
            }, 1000);
        }

        return view;
    }

    // Dừng cuộn tự động khi Fragment không hoạt động
    @Override
    public void onPause() {
        super.onPause();
    }

    // Tiếp tục cuộn tự động khi Fragment trở lại
    @Override
    public void onResume() {
        super.onResume();
        // loadPerfumeFromRoom();
    }


    private void loadPerfumeFromRoom() {

        new Thread(() -> {
            // Thực hiện tải dữ liệu trong background thread
            List<PerfumeEntity> perfumeList = appDatabase.perfumeDao().getAllPerfumes();
            List<BrandEntity> brandList = appDatabase.BrandDao().getAllBrandsWithImage();
            List<Note> noteList = appDatabase.noteDao().getAllNotes();

            List<PerfumeEntity> topPerfumes = perfumeList.size() > 10 ? perfumeList.subList(0, 10) : perfumeList;
            List<BrandEntity> topBrands = brandList.size() > 10 ? brandList.subList(0, 10) : brandList;
            List<Note> topNotes = noteList.size() > 5 ? noteList.subList(0, 5) : noteList;

            requireActivity().runOnUiThread(() -> {

                // Tắt shimmer và hiển thị nội dung chính
                parentItems.clear();
                parentItems.add(new SearchSection<>("EXPLORE BY PERFUMES", topPerfumes));
                parentItems.add(new SearchSection<>("EXPLORE BY BRANDS", topBrands));
                parentItems.add(new SearchSection<>("EXPLORE BY NOTES", topNotes));
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);
                    contentContainer.setVisibility(View.VISIBLE);
                    sectionAdapter.notifyDataSetChanged();
                }, 500); // 1000ms = 1 giây


            });

        }).start();
    }


}

