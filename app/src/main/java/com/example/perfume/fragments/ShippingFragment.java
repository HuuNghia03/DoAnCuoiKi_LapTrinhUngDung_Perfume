package com.example.perfume.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.perfume.R;
import com.example.perfume.activities.AddressConfirmActivity;
import com.example.perfume.activities.LoginActivity;
import com.example.perfume.activities.WelcomeActivity;
import com.example.perfume.entities.AddressEntity;
import com.example.perfume.entities.UserEntity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.concurrent.Executors;

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

        // Lấy danh sách địa chỉ từ arguments, hiển thị địa chỉ đầu tiên nếu có
        if (getArguments() != null) {
            addressList = (List<AddressEntity>) getArguments().getSerializable("address_list");
            if (addressList != null && !addressList.isEmpty()) {
                selectedAddressResult = addressList.get(0);
                String name = selectedAddressResult.getFirstName() + " " + selectedAddressResult.getLastName();
                String cityText = selectedAddressResult.getWard() + ", " +
                        selectedAddressResult.getDistrict() + ", " +
                        selectedAddressResult.getCity();

                Toast.makeText(requireContext(), "Address: " + selectedAddressResult.getDetailAddress(), Toast.LENGTH_SHORT).show();
                detailAddress.setText(selectedAddressResult.getDetailAddress());
                city.setText(cityText);
                nameUser.setText(name);
                phoneUser.setText(selectedAddressResult.getPhone());
            } else {
                showAddressDialog();
            }
        }

        // Bắt sự kiện chọn địa chỉ
        selectAddress.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddressConfirmActivity.class);
            startActivityForResult(intent, 100); // requestCode = 100
        });

        // Chọn phương thức giao hàng chuẩn
        standardDelivery.setOnClickListener(v -> {
            selectedDelivery = "standard";
            standardDelivery.setBackgroundResource(R.drawable.bg_delivery_selected);
            expressDelivery.setBackgroundResource(R.drawable.bg_delivery_unselected);
        });

        // Chọn phương thức giao hàng nhanh
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
                String name = selectedAddress.getFirstName() + " " + selectedAddress.getLastName();
                String cityText = selectedAddress.getWard() + ", " +
                        selectedAddress.getDistrict() + ", " +
                        selectedAddress.getCity();

                detailAddress.setText(selectedAddress.getDetailAddress());
                city.setText(cityText);
                nameUser.setText(name);
                phoneUser.setText(selectedAddress.getPhone());

            }
        }
    }
    private void showAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_profile, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Ánh xạ view
        TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
        TextView txtMessage = dialogView.findViewById(R.id.txtMessage);
        Button btnContinue = dialogView.findViewById(R.id.btnContinue);
        ImageView btnClose = dialogView.findViewById(R.id.btnClose);
        txtTitle.setText("Add Address");
        txtMessage.setText("You currently do not have a shipping address. Please add your shipping address to proceed with your purchase.");

        // Xử lý nút
        btnContinue.setOnClickListener(v -> {


            Intent intent = new Intent(requireContext(), AddressConfirmActivity.class);
            startActivityForResult(intent, 100);
            dialog.dismiss();
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
