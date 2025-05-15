package com.example.perfume;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class PaymentFragment extends Fragment {
    private LinearLayout visaOption, bankOption, visaForm, bankForm;
    private String paymentMethod = "visa";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        viewBind(view);
        setUpClickListener(view);
        return view;
    }

    private void viewBind(View view) {
        visaOption = view.findViewById(R.id.visaOption);
        bankOption = view.findViewById(R.id.bankOption);
        visaForm = view.findViewById(R.id.visaForm);
        bankForm = view.findViewById(R.id.bankForm);
    }


    private void setUpClickListener(View view) {
        visaOption.setOnClickListener(v -> {
            paymentMethod = "visa";
            visaOption.setBackgroundResource(R.drawable.bg_delivery_selected);
            bankOption.setBackgroundResource(R.drawable.bg_delivery_unselected);
            visaForm.setVisibility(View.VISIBLE);
            bankForm.setVisibility(View.GONE);

        });

        bankOption.setOnClickListener(v -> {
            paymentMethod = "bank";

            bankOption.setBackgroundResource(R.drawable.bg_delivery_selected);
            visaOption.setBackgroundResource(R.drawable.bg_delivery_unselected);
            visaForm.setVisibility(View.GONE);
            bankForm.setVisibility(View.VISIBLE);


        });
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }


}
