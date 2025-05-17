package com.example.perfume.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.perfume.AddressStorage;
import com.example.perfume.Navigator;
import com.example.perfume.R;
import com.example.perfume.entity.AddressEntity;

import java.util.List;

public class AddressConfirmActivity extends AppCompatActivity {

    private TextView addAddress;
    private LinearLayout addressItemsContainer;
    private RadioButton selectedRadioButton = null;
    private AddressEntity selectedAddress = null;
    private Button btnConfirm;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_confirm);

        // Ánh xạ view
        addAddress = findViewById(R.id.addAddress);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.back_icon);
        addressItemsContainer = findViewById(R.id.address_items_container);

        // Bắt sự kiện thêm địa chỉ
        addAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddressAddActivity.class);
            startActivityForResult(intent, 100);
            overridePendingTransition(R.anim.zoom_in, R.anim.fade_out);
        });

        // Bắt sự kiện quay lại
        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.pop_zoom_in, 0);
        });

        // Hiển thị danh sách địa chỉ
        String key = "addresses_" + Navigator.getUserId(this);
        List<AddressEntity> addressList = AddressStorage.getAddresses(this, key);
        displayAddress(addressList);

        // Bắt sự kiện xác nhận
        btnConfirm.setOnClickListener(v -> {
            if (selectedAddress == null) {
                Toast.makeText(this, "Please select an address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Trả địa chỉ đã chọn về cho Activity trước đó
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_address", selectedAddress);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Load lại danh sách địa chỉ sau khi thêm mới
            String key = "addresses_" + Navigator.getUserId(this);
            List<AddressEntity> updatedList = AddressStorage.getAddresses(this, key);
            displayAddress(updatedList);
        }
    }

    private void displayAddress(List<AddressEntity> addresses) {
        addressItemsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

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

            // Xử lý chọn RadioButton
            RadioButton radioButton = view.findViewById(R.id.radioSelect);
            radioButton.setOnClickListener(v -> {
                if (selectedRadioButton != null) {
                    selectedRadioButton.setChecked(false);
                }
                radioButton.setChecked(true);
                selectedRadioButton = radioButton;
                selectedAddress = address;
            });

            addressItemsContainer.addView(view);
        }
    }
}
