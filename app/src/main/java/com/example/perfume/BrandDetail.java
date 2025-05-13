package com.example.perfume;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class BrandDetail extends Fragment {
    TextView textPerfumesTab, textInforTab, dotPerfumes, dotInformation, brandName, brandDetailTitle;
    ImageView brandBanner, brandLogo;
    Button gotoWebsite;
    private LinearLayout perfumesTabLayout, inforTabLayout, brandContainer;
    private ShimmerFrameLayout shimmerLayout;

    private AppDatabase appDatabase;

    private String name, banner, logo, founded, founder, country, notablePerfumes, segment, popularity, style, link, description;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brand_detail, container, false);

        bindViews(view);
        ImageView btnBack = view.findViewById(R.id.buttonBack);
        appDatabase = AppDatabase.getInstance(requireContext());

        // Font chữ
        Typeface optima_medium = ResourcesCompat.getFont(requireContext(), R.font.optima_medium);
        Typeface optima = ResourcesCompat.getFont(requireContext(), R.font.optima);

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Lấy dữ liệu brand từ argument
        BrandEntity brand = (BrandEntity) getArguments().getSerializable("brand");
        if (brand != null) {
            name = brand.getName();
            banner = brand.getBanner();
            logo = brand.getLogo();
            founded = brand.getFounded();
            founder = brand.getFounder();
            country = brand.getCountry();
            notablePerfumes = brand.getNotablePerfumes();
            segment = brand.getSegment();
            popularity = brand.getPopularity();
            style = brand.getStyle();
            link = brand.getLink();
            description = brand.getDescription();

            // Hiển thị hình ảnh và tên thương hiệu
            Glide.with(this)
                    .load(logo)
                    .transition(DrawableTransitionOptions.withCrossFade(500)) // 500ms
                    .into(brandLogo);

            Glide.with(this)
                    .load(banner)
                    .transition(DrawableTransitionOptions.withCrossFade(500)) // 500ms

                    .into(brandBanner);

            brandName.setText(name);

            shimmerLayout.startShimmer();
            shimmerLayout.setVisibility(View.VISIBLE);
            brandContainer.setVisibility(View.GONE);

            loadPerfume(); // load lần đầu
            loadInfor();   // load sẵn thông tin nhưng ẩn
            inforTabLayout.setVisibility(View.GONE);
            brandDetailTitle.setVisibility(View.VISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                brandContainer.setVisibility(View.VISIBLE);
            }, 500); // 1000ms = 1 giây


        }
        String fixedLink = link;
        if (fixedLink != null && !fixedLink.isEmpty() && !fixedLink.startsWith("http://") && !fixedLink.startsWith("https://")) {
            fixedLink = "http://" + fixedLink;
        }
        final String finalLink = fixedLink;
        // Xử lý nút mở website
        gotoWebsite.setOnClickListener(v -> {
            if (finalLink != null && !finalLink.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalLink));
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Website not available", Toast.LENGTH_SHORT).show();
            }
        });

        // Chuyển tab sang Thông tin
        textPerfumesTab.setOnClickListener(v -> {
            brandDetailTitle.setVisibility(View.VISIBLE);
            perfumesTabLayout.setVisibility(View.VISIBLE);
            inforTabLayout.setVisibility(View.GONE);
            dotPerfumes.setVisibility(View.VISIBLE);
            dotInformation.setVisibility(View.INVISIBLE);
            textPerfumesTab.setTextColor(Color.BLACK);
            textPerfumesTab.setTypeface(optima_medium);
            textInforTab.setTextColor(Color.parseColor("#76777D"));
            textInforTab.setTypeface(optima);
        });

        textInforTab.setOnClickListener(v -> {
            brandDetailTitle.setVisibility(View.GONE);
            perfumesTabLayout.setVisibility(View.GONE);
            inforTabLayout.setVisibility(View.VISIBLE);
            dotPerfumes.setVisibility(View.INVISIBLE);
            dotInformation.setVisibility(View.VISIBLE);
            textInforTab.setTextColor(Color.BLACK);
            textInforTab.setTypeface(optima_medium);
            textPerfumesTab.setTextColor(Color.parseColor("#76777D"));
            textPerfumesTab.setTypeface(optima);
        });

        return view;
    }


    void loadPerfume() {
        perfumesTabLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        new Thread(() -> {
            List<PerfumeEntity> perfumes = appDatabase.perfumeDao().getPerfumesByBrand(name);

            requireActivity().runOnUiThread(() -> {
                for (PerfumeEntity perfume : perfumes) {
                    View itemView = inflater.inflate(R.layout.home_item, perfumesTabLayout, false);
                    ImageView img = itemView.findViewById(R.id.image);
                    TextView title = itemView.findViewById(R.id.name);
                    TextView brand = itemView.findViewById(R.id.brand);
                    ImageView banner = itemView.findViewById(R.id.bannerImage);
                    TextView affinity = itemView.findViewById(R.id.affinity);
                    TextView concentration = itemView.findViewById(R.id.concentration);
                    Button btnAddCart = itemView.findViewById(R.id.btAddCart);
                    btnAddCart.setText("ADD TO CART");
                    Glide.with(this).load(perfume.getImg()).into(img);
                    Glide.with(this).load(perfume.getImgs()).into(banner);
                    title.setText(perfume.getName());
                    brand.setText(perfume.getBrand().toUpperCase());
                    int random = new Random().nextInt(31) + 50;
                    affinity.setText(random + "%");
                    concentration.setText(perfume.getConcentration());
                    List<Float> priceList = new ArrayList<>();
                    for (String s : perfume.getPrices().split(",")) {
                        priceList.add(Float.parseFloat(s.trim()));
                    }
                    List<Integer> volumeList = new ArrayList<>();
                    for (String s : perfume.getVolumes().split(",")) {
                        volumeList.add(Integer.parseInt(s.trim()));
                    }
                    btnAddCart.setOnClickListener(v -> {
                        CartManager.showAddToCartDialog(requireContext(), perfume, volumeList, priceList);
                    });
                    itemView.setOnClickListener(v -> {
                        Navigator.openPerfumeDetail1((AppCompatActivity) v.getContext(), perfume, this);
                    });
                    perfumesTabLayout.addView(itemView);

                    // ✨ Hiệu ứng mờ dần
                    AlphaAnimation animation = new AlphaAnimation(0f, 1f);
                    animation.setDuration(500); // Thời gian hiệu ứng 500ms
                    itemView.startAnimation(animation);
                }
            });
        }).start();
    }

    void loadInfor() {
        inforTabLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());


        new Thread(() -> {

            requireActivity().runOnUiThread(() -> {

                View itemView = inflater.inflate(R.layout.brand_detail_infor, inforTabLayout, false);
                TextView tvCountry = itemView.findViewById(R.id.tvCountry);
                TextView tvFounded = itemView.findViewById(R.id.tvFounded);
                TextView tvFounder = itemView.findViewById(R.id.tvFounder);
                TextView tvDescription = itemView.findViewById(R.id.tvDescription);
                TextView tvNotable = itemView.findViewById(R.id.tvNotable);
                TextView tvSegment = itemView.findViewById(R.id.tvSegment);
                TextView tvPopularity = itemView.findViewById(R.id.tvPopularity);
                TextView tvStyle = itemView.findViewById(R.id.tvStyle);
                TextView tvWebsite = itemView.findViewById(R.id.tvWebsite);
                tvFounded.setText("Founded: " + founded);
                tvCountry.setText("Country: " + country);
                tvFounder.setText("Founder: " + founder);
                tvDescription.setText(description);
                tvNotable.setText("Notable: " + notablePerfumes);
                tvSegment.setText("Segment: " + segment);
                tvPopularity.setText("Popularity: " + popularity);
                tvStyle.setText("Style: " + style);
                tvWebsite.setText("Website: " + link);

                inforTabLayout.addView(itemView);

                // ✨ Hiệu ứng mờ dần
                AlphaAnimation animation = new AlphaAnimation(0f, 1f);
                animation.setDuration(500); // Thời gian hiệu ứng 500ms
                itemView.startAnimation(animation);

            });
        }).start();
    }

    private void bindViews(View view) {
        perfumesTabLayout = view.findViewById(R.id.perfumesTabLayout);
        inforTabLayout = view.findViewById(R.id.inforTabLayout);
        brandContainer = view.findViewById(R.id.brandContainer);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        textPerfumesTab = view.findViewById(R.id.textPerfumesTab);
        textInforTab = view.findViewById(R.id.textInforTab);
        dotPerfumes = view.findViewById(R.id.dotPerfumes);
        dotInformation = view.findViewById(R.id.dotInformation);
        brandBanner = view.findViewById(R.id.brandBanner);
        brandLogo = view.findViewById(R.id.brandLogo);
        brandName = view.findViewById(R.id.brandName);
        gotoWebsite = view.findViewById(R.id.gotoWebsite);
        brandDetailTitle = view.findViewById(R.id.brandDetailTitle);
    }

}
