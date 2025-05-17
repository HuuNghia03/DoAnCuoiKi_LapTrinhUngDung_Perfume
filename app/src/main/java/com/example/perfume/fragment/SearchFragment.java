package com.example.perfume.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfume.AppDatabase;
import com.example.perfume.R;
import com.example.perfume.UserViewModel;
import com.example.perfume.activity.HomeActivity;
import com.example.perfume.adapter.BrandAdapter;
import com.example.perfume.adapter.NoteAdapter;
import com.example.perfume.adapter.PerfumeAdapter;
import com.example.perfume.entity.BrandEntity;
import com.example.perfume.entity.NoteEntity;
import com.example.perfume.entity.PerfumeEntity;
import com.example.perfume.Navigator;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class SearchFragment extends Fragment {

    private int userId;
    private AppDatabase appDatabase;
    private LinearLayout itemsContainer;
    private ShimmerFrameLayout shimmerLayout;
    private ViewGroup contentContainer;
    private TextView nameUser;

    private UserViewModel userViewModel;

    private PerfumeAdapter perfumeAdapter;
    private BrandAdapter brandAdapter;
    private NoteAdapter noteAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        nameUser = view.findViewById(R.id.nameUser);
        itemsContainer = view.findViewById(R.id.items_container);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        contentContainer = view.findViewById(R.id.contentContainer);

        appDatabase = AppDatabase.getInstance(requireContext());
        userId = Navigator.getUserId(getContext());
        Log.e("HomeFragment", "onCreateView called");

        setupUserViewModel();

        showLoading();

        loadPerfume();

        return view;
    }

    private void setupUserViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setUserId(userId);

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                String fullName = user.getFirstname() + " " + user.getLastname();
                nameUser.setText(fullName);
            }
        });
    }

    private void showLoading() {
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();
        contentContainer.setVisibility(View.GONE);
    }

    private void hideLoading() {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        contentContainer.setVisibility(View.VISIBLE);
    }

    private void loadPerfume() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        new Thread(() -> {
            List<PerfumeEntity> perfumeList = appDatabase.perfumeDao().getAllPerfumes();
            List<BrandEntity> brandList = appDatabase.BrandDao().getAllBrandsWithImage();
            List<NoteEntity> noteList = appDatabase.noteDao().getAllNotes();

            List<PerfumeEntity> topPerfumes = perfumeList.size() > 10 ? perfumeList.subList(0, 10) : perfumeList;
            List<BrandEntity> topBrands = brandList.size() > 10 ? brandList.subList(0, 10) : brandList;
            List<NoteEntity> topNotes = noteList.size() > 5 ? noteList.subList(0, 5) : noteList;

            perfumeAdapter = new PerfumeAdapter(requireContext(), topPerfumes, -1, this, false);
            brandAdapter = new BrandAdapter(requireContext(), topBrands, this, false);
            noteAdapter = new NoteAdapter(requireContext(), topNotes, this, false);

            requireActivity().runOnUiThread(() -> {
                itemsContainer.removeAllViews();

                addSection(inflater, "FRAGRANCE COLLECTION", perfumeAdapter, v -> openPerfumeSeeMore());
                addSection(inflater, "FRAGRANCE BRANDS", brandAdapter, v -> openBrandSeeMore());
                addSection(inflater, "FRAGRANCE NOTES", noteAdapter, v -> openNoteSeeMore());

                new Handler(Looper.getMainLooper()).postDelayed(this::hideLoading, 500);
            });
        }).start();
    }

    private void addSection(LayoutInflater inflater, String title, RecyclerView.Adapter<?> adapter, View.OnClickListener seeMoreClick) {
        View section = inflater.inflate(R.layout.search_section, itemsContainer, false);

        TextView tvTitle = section.findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        RecyclerView recyclerView = section.findViewById(R.id.itemRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        section.findViewById(R.id.textSeemore).setOnClickListener(seeMoreClick);

        itemsContainer.addView(section);
    }

    private void openPerfumeSeeMore() {
        openSeeMoreFragment(new PerfumeSeeMoreFragment(), "PerfumeSeeMore");
    }

    private void openBrandSeeMore() {
        openSeeMoreFragment(new BrandSeeMoreFragment(), "BrandSeeMore");
    }

    private void openNoteSeeMore() {
        openSeeMoreFragment(new NoteSeeMoreFragment(), "NoteSeeMore");
    }

    private void openSeeMoreFragment(Fragment fragment, String tag) {
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        ((HomeActivity) getActivity()).setDetailFragment(fragment);

        transaction.setCustomAnimations(R.anim.zoom_in, 0, R.anim.pop_zoom_in, 0);
        transaction.hide(this);
        transaction.add(R.id.fragment_container, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
