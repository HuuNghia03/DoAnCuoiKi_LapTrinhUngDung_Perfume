package com.example.perfume;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.perfume.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PerfumeDetail extends Fragment {
    private com.example.perfume.PerfumeDatabase perfumeDatabase;
    ImageView imageTop,imageHeart,imageBase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.perfume_detail, container, false);
        perfumeDatabase = com.example.perfume.PerfumeDatabase.getInstance(requireContext());

        Bundle bundle = getArguments();
        ImageView btnBack = view.findViewById(R.id.buttonBack);
        ScrollView scrollView = view.findViewById(R.id.scrollView);
        btnBack.setOnClickListener(v -> {
            // Quay lại fragment trước đó
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        if (bundle != null) {
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
            imageTop=view.findViewById(R.id.imageTopNote);
            imageBase=view.findViewById(R.id.imageBaseNote);
            imageHeart=view.findViewById(R.id.imageHeartNote);
            View hintHeader =view.findViewById(R.id.hintHeader);
            View linespace = view.findViewById(R.id.linespace);

            // Set dữ liệu
            name.setText(bundle.getString("perfumeName"));
            namehint.setText(bundle.getString("perfumeName"));

            brand.setText(bundle.getString("brand"));
            float pricefl= bundle.getFloat("price");
            price.setText(String.valueOf(pricefl)+"$");
            int yearint = bundle.getInt("year");
            year.setText(String.valueOf(yearint));
            olfactory.setText(bundle.getString("olfactory"));
            topNote.setText(bundle.getString("top"));
            heartNote.setText(bundle.getString("heart"));
            baseNote.setText(bundle.getString("base"));
            description.setText(bundle.getString("description"));
            perfumer.setText(bundle.getString("perfumer"));
            Glide.with(this).load(bundle.getString("img")).into(imagePerfume);
            Glide.with(this).load(bundle.getString("imgs")).into(imageBanner);

            String Gender = bundle.getString("gender");
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
                com.example.perfume.Note top = perfumeDatabase.noteDao().findCategoryByNote(bundle.getString("top"));
                com.example.perfume.Note heart = perfumeDatabase.noteDao().findCategoryByNote(bundle.getString("heart"));
                com.example.perfume.Note base = perfumeDatabase.noteDao().findCategoryByNote(bundle.getString("base"));

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
