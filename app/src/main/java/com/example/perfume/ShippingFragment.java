package com.example.perfume;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.perfume.entities.AddressEntity;

import java.util.List;

public class ShippingFragment extends Fragment {
    private LinearLayout standardDelivery, expressDelivery;
    private TextView selectAddress, nameUser, phoneUser, detailAddress, city;
    private List<AddressEntity> addressList;
    private String selectedDelivery = "express";
    private AddressEntity selectedAddressResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);
        viewBind(view);
        if (getArguments() != null) {
            addressList = (List<AddressEntity>) getArguments().getSerializable("address_list");

            if (addressList != null && !addressList.isEmpty()) {
                // Ví dụ: hiển thị địa chỉ đầu tiên
                selectedAddressResult = addressList.get(0);
                String Name = selectedAddressResult.getFirstName() + " " + selectedAddressResult.getLastName();
                String City = selectedAddressResult.getWard() + ", " + selectedAddressResult.getDistrict() + ", " + selectedAddressResult.getCity();

                Toast.makeText(requireContext(), "addres:" + selectedAddressResult.getDetailAddress(), Toast.LENGTH_SHORT).show();
                detailAddress.setText(selectedAddressResult.getDetailAddress());
                city.setText(City);
                nameUser.setText(Name);
                phoneUser.setText((selectedAddressResult.getPhone()));
            }
        }
        selectAddress.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddressConfirmActivity.class);
            startActivityForResult(intent, 100); // requestCode = 100
        });
        standardDelivery.setOnClickListener(v -> {
            selectedDelivery = "standard";
            standardDelivery.setBackgroundResource(R.drawable.bg_delivery_selected);
            expressDelivery.setBackgroundResource(R.drawable.bg_delivery_unselected);

        });

        expressDelivery.setOnClickListener(v -> {
            selectedDelivery = "express";
            expressDelivery.setBackgroundResource(R.drawable.bg_delivery_selected);
            standardDelivery.setBackgroundResource(R.drawable.bg_delivery_unselected);

        });

        return view;
    }

    public AddressEntity getSelectedAddress() {
        return selectedAddressResult;
    }

    public String getDeliveryMethod() {
        return selectedDelivery;
    }

    private void viewBind(View view) {
        standardDelivery = view.findViewById(R.id.standardDelivery);
        expressDelivery = view.findViewById(R.id.expressDelivery);
        selectAddress = view.findViewById(R.id.selectAddress);
        nameUser = view.findViewById(R.id.nameUser);
        phoneUser = view.findViewById(R.id.phoneUser);
        detailAddress = view.findViewById(R.id.detailAddress);
        city = view.findViewById(R.id.city);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == getActivity().RESULT_OK) {
            AddressEntity selectedAddress = (AddressEntity) data.getSerializableExtra("selected_address");
            if (selectedAddress != null) {
                selectedAddressResult = selectedAddress;
                String Name = selectedAddress.getFirstName() + " " + selectedAddress.getLastName();
                String City = selectedAddress.getWard() + ", " + selectedAddress.getDistrict() + ", " + selectedAddress.getCity();

                detailAddress.setText(selectedAddress.getDetailAddress());
                city.setText(City);
                nameUser.setText(Name);
                phoneUser.setText(selectedAddress.getPhone());

                Toast.makeText(requireContext(), "Chọn: " + selectedAddress.getDetailAddress(), Toast.LENGTH_SHORT).show();
            }
        }
    }



}
