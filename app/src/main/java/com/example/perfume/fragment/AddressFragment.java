package com.example.perfume.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.perfume.AddressStorage;
import com.example.perfume.Navigator;
import com.example.perfume.activity.AddressAddActivity;
import com.example.perfume.entity.AddressEntity;
import com.example.perfume.relation.OrderItemWithPerfume;
import com.example.perfume.OrderViewModel;
import com.example.perfume.relation.OrderWithItems;
import com.example.perfume.R;
import com.example.perfume.activity.HomeActivity;

import java.util.List;

public class AddressFragment extends Fragment {

    private TextView addAddress, cart_title;
    private LinearLayout addressItemsContainer;
    private Button btnConfirm;
    private ImageView btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.address_confirm, container, false);

        addAddress = view.findViewById(R.id.addAddress);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnBack = view.findViewById(R.id.back_icon);
        cart_title = view.findViewById(R.id.cart_title);
        addressItemsContainer = view.findViewById(R.id.address_items_container);

        cart_title.setText("Your Address");
        btnConfirm.setVisibility(View.GONE);
        String key = "addresses_" + Navigator.getUserId(requireContext());
        List<AddressEntity> addressList = AddressStorage.getAddresses(requireContext(), key);
        displayAddress(addressList);

        btnBack.setOnClickListener(v -> {
            ((HomeActivity) requireActivity()).setDetailFragment(
                    getParentFragmentManager().findFragmentByTag("4"));
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        // Bắt sự kiện thêm địa chỉ
        addAddress.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddressAddActivity.class);
            startActivityForResult(intent, 100);
            requireActivity().overridePendingTransition(R.anim.zoom_in, R.anim.fade_out); // ✅ Sửa ở đây
        });



        return view;
    }

    private void displayAddress(List<AddressEntity> addresses) {
        addressItemsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (AddressEntity address : addresses) {
            View view = inflater.inflate(R.layout.item_address, addressItemsContainer, false);

            // Ánh xạ các TextView trong item
            ((TextView) view.findViewById(R.id.nameAddress)).setText(address.getNameAddress().toUpperCase());
            ((TextView) view.findViewById(R.id.firstName)).setText(address.getFirstName());
            ((TextView) view.findViewById(R.id.lastName)).setText(address.getLastName());
            ((TextView) view.findViewById(R.id.detail)).setText(address.getDetailAddress());
            ((TextView) view.findViewById(R.id.ward)).setText(address.getWard() + ", ");
            ((TextView) view.findViewById(R.id.district)).setText(address.getDistrict() + ", ");
            ((TextView) view.findViewById(R.id.city)).setText(address.getCity());
            ((TextView) view.findViewById(R.id.phone)).setText(address.getPhone());
            ((TextView) view.findViewById(R.id.isdefault)).setVisibility((address.getIsDefault() ? View.VISIBLE : View.INVISIBLE));
            RadioButton radioButton = view.findViewById(R.id.radioSelect);
            radioButton.setVisibility(View.INVISIBLE);

            addressItemsContainer.addView(view);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Load lại danh sách địa chỉ sau khi thêm mới
            String key = "addresses_" + Navigator.getUserId(requireContext());
            List<AddressEntity> updatedList = AddressStorage.getAddresses(requireContext(), key);
            displayAddress(updatedList);
        }
    }
}
