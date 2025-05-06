package com.example.perfume;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView parentRecyclerView;
    private List<SearchSection<?>> parentItems = new ArrayList<>();
    private SearchSectionAdapter sectionAdapter;
    private PerfumeDatabase perfumeDatabase;
    private int currentPosition = 0;
    private boolean isForward = true;
    final Handler handler = new Handler();
    private Runnable autoScrollRunnable;


    private LinearLayoutManager layoutManagerBanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        parentRecyclerView = view.findViewById(R.id.recyclerViewParent);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        perfumeDatabase = PerfumeDatabase.getInstance(requireContext());
        ImageView btnCart=view.findViewById(R.id.btnCart);

        // ⭐ Gán adapter
        sectionAdapter = new SearchSectionAdapter(getContext(), getParentFragmentManager(), parentItems);
        parentRecyclerView.setAdapter(sectionAdapter);

        // Cấu hình banner
        RecyclerView recyclerViewBanner = view.findViewById(R.id.recyclerViewBanner);
        layoutManagerBanner = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewBanner.setLayoutManager(layoutManagerBanner);

        List<com.example.perfume.BannerItem> bannerList = new ArrayList<>();
        bannerList.add(new com.example.perfume.BannerItem(R.drawable.banner_perfume4));
        bannerList.add(new com.example.perfume.BannerItem(R.drawable.banner_perfume1));
        bannerList.add(new com.example.perfume.BannerItem(R.drawable.banner_perfume2));
        bannerList.add(new com.example.perfume.BannerItem(R.drawable.banner_perfume3));
        com.example.perfume.BannerAdapter bannerAdapter = new com.example.perfume.BannerAdapter(bannerList);
        recyclerViewBanner.setAdapter(bannerAdapter);

        startAutoScroll();
        loadPerfumeFromRoom();

        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), com.example.perfume.CartActivity.class);
            startActivity(intent);
        });
        return view;
    }

    // Gọi khi cần dừng
    private void stopAutoScroll() {
        if (handler != null && autoScrollRunnable != null) {
            handler.removeCallbacks(autoScrollRunnable);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        stopAutoScroll();
    }
    @Override
    public void onResume() {
        super.onResume();
        // Gọi lại loadPerfumeFromRoom() khi Fragment trở thành visible
        loadPerfumeFromRoom();
    }


    // Hàm để cuộn mượt mà
    private void smoothScrollToPosition(int position) {
        // Tạo một LinearSmoothScroller tùy chỉnh
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getContext()) {
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 0.2f; // Điều chỉnh tốc độ cuộn (giảm giá trị này để cuộn mượt hơn)
            }
        };
        smoothScroller.setTargetPosition(position);
        layoutManagerBanner.startSmoothScroll(smoothScroller); // Bắt đầu cuộn mượt mà
    }

    private void loadPerfumeFromRoom() {
        new Thread(() -> {
            // Thực hiện tải dữ liệu trong background thread
            List<PerfumeEntity> perfumeList = perfumeDatabase.perfumeDao().getAllPerfumes();
            List<BrandEntity> brandList = perfumeDatabase.BrandDao().getAllBrandsWithImage();
            List<Note> noteList = perfumeDatabase.noteDao().getAllNotes();

            List<PerfumeEntity> topPerfumes = perfumeList.size() > 10 ? perfumeList.subList(0, 10) : perfumeList;
            List<BrandEntity> topBrands = brandList.size() > 10 ? brandList.subList(0, 10) : brandList;
            List<Note> topNotes = noteList.size() > 5 ? noteList.subList(0, 5) : noteList;

            requireActivity().runOnUiThread(() -> {
                // Sau khi tải xong, cập nhật lại RecyclerView và ẩn ProgressBar
                parentItems.clear();
                parentItems.add(new SearchSection<>("EXPLORE BY PERFUMES", topPerfumes));
                parentItems.add(new SearchSection<>("EXPLORE BY BRANDS", topBrands));
                parentItems.add(new SearchSection<>("EXPLORE BY NOTES", topNotes));
                sectionAdapter.notifyDataSetChanged();

                // Ẩn ProgressBar khi dữ liệu đã được tải xong

            });
        }).start();
    }
    private void startAutoScroll() {
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (isForward) {
                    currentPosition++;
                    if (currentPosition == 3) { // 3 = bannerList.size() - 1
                        isForward = false;
                    }
                } else {
                    currentPosition--;
                    if (currentPosition == 0) {
                        isForward = true;
                    }
                }
                smoothScrollToPosition(currentPosition);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(autoScrollRunnable, 3000);
    }

}
