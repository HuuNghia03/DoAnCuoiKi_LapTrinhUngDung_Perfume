package com.example.perfume.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.res.ResourcesCompat;

import com.example.perfume.R;
import com.example.perfume.UserEntity;
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
    public WelcomeOlfactiveView(Context context,UserEntity userEntity, Runnable onNext) {
        super(context);
        this.userEntity=userEntity;
        init(context, onNext);


    }

   private void init(Context context, Runnable onNext) {
        LayoutInflater.from(context).inflate(R.layout.welcome_offactive, this, true);
        String olfactive=userEntity.getCategoryList();
        olfaciveChar = findViewById(R.id.olfactiveChart);
        btnConfirm = findViewById(R.id.confirm_button);
       List<String> allCategories = new ArrayList<>();
       allCategories.add("Aquatic");
       allCategories.add("Woody");
       allCategories.add("Powdery");
       allCategories.add("Citrus");
       allCategories.add("Fruity");
       allCategories.add("Herbal");
       allCategories.add("Floral");

       // Chuyển chuỗi olfactive thành danh sách category đã chọn
       List<String> selectedCategories = Arrays.asList(olfactive.split(", "));  // Tách chuỗi theo dấu phẩy

// Tạo dữ liệu cho biểu đồ (giá trị từ 0 đến 2)
       List<RadarEntry> entries = new ArrayList<>();

// Kiểm tra mỗi category và gán giá trị 2 nếu có trong olfactive, nếu không có thì gán 1
       for (String category : allCategories) {
           if (selectedCategories.contains(category)) {
               entries.add(new RadarEntry(2f));  // Nếu category có trong olfactive, gán giá trị 2
           } else {
               entries.add(new RadarEntry(1f));  // Nếu category không có trong olfactive, gán giá trị 1
           }
       }


// Tạo và cấu hình data set
        RadarDataSet dataSet = new RadarDataSet(entries, "Olfactive Profile");
        dataSet.setColor(Color.BLACK);               // Màu viền
        dataSet.setFillColor(Color.BLACK);           // Màu vùng tô
        dataSet.setDrawFilled(true);                // Cho phép tô màu
        dataSet.setLineWidth(2f);                   // Độ dày nét vẽ
        dataSet.setValueTextSize(12f);              // Size chữ

// Đưa data vào biểu đồ
        RadarData radarData = new RadarData(dataSet);
        olfaciveChar.setData(radarData);

// Gán nhãn cho trục X
        String[] labels = {"AQUATIC", "WOODY", "POWDERY", "CITRUS", "FRUITY", "HERBAL", "FLORAL"};
        XAxis xAxis = olfaciveChar.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return labels[(int) value % labels.length];
            }
        });

// Cấu hình trục Y để ẩn mức số
        YAxis yAxis = olfaciveChar.getYAxis();
        yAxis.setAxisMinimum(0f);        // Min = 0
        yAxis.setAxisMaximum(2f);        // Max = 2
        yAxis.setLabelCount(3, true);    // Chia 3 mức: 0, 1, 2 (ẩn)
        Typeface optimaFont = ResourcesCompat.getFont(getContext(), R.font.optima_medium);

        xAxis.setTypeface(optimaFont);
        xAxis.setTextSize(14f);
        xAxis.setTextColor(Color.BLACK);
        olfaciveChar.setRotationEnabled(false);

        olfaciveChar.getYAxis().setTypeface(optimaFont);

        dataSet.setValueTypeface(optimaFont);

        // Giao diện và hiệu ứng
        olfaciveChar.setBackgroundColor(Color.TRANSPARENT);   // Nền trắng
        olfaciveChar.getDescription().setEnabled(false);  // Tắt mô tả
        olfaciveChar.getLegend().setEnabled(false);       // Tắt chú thích
        olfaciveChar.setWebLineWidth(1f);                 // Độ dày lưới
        olfaciveChar.setWebColor(Color.BLACK);            // Màu lưới ngoài
        olfaciveChar.setWebColorInner(Color.BLACK);        // Màu lưới trong
        olfaciveChar.setWebAlpha(100);                    // Độ trong suốt
        olfaciveChar.animateXY(1000, 1000);               // Hiệu ứng
        dataSet.setDrawValues(false);         // Ẩn giá trị trên các điểm
        yAxis.setDrawLabels(false);          // Ẩn số trên trục Y
        olfaciveChar.getLegend().setEnabled(false);       // Tắt chú thích
        olfaciveChar.getDescription().setEnabled(false);  // Tắt mô tả


        btnConfirm.setOnClickListener(v -> {
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
            postDelayed(onNext::run, 200);
        });
    }
}
