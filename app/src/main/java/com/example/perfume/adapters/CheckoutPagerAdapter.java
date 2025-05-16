package com.example.perfume.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.perfume.entities.AddressEntity;
import com.example.perfume.fragments.PaymentFragment;
import com.example.perfume.fragments.ReviewFragment;
import com.example.perfume.fragments.ShippingFragment;

import java.util.ArrayList;
import java.util.List;

public class CheckoutPagerAdapter extends FragmentStateAdapter {
    private final List<AddressEntity> addressList;

    private ShippingFragment shippingFragment;
    private PaymentFragment paymentFragment;
    private ReviewFragment reviewFragment;

    private AddressEntity selectedAddress;
    private String deliveryMethod;
    private String paymentMethod;

    public CheckoutPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<AddressEntity> addressList) {
        super(fragmentActivity);
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                if (shippingFragment == null) {
                    shippingFragment = new ShippingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("address_list", new ArrayList<>(addressList));
                    shippingFragment.setArguments(bundle);
                }
                return shippingFragment;

            case 1:
                if (paymentFragment == null) {
                    paymentFragment = new PaymentFragment();
                }
                return paymentFragment;

            case 2:
                if (reviewFragment == null) {
                    reviewFragment = new ReviewFragment();
                }
                return reviewFragment;

            default:
                return new ShippingFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }


    public ReviewFragment getReviewFragment() {

        return reviewFragment;
    }

    public AddressEntity getSelectedAddress() {
        selectedAddress=shippingFragment.getSelectedAddress();

        return selectedAddress;
    }

    public String getDeliveryMethod() {
        deliveryMethod=shippingFragment.getDeliveryMethod();
        return deliveryMethod;
    }

    public String getPaymentMethod() {
        paymentMethod=paymentFragment.getPaymentMethod();
        return paymentMethod;
    }
}

