package com.example.perfume.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.res.ResourcesCompat;

import com.example.perfume.R;
import com.example.perfume.entity.UserEntity;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WelcomeOlfactiveView extends LinearLayout {
    private Button btnConfirm;
    private RadarChart olfaciveChar;
    private UserEntity userEntity;

    public WelcomeOlfactiveView(Context context, UserEntity userEntity, Runnable onNext) {
        super(context);
        this.userEntity = userEntity;
        init(context, onNext);
    }

    private void init(Context context, Runnable onNext) {
        LayoutInflater.from(context).inflate(R.layout.welcome_offactive, this, true);
        olfaciveChar = findViewById(R.id.olfactiveChart);
        btnConfirm = findViewById(R.id.confirm_button);

        String olfactive = userEntity.getCategoryList();
        List<String> Notes = Arrays.asList(olfactive.split(", "));

        // Gọi hàm riêng để vẽ biểu đồ
        setupRadarChart(context, Notes,olfaciveChar, Color.parseColor("#000000"));

        btnConfirm.setOnClickListener(v -> {
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
            postDelayed(onNext::run, 200);
        });
    }

    // Hàm vẽ radar chart
    public void setupRadarChart(Context context, List<String> selectedNotes, RadarChart olfactiveChart, int color) {
        List<String> notes = Arrays.asList("Aquatic", "Woody", "Powdery", "Citrus", "Fruity", "Herbal", "Floral");

        List<RadarEntry> entries = new ArrayList<>();
        for (String note : notes) {
            entries.add(new RadarEntry(selectedNotes.contains(note) ? 2f : 1f));
        }

        RadarDataSet dataSet = new RadarDataSet(entries, "Olfactive Profile");
        dataSet.setColor(color);
        dataSet.setFillColor(color);
        dataSet.setDrawFilled(true);
        dataSet.setLineWidth(2f);
        dataSet.setValueTextSize(12f);
        dataSet.setDrawValues(false);

        RadarData radarData = new RadarData(dataSet);
        olfactiveChart.setData(radarData);

        String[] labels = {"AQUATIC", "WOODY", "POWDERY", "CITRUS", "FRUITY", "HERBAL", "FLORAL"};
        XAxis xAxis = olfactiveChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return labels[(int) value % labels.length];
            }
        });

        xAxis.setTextSize(14f);
        xAxis.setTextColor(color);

        YAxis yAxis = olfactiveChart.getYAxis();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(2f);
        yAxis.setLabelCount(3, true);
        yAxis.setDrawLabels(false);

        Typeface optimaFont = ResourcesCompat.getFont(context, R.font.optima_medium);
        xAxis.setTypeface(optimaFont);
        yAxis.setTypeface(optimaFont);
        dataSet.setValueTypeface(optimaFont);

        olfactiveChart.setRotationEnabled(false);
        olfactiveChart.setBackgroundColor(Color.TRANSPARENT);
        olfactiveChart.getDescription().setEnabled(false);
        olfactiveChart.getLegend().setEnabled(false);
        olfactiveChart.setWebLineWidth(1f);
        olfactiveChart.setWebColor(color);
        olfactiveChart.setWebColorInner(color);
        olfactiveChart.setWebAlpha(100);
        olfactiveChart.animateXY(1000, 1000);
        olfactiveChart.invalidate();
    }

}

