package com.example.perfume.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.perfume.AppDatabase;
import com.example.perfume.Navigator;
import com.example.perfume.R;
import com.example.perfume.UserRepository;
import com.example.perfume.UserViewModel;
import com.example.perfume.entity.PerfumeEntity;
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
    private UserRepository userRepository;
    private AppDatabase db;
    private NestedScrollView contentContainer;
    private ShimmerFrameLayout shimmerLayout;
    private ImageView purposeIcon;
    private TextView purpose;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        itemsContainer = view.findViewById(R.id.home_items_container);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        contentContainer = view.findViewById(R.id.contentContainer);
        purposeIcon = view.findViewById(R.id.purposeIcon);
        purpose = view.findViewById(R.id.purpose);

        db = AppDatabase.getInstance(getContext());
        userId = Navigator.getUserId(requireContext());
        userRepository = new UserRepository(getContext());

        setupUserViewModel();

        return view;
    }

    private void setupUserViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setUserId(userId);

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                if (user.getPerfumePurpose().equalsIgnoreCase("For gift")) {
                    purposeIcon.setImageResource(R.drawable.ic_gift);
                    purpose.setText("FOR A GIFT");
                } else {
                    purposeIcon.setImageResource(R.drawable.ic_profile_close);
                    purpose.setText("FOR ME");
                }

                // Lấy lại olfactive mới từ database
                userRepository.getOlfactiveByUserId(userId, olfactive -> {
                    if (olfactive != null && !olfactive.isEmpty()) {
                        List<String> notes = Arrays.asList(olfactive.split(","));

                        shimmerLayout.startShimmer();
                        shimmerLayout.setVisibility(View.VISIBLE);
                        contentContainer.setVisibility(View.GONE);

                        loadPerfumes(notes);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            shimmerLayout.stopShimmer();
                            shimmerLayout.setVisibility(View.GONE);
                            contentContainer.setVisibility(View.VISIBLE);
                        }, 500);
                    }
                });
            }
        });
    }

    public void loadPerfumes(List<String> notes) {
        itemsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        new Thread(() -> {
            Map<String, Integer> noteNameToId = new HashMap<>();
            noteNameToId.put("Fruity", 2);
            noteNameToId.put("Citrus", 1);
            noteNameToId.put("Herbal", 17);
            noteNameToId.put("Aquatic", 11);
            noteNameToId.put("Woody", 1);
            noteNameToId.put("Powdery", 10);
            noteNameToId.put("Floral", 4);

            Set<PerfumeEntity> result = new HashSet<>();

            for (String note : notes) {
                Integer noteId = noteNameToId.get(note.trim());
                if (noteId != null) {
                    result.addAll(db.perfumeDao().getPerfumesByTopNoteId(noteId));
                    result.addAll(db.perfumeDao().getPerfumesByHeartNoteId(noteId));
                    result.addAll(db.perfumeDao().getPerfumesByBaseNoteId(noteId));
                }
            }

            List<PerfumeEntity> perfumeEntityList = new ArrayList<>(result)
                    .subList(0, Math.min(result.size(), 5));

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
                    affinity.setText((new Random().nextInt(31) + 50) + "%");
                    concentration.setText(perfume.getConcentration());

                    itemsContainer.addView(itemView);
                    itemView.setOnClickListener(v -> {
                        Navigator.openPerfumeDetail1((AppCompatActivity) v.getContext(), perfume, this);
                    });

                    AlphaAnimation animation = new AlphaAnimation(0f, 1f);
                    animation.setDuration(500);
                    itemView.startAnimation(animation);
                }
            });
        }).start();
    }
}
