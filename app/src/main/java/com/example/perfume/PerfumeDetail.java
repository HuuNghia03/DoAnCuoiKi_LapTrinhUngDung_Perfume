package com.example.perfume;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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
    ImageView imageTop, imageHeart, imageBase;


    // Danh sách nhóm hương
    String[] categories = {"Floral", "Woody", "Citrus", "Oriental", "Fresh", "Sweet"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.perfume_detail, container, false);
        appDatabase = AppDatabase.getInstance(requireContext());

        Button btnAddCart = view.findViewById(R.id.btAddCart);
        ImageView btnBack = view.findViewById(R.id.buttonBack);

        //Biểu đồ olfatory
        RadarChart radarChart = view.findViewById(R.id.radarChart);

// 1. Gán dữ liệu
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
// 2. Thiết lập dữ liệu
        RadarData data = new RadarData(dataSet);
        radarChart.setData(data);
// 3. Cấu hình hiển thị
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


        ScrollView scrollView = view.findViewById(R.id.scrollView);
        btnBack.setOnClickListener(v -> {
            // Quay lại fragment trước đó
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        PerfumeEntity perfume = (PerfumeEntity) getArguments().getSerializable("perfume");


        if (perfume != null) {
            // Lấy các view
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

            // Set dữ liệu
            name.setText(perfume.getName());
            namehint.setText(perfume.getName());
            brand.setText(perfume.getBrand());
            List<Float> priceList = new ArrayList<>();
            for (String s : perfume.getPrices().split(",")) {
                priceList.add(Float.parseFloat(s.trim()));
            }

            if (!priceList.isEmpty()) {
                float minPrice = priceList.get(0);
                float maxPrice = priceList.get(priceList.size() - 1);
                price.setText("$" + minPrice + " - $" + maxPrice);
            }
            List<Integer> volumeList = new ArrayList<>();
            for (String s : perfume.getVolumes().split(",")) {
                volumeList.add(Integer.parseInt(s.trim()));
            }
            year.setText(String.valueOf(perfume.getYear()));
            olfactory.setText(perfume.getOlfactory());
            topNote.setText(perfume.getTop());
            heartNote.setText(perfume.getHeart());
            baseNote.setText(perfume.getBase());
            description.setText(perfume.getDescription());
            perfumer.setText(perfume.getDesigners());
            Glide.with(this).load(perfume.getImg()).into(imagePerfume);
            Glide.with(this).load(perfume.getImgs()).into(imageBanner);

            String Gender = perfume.getGender();
            if (Gender != null) {
                switch (Gender) {
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
            gender.setText(Gender);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                com.example.perfume.Note top = appDatabase.noteDao().findCategoryByNote(perfume.getTop());
                com.example.perfume.Note heart = appDatabase.noteDao().findCategoryByNote(perfume.getHeart());
                com.example.perfume.Note base = appDatabase.noteDao().findCategoryByNote(perfume.getBase());

                // Cập nhật UI phải chạy trên main thread
                requireActivity().runOnUiThread(() -> {
                    if (top != null)
                        Glide.with(requireContext()).load(top.getImageUrl()).into(imageTop);
                    if (heart != null)
                        Glide.with(requireContext()).load(heart.getImageUrl()).into(imageHeart);
                    if (base != null)
                        Glide.with(requireContext()).load(base.getImageUrl()).into(imageBase);
                });
            });
            btnAddCart.setOnClickListener(v -> {
                CartManager.showAddToCartDialog(requireContext(), perfume, volumeList, priceList);
            });
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {
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
                }
            });

        }


        return view;
    }


}
