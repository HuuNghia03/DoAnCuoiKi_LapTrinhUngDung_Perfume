package com.example.perfume;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.perfume.activities.HomeActivity;
import com.example.perfume.entities.NoteEntity;
import com.example.perfume.entities.PerfumeEntity;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PerfumeDetail extends Fragment {

    private AppDatabase appDatabase;
    private ImageView imageTop, imageHeart, imageBase,btnBack;
    private Button btnAddCart;
    private ScrollView scrollView;
    private  RadarChart radarChart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.perfume_detail, container, false);

        appDatabase = AppDatabase.getInstance(requireContext());

        // Các view
        btnAddCart = view.findViewById(R.id.btnAddCart);
        btnBack = view.findViewById(R.id.buttonBack);
        scrollView = view.findViewById(R.id.scrollView);
        radarChart = view.findViewById(R.id.radarChart);

        setupRadarChart(radarChart);
        setupBackPressedHandler();
        setupBtnBack(btnBack);
        setupPerfumeData(view);
        setupScrollViewBehavior(scrollView, view);

        return view;
    }

    private void setupRadarChart(RadarChart radarChart) {
        String[] categories = {"Citrus", "Green", "Sweet", "Fruity", "Floral", "Spicy", "Woody", "Animalic"};
        float[] values = {4f, 3f, 2f, 1f, 2f, 1f, 4f, 2f};

        List<RadarEntry> entries = new ArrayList<>();
        for (float v : values) {
            entries.add(new RadarEntry(v));
        }

        RadarDataSet dataSet = new RadarDataSet(entries, "Olfactive Profile");
        dataSet.setColor(Color.WHITE);
        dataSet.setFillColor(Color.WHITE);
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(80);
        dataSet.setLineWidth(2f);
        dataSet.setDrawHighlightCircleEnabled(true);
        dataSet.setDrawValues(false);

        RadarData data = new RadarData(dataSet);
        radarChart.setData(data);

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return categories[(int) value % categories.length];
            }
        });
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12f);

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(5f);
        yAxis.setDrawLabels(false);
        yAxis.setLabelCount(5, true);

        radarChart.getDescription().setEnabled(false);
        radarChart.getLegend().setEnabled(false);
        radarChart.invalidate();
    }

    private void setupBackPressedHandler() {
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        ((HomeActivity) requireActivity()).setDetailFragment(
                                getParentFragmentManager().findFragmentByTag("PerfumeSeeMore"));
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                });
    }

    private void setupBtnBack(ImageView btnBack) {
        btnBack.setOnClickListener(v -> {
            ((HomeActivity) requireActivity()).setDetailFragment(
                    getParentFragmentManager().findFragmentByTag("PerfumeSeeMore"));
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setupPerfumeData(View view) {
        PerfumeEntity perfume = (PerfumeEntity) getArguments().getSerializable("perfume");

        if (perfume == null) return;

        TextView name = view.findViewById(R.id.textPerfumeName);
        TextView namehint = view.findViewById(R.id.textPerfumeNameHint);
        TextView brand = view.findViewById(R.id.textBrand);
        ImageView imagePerfume = view.findViewById(R.id.imagePerfume);
        ImageView imageBanner = view.findViewById(R.id.imageBanner);
        ImageView imageGender = view.findViewById(R.id.imageGender);
        TextView price = view.findViewById(R.id.textPrice);
        TextView gender = view.findViewById(R.id.textGender);
        TextView year = view.findViewById(R.id.textYear);
        TextView olfactory = view.findViewById(R.id.textOlfactory);
        TextView topNote = view.findViewById(R.id.textTopNote);
        TextView heartNote = view.findViewById(R.id.textHeartNote);
        TextView baseNote = view.findViewById(R.id.textBaseNote);
        TextView description = view.findViewById(R.id.textDescription);
        TextView perfumer = view.findViewById(R.id.textPerfumer);
        imageTop = view.findViewById(R.id.imageTopNote);
        imageBase = view.findViewById(R.id.imageBaseNote);
        imageHeart = view.findViewById(R.id.imageHeartNote);
        View hintHeader = view.findViewById(R.id.hintHeader);
        View linespace = view.findViewById(R.id.linespace);

        // Gán dữ liệu text
        name.setText(perfume.getName());
        namehint.setText(perfume.getName());
        brand.setText(perfume.getBrand());
        year.setText(String.valueOf(perfume.getYear()));
        olfactory.setText(perfume.getOlfactory());
        topNote.setText(perfume.getTop());
        heartNote.setText(perfume.getHeart());
        baseNote.setText(perfume.getBase());
        description.setText(perfume.getDescription());
        perfumer.setText(perfume.getDesigners());

        // Giá tiền
        List<Float> priceList = new ArrayList<>();
        for (String s : perfume.getPrices().split(",")) {
            priceList.add(Float.parseFloat(s.trim()));
        }
        if (!priceList.isEmpty()) {
            float minPrice = priceList.get(0);
            float maxPrice = priceList.get(priceList.size() - 1);
            price.setText("$" + minPrice + " - $" + maxPrice);
        }

        // Khối lượng chai
        List<Integer> volumeList = new ArrayList<>();
        for (String s : perfume.getVolumes().split(",")) {
            volumeList.add(Integer.parseInt(s.trim()));
        }

        // Giới tính và icon
        String genderStr = perfume.getGender();
        if (genderStr != null) {
            switch (genderStr) {
                case "Men":
                    imageGender.setImageResource(R.drawable.ic_male);
                    break;
                case "Women":
                    imageGender.setImageResource(R.drawable.ic_female);
                    break;
                default:
                    imageGender.setImageResource(R.drawable.ic_unisex);
                    break;
            }
        }
        gender.setText(genderStr);

        // Load ảnh
        Glide.with(this).load(perfume.getImg()).into(imagePerfume);
        Glide.with(this).load(perfume.getImgs()).into(imageBanner);

        // Load ảnh nhóm hương từ database nền tảng đa luồng
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            NoteEntity top = appDatabase.noteDao().findCategoryByNote(perfume.getTop());
            NoteEntity heart = appDatabase.noteDao().findCategoryByNote(perfume.getHeart());
            NoteEntity base = appDatabase.noteDao().findCategoryByNote(perfume.getBase());

            requireActivity().runOnUiThread(() -> {
                if (top != null)
                    Glide.with(requireContext()).load(top.getImageUrl()).into(imageTop);
                if (heart != null)
                    Glide.with(requireContext()).load(heart.getImageUrl()).into(imageHeart);
                if (base != null)
                    Glide.with(requireContext()).load(base.getImageUrl()).into(imageBase);
            });
        });

        // Thêm nút thêm giỏ hàng
        btnAddCart.setOnClickListener(v -> {
            CartManager.showAddToCartDialog(requireContext(), perfume, volumeList, priceList);
        });
    }

    private void setupScrollViewBehavior(ScrollView scrollView, View rootView) {
        TextView namehint = rootView.findViewById(R.id.textPerfumeNameHint);
        View hintHeader = rootView.findViewById(R.id.hintHeader);
        View linespace = rootView.findViewById(R.id.linespace);
        ImageView imagePerfume = rootView.findViewById(R.id.imagePerfume);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int[] location = new int[2];
            imagePerfume.getLocationOnScreen(location);
            int y = location[1];

            int[] headerLocation = new int[2];
            hintHeader.getLocationOnScreen(headerLocation);
            int headerBottom = headerLocation[1] + hintHeader.getHeight();

            if (y <= headerBottom) {
                namehint.setVisibility(View.VISIBLE);
                hintHeader.setBackgroundColor(Color.parseColor("#FFFFFF"));
                linespace.setVisibility(View.VISIBLE);
            } else {
                namehint.setVisibility(View.INVISIBLE);
                hintHeader.setBackground(null);
                linespace.setVisibility(View.INVISIBLE);
            }
        });
    }
}
