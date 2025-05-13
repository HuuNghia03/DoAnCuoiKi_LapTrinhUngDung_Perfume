package com.example.perfume;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

public class first_activity extends AppCompatActivity {
    Button btregister,btlogin;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_layout);
        View indicator1 = findViewById(R.id.indicator1);
        View indicator2 = findViewById(R.id.indicator2);
        btregister=findViewById(R.id.button_register);
        btlogin=findViewById(R.id.button_login);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        List<Integer> layouts = Arrays.asList(R.layout.home_page1, R.layout.home_page2);
        com.example.perfume.PageAdapter adapter = new com.example.perfume.PageAdapter(layouts);
        viewPager.setAdapter(adapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 0) {
                    indicator1.setBackgroundResource(R.drawable.indicator_active);
                    indicator2.setBackgroundResource(R.drawable.indicator_inactive);
                } else if (position == 1) {
                    indicator1.setBackgroundResource(R.drawable.indicator_inactive);
                    indicator2.setBackgroundResource(R.drawable.indicator_active);
                }
            }
        });
        btregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_register= new Intent(first_activity.this,register_activity.class);
                startActivity(intent_register);
            }
        });
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(first_activity.this,login_activity.class);
                startActivity(intent_login);
            }
        });






    }
}
