package com.example.perfume;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

public class BrandDetail extends Fragment {
    TextView textPerfumesTab, textInforTab, dotPerfumes, dotInformation, brandName;
    ImageView brandBanner, brandLogo;
    Button gotoWebsite;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brand_detail, container, false);
        textPerfumesTab = view.findViewById(R.id.textPerfumesTab);
        textInforTab = view.findViewById(R.id.textInforTab);
        dotPerfumes = view.findViewById(R.id.dotPerfumes);
        dotInformation = view.findViewById(R.id.dotInformation);
        brandBanner = view.findViewById(R.id.brandBanner);
        brandLogo = view.findViewById(R.id.brandLogo);
        brandName = view.findViewById(R.id.brandName);
        gotoWebsite=view.findViewById(R.id.gotoWebsite);

        Bundle bundlePerfume = new Bundle();
        Bundle bundleInfor = new Bundle();
        ImageView btnBack = view.findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(v -> {
            // Quay lại fragment trước đó
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        com.example.perfume.BrandDetailPerfume brandDetailPerfumeFragment = new com.example.perfume.BrandDetailPerfume();
        com.example.perfume.BrandDetailInfor brandDetailInforFragment= new com.example.perfume.BrandDetailInfor();

        textInforTab.setOnClickListener(v -> {
            loadFragment(brandDetailInforFragment,bundleInfor);
            dotInformation.setVisibility(View.VISIBLE);
            dotPerfumes.setVisibility(View.INVISIBLE);
            textInforTab.setTextColor(Color.parseColor("#000000"));
            // Đặt font Montserrat bold từ resource
            Typeface montserratBold = ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold);
            textInforTab.setTypeface(montserratBold);
            textPerfumesTab.setTextColor(Color.parseColor("#76777D"));
            // Đặt font Montserrat bold từ resource
            Typeface montserrat_light = ResourcesCompat.getFont(requireContext(), R.font.montserrat_light);
            textPerfumesTab.setTypeface(montserrat_light);
        });
        textPerfumesTab.setOnClickListener(v -> {
                    loadFragment(brandDetailPerfumeFragment,bundlePerfume);
                    dotPerfumes.setVisibility(View.VISIBLE);
                    dotInformation.setVisibility(View.INVISIBLE);
                    textPerfumesTab.setTextColor(Color.parseColor("#000000"));
                    // Đặt font Montserrat bold từ resource
                    Typeface montserratBold = ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold);
                    textPerfumesTab.setTypeface(montserratBold);
                    textInforTab.setTextColor(Color.parseColor("#76777D"));
                    // Đặt font Montserrat bold từ resource
                    Typeface montserrat_light = ResourcesCompat.getFont(requireContext(), R.font.montserrat_light);
                    textInforTab.setTypeface(montserrat_light);
                }
        );
        BrandEntity brand = (BrandEntity) getArguments().getSerializable("brand");
        if (brand != null) {
            String name = brand.getName();
            String banner = brand.getBanner();
            String logo = brand.getLogo();
            String founded = brand.getFounded();
            String founder = brand.getFounder();
            String country = brand.getCountry();
            String notablePerfumes =  brand.getNotablePerfumes();
            String segment =  brand.getSegment();
            String popularity =  brand.getPopularity();
            String style =brand.getStyle();
            String link = brand.getLink();
            String description = brand.getDescription();

            bundlePerfume.putString("name", name);
            loadFragment(brandDetailPerfumeFragment,bundlePerfume);

            bundleInfor.putString("founded", founded);
            bundleInfor.putString("founder", founder);
            bundleInfor.putString("country", country);
            bundleInfor.putString("notablePerfumes", notablePerfumes);
            bundleInfor.putString("segment", segment);
            bundleInfor.putString("popularity", popularity);
            bundleInfor.putString("style",style);
            bundleInfor.putString("link", link);
            bundleInfor.putString("description", description);

            Glide.with(this).load(logo).into(brandLogo);
            Glide.with(this).load(banner).into(brandBanner);
            brandName.setText(name);

        }
        gotoWebsite.setOnClickListener(v -> {
            Bundle argsBundle = getArguments();
            if (argsBundle != null) {
                String link = argsBundle.getString("link");
                if (link != null && !link.isEmpty()) {
                    if (!link.startsWith("http://") && !link.startsWith("https://")) {
                        link = "http://" + link; // đề phòng link thiếu http
                    }
                    android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
                    intent.setData(android.net.Uri.parse(link));
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private void loadFragment(Fragment fragment,Bundle bundle) {
        fragment.setArguments(bundle);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.brandDetailContainer, fragment)
                .addToBackStack(null)
                .commit();

    }
}
