package com.example.perfume;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class HomeFragment extends Fragment {
    private LinearLayout itemsContainer;
    private int userId;
    UserRepository userRepository;
    private AppDatabase db;
    private List<PerfumeEntity> perfumeEntityList;
    private List<String> categories;
    private NestedScrollView contentContainer;
    private ShimmerFrameLayout shimmerLayout;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        itemsContainer = view.findViewById(R.id.home_items_container);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        contentContainer = view.findViewById(R.id.contentContainer);

        db = AppDatabase.getInstance(getContext());
        userId = Navigator.getUserId(requireContext());
        userRepository = new UserRepository(getContext());


        // CHỈ load nếu chưa có item nào trong itemsContainer
        if (itemsContainer.getChildCount() == 0) {


            userRepository.getOlfactiveByUserId(userId, olfactive -> {
                if (olfactive != null) {
                    categories = Arrays.asList(olfactive.split(","));
                    shimmerLayout.startShimmer();
                    shimmerLayout.setVisibility(View.VISIBLE);
                    contentContainer.setVisibility(View.GONE);
                    loadPerfumes(categories);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        shimmerLayout.stopShimmer();
                        shimmerLayout.setVisibility(View.GONE);
                        contentContainer.setVisibility(View.VISIBLE);
                    }, 500); // 1000ms = 1 giây
                }
            });
        }

        return view;
    }


    public void loadPerfumes(List<String> categories) {

        itemsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        new Thread(() -> {
            // Ánh xạ category sang list notes
            Map<String, List<String>> categoryToNotes = new HashMap<>();
            categoryToNotes.put("Fruity", Arrays.asList("Apple"));
            categoryToNotes.put("Citrus", Arrays.asList("Lemon"));
            categoryToNotes.put("Herbal", Arrays.asList("Mint"));
            categoryToNotes.put("Aquatic", Arrays.asList("Seaweed"));
            categoryToNotes.put("Woody", Arrays.asList("Cedar"));
            categoryToNotes.put("Powdery", Arrays.asList("Iris"));
            categoryToNotes.put("Floral", Arrays.asList("Jasmine"));
            Set<PerfumeEntity> result = new HashSet<>();
            for (String category : categories) {
                List<String> notes = categoryToNotes.get(category.trim());
                if (notes != null) {
                    for (String note : notes) {
                        result.addAll(db.perfumeDao().getPerfumesByTopNote(note));
                        result.addAll(db.perfumeDao().getPerfumesByHeartNote(note));
                        result.addAll(db.perfumeDao().getPerfumesByBaseNote(note));
                    }
                }
            }
            perfumeEntityList = new ArrayList<>(result).subList(0, Math.min(result.size(), 5));

            requireActivity().runOnUiThread(() -> {
                for (PerfumeEntity perfume : perfumeEntityList) {
                    View itemView = inflater.inflate(R.layout.home_item, itemsContainer, false);
                    ImageView img = itemView.findViewById(R.id.image);
                    TextView title = itemView.findViewById(R.id.name);
                    TextView brand = itemView.findViewById(R.id.brand);
                    ImageView banner = itemView.findViewById(R.id.bannerImage);
                    TextView affinity = itemView.findViewById(R.id.affinity);

                    TextView concentration = itemView.findViewById(R.id.concentration);

                    Glide.with(this).load(perfume.getImg()).into(img);
                    Glide.with(this).load(perfume.getImgs()).into(banner);
                    title.setText(perfume.getName());
                    brand.setText(perfume.getBrand().toUpperCase());
                    int random = new Random().nextInt(31) + 50; // Tạo số từ 50 đến 80 (80 - 50 + 1 = 31)
                    affinity.setText(random + "%");

                    concentration.setText(perfume.getConcentration());

                    itemsContainer.addView(itemView);
                    AlphaAnimation animation = new AlphaAnimation(0f, 1f);
                    animation.setDuration(500); // Thời gian hiệu ứng 500ms
                    itemView.startAnimation(animation);
                }



            });


        }).start();
    }
}
