package com.example.perfume.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.perfume.AddressStorage;
import com.example.perfume.Navigator;
import com.example.perfume.R;
import com.example.perfume.adapters.CheckoutPagerAdapter;
import com.example.perfume.entities.AddressEntity;
import com.example.perfume.fragments.ReviewFragment;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private ViewPager2 checkoutViewPager;
    private Button btnNext;
    private CheckoutPagerAdapter checkoutPagerAdapterInstance;

    private TextView textShipping, textPayment, textReview;
    private ImageView iconShipping, iconPayment, iconReview;
    private View line1, line2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();
        setupViewPager();
        setupNextButton();
        handlePageChanges();
    }

    private void initViews() {
        textShipping = findViewById(R.id.textShipping);
        textPayment = findViewById(R.id.textPayment);
        textReview = findViewById(R.id.textReview);

        iconShipping = findViewById(R.id.iconShipping);
        iconPayment = findViewById(R.id.iconPayment);
        iconReview = findViewById(R.id.iconReview);

        line1 = findViewById(R.id.line_1);
        line2 = findViewById(R.id.line_2);

        checkoutViewPager = findViewById(R.id.checkoutViewPager);
        btnNext = findViewById(R.id.btnNext);
    }

    private void setupViewPager() {
        String key = "addresses_" + Navigator.getUserId(this);
        List<AddressEntity> addressList = AddressStorage.getAddresses(this, key);

        checkoutPagerAdapterInstance = new CheckoutPagerAdapter(this, addressList);
        checkoutViewPager.setAdapter(checkoutPagerAdapterInstance);
    }

    private void setupNextButton() {
        btnNext.setOnClickListener(v -> {
            int currentItem = checkoutViewPager.getCurrentItem();
            if (currentItem < checkoutPagerAdapterInstance.getItemCount() - 1) {
                checkoutViewPager.setCurrentItem(currentItem + 1);
            } else {
                // Nếu đang ở bước cuối (Review), thực hiện đặt hàng
                // performOrder();
            }
        });
    }

    private void handlePageChanges() {
        checkoutViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                resetStepStyles();
                highlightCurrentStep(position);
            }
        });
    }

    private void resetStepStyles() {
        textShipping.setTextColor(Color.parseColor("#888888"));
        textPayment.setTextColor(Color.parseColor("#888888"));
        textReview.setTextColor(Color.parseColor("#888888"));

        iconShipping.setImageResource(R.drawable.shipping);
        iconPayment.setImageResource(R.drawable.payment);
        iconReview.setImageResource(R.drawable.review);

        line1.setBackgroundColor(Color.parseColor("#f5f5f5"));
        line2.setBackgroundColor(Color.parseColor("#f5f5f5"));

        btnNext.setVisibility(View.VISIBLE);
    }

    private void highlightCurrentStep(int position) {
        switch (position) {
            case 0:
                textShipping.setTextColor(Color.BLACK);
                iconShipping.setImageResource(R.drawable.shipping_bold);
                break;

            case 1:
                textShipping.setTextColor(Color.BLACK);
                iconShipping.setImageResource(R.drawable.shipping_bold);

                textPayment.setTextColor(Color.BLACK);
                iconPayment.setImageResource(R.drawable.payment_bold);
                line1.setBackgroundColor(Color.BLACK);
                break;

            case 2:
                ReviewFragment reviewFragment = checkoutPagerAdapterInstance.getReviewFragment();
                if (reviewFragment != null) {
                    reviewFragment.updateData(
                            checkoutPagerAdapterInstance.getSelectedAddress(),
                            checkoutPagerAdapterInstance.getDeliveryMethod(),
                            checkoutPagerAdapterInstance.getPaymentMethod()
                    );
                }

                btnNext.setVisibility(View.GONE);

                textShipping.setTextColor(Color.BLACK);
                iconShipping.setImageResource(R.drawable.shipping_bold);

                textPayment.setTextColor(Color.BLACK);
                iconPayment.setImageResource(R.drawable.payment_bold);
                line1.setBackgroundColor(Color.BLACK);

                textReview.setTextColor(Color.BLACK);
                iconReview.setImageResource(R.drawable.review_bold);
                line2.setBackgroundColor(Color.BLACK);
                break;
        }
    }
}
