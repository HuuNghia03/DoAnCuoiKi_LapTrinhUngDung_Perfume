package com.example.perfume.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.perfume.R;

public class BrandDetailInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.brand_detail_infor, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        // Ánh xạ và set dữ liệu từ brand object ở đây
        TextView tvCountry = view.findViewById(R.id.tvCountry);
        TextView tvFounded = view.findViewById(R.id.tvFounded);
        TextView tvFounder = view.findViewById(R.id.tvFounder);
        TextView tvDescription = view.findViewById(R.id.tvDescription);
        TextView tvNotable = view.findViewById(R.id.tvNotable);
        TextView tvSegment = view.findViewById(R.id.tvSegment);
        TextView tvPopularity = view.findViewById(R.id.tvPopularity);
        TextView tvStyle = view.findViewById(R.id.tvStyle);
        TextView tvWebsite = view.findViewById(R.id.tvWebsite);
        tvFounded.setText("Founded: "+args.getString("founded"));
        tvCountry.setText("Country: "+args.getString("country"));
        tvFounder.setText("Founder: "+args.getString("founder"));
        tvDescription.setText(args.getString("description"));
        tvNotable.setText("Notable: "+args.getString("notablePerfumes"));
        tvSegment.setText("Segment: "+args.getString("segment"));
        tvPopularity.setText("Popularity: "+args.getString("popularity"));
        tvStyle.setText("Style: "+args.getString("style"));
        tvWebsite.setText("Website: "+args.getString("link"));


    }
}
