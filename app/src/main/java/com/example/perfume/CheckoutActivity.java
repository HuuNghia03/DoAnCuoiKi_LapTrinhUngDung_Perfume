package com.example.perfume;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.perfume.R;
import com.example.perfume.entities.AddressEntity;

import java.util.List;


public class CheckoutActivity extends AppCompatActivity {
    private ViewPager2 checkoutViewPager;
    private Button btnNext;
    private CheckoutPagerAdapter checkoutPagerAdapterInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        TextView textShipping = findViewById(R.id.textShipping);
        TextView textPayment = findViewById(R.id.textPayment);
        TextView textReview = findViewById(R.id.textReview);

        ImageView iconShipping = findViewById(R.id.iconShipping);
        ImageView iconPayment = findViewById(R.id.iconPayment);
        ImageView iconReview = findViewById(R.id.iconReview);

        View line1 = findViewById(R.id.line_1);
        View line2 = findViewById(R.id.line_2);

        String key = "addresses_" + Navigator.getUserId(this); // hoặc user.getId()
        List<AddressEntity> list = AddressStorage.getAddresses(this, key);
        checkoutViewPager = findViewById(R.id.checkoutViewPager);
        btnNext = findViewById(R.id.btnNext);
        checkoutPagerAdapterInstance = new CheckoutPagerAdapter(this, list);
        checkoutViewPager.setAdapter(checkoutPagerAdapterInstance);
        btnNext.setOnClickListener(v -> {
            int currentItem = checkoutViewPager.getCurrentItem();
            if (currentItem < checkoutPagerAdapterInstance.getItemCount() - 1) {
                checkoutViewPager.setCurrentItem(currentItem + 1);
            } else {
                // Nếu đang ở fragment thứ 3 (ReviewFragment), thực hiện chức năng đặt hàng
                // performOrder();
            }
        });

        checkoutViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Reset màu
                textShipping.setTextColor(Color.parseColor("#888888"));
                textPayment.setTextColor(Color.parseColor("#888888"));
                textReview.setTextColor(Color.parseColor("#888888"));

                iconShipping.setImageResource(R.drawable.shipping);
                iconPayment.setImageResource(R.drawable.payment);
                iconReview.setImageResource(R.drawable.review);

                line1.setBackgroundColor(Color.parseColor("#f5f5f5"));
                line2.setBackgroundColor(Color.parseColor("#f5f5f5"));

                // Tô đậm step hiện tại
                switch (position) {
                    case 0:
                        textShipping.setTextColor(Color.parseColor("#000000"));
                        iconShipping.setImageResource(R.drawable.shipping_bold);

                        break;
                    case 1:
                        textShipping.setTextColor(Color.parseColor("#000000"));
                        iconShipping.setImageResource(R.drawable.shipping_bold);
                        textPayment.setTextColor(Color.parseColor("#000000"));
                        line1.setBackgroundColor(Color.parseColor("#000000"));
                        iconPayment.setImageResource(R.drawable.payment_bold);

                        break;
                    case 2:
                        ReviewFragment reviewFragment = checkoutPagerAdapterInstance.getReviewFragment();
                        if (reviewFragment != null) {
                            reviewFragment.updateData(
                                    checkoutPagerAdapterInstance.getSelectedAddress(),
                                    checkoutPagerAdapterInstance.getDeliveryMethod(),
                                    checkoutPagerAdapterInstance.getPaymentMethod());
                        }
                        ;
                        btnNext.setVisibility(View.GONE);
                        textReview.setTextColor(Color.parseColor("#000000"));
                        line2.setBackgroundColor(Color.parseColor("#000000"));
                        iconReview.setImageResource(R.drawable.review_bold);
                        textShipping.setTextColor(Color.parseColor("#000000"));
                        iconShipping.setImageResource(R.drawable.shipping_bold);
                        textPayment.setTextColor(Color.parseColor("#000000"));
                        line1.setBackgroundColor(Color.parseColor("#000000"));
                        iconPayment.setImageResource(R.drawable.payment_bold);


                        break;
                }
            }
        });

    }


}
