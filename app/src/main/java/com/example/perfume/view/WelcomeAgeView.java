package com.example.perfume.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.perfume.R;
import com.example.perfume.entity.UserEntity;

public class WelcomeAgeView extends LinearLayout {
    private UserEntity userEntity;
    private EditText Age;
    private Button btnConfirm;
    public WelcomeAgeView(Context context, UserEntity userEntity, Runnable onNext) {
        super(context);
        this.userEntity = userEntity;
        init(context, onNext);
    }

    private void init(Context context, Runnable onNext) {
        LayoutInflater.from(context).inflate(R.layout.welcome_age, this, true);
        Age = findViewById(R.id.age);
        btnConfirm = findViewById(R.id.confirm_button);

        btnConfirm.setOnClickListener(v -> {
            String ageText = Age.getText().toString();

            // Kiểm tra nếu EditText rỗng
            if (ageText.isEmpty()) {
                // Hiển thị thông báo yêu cầu người dùng nhập thông tin
                Toast.makeText(context, "Please enter your age.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // Lưu giá trị vào userEntity nếu nhập hợp lệ
                    userEntity.setAge(Integer.parseInt(ageText));
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
                    postDelayed(onNext::run, 200);
                } catch (NumberFormatException e) {
                    // Nếu giá trị không phải là số, yêu cầu người dùng nhập lại
                    Toast.makeText(context, "Invalid age. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
