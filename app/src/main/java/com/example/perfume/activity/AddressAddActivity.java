package com.example.perfume.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.perfume.AddressStorage;
import com.example.perfume.Navigator;
import com.example.perfume.R;
import com.example.perfume.entity.AddressEntity;

import java.util.List;

public class AddressAddActivity extends AppCompatActivity {
    private EditText addressName, firstName, lastName, City, District, Ward, detailAddress, Phone;
    private Button btnSave;
    private ImageView btnBack;
    private SwitchCompat switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_add);
        addressName = findViewById(R.id.addressName);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        City = findViewById(R.id.city);
        District = findViewById(R.id.district);
        Ward = findViewById(R.id.ward);
        detailAddress = findViewById(R.id.detail);
        Phone = findViewById(R.id.phone);
        switch1 = findViewById(R.id.switch1);
        btnSave = findViewById(R.id.save_button);
        btnBack=findViewById(R.id.back_icon);
        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.pop_zoom_in,0);
        });
        btnSave.setOnClickListener(v -> {
            String addressname = addressName.getText().toString();
            String firstname = firstName.getText().toString();
            String lastname = lastName.getText().toString();
            String city = City.getText().toString();
            String district = District.getText().toString();
            String ward = Ward.getText().toString();
            String detailaddress = detailAddress.getText().toString();
            String phone = Phone.getText().toString();
            boolean isDefault=switch1.isChecked();

            String key = "addresses_" + Navigator.getUserId(this); // hoặc user.getId()
            List<AddressEntity> list = AddressStorage.getAddresses(this, key);
            if (isDefault) {
                for (AddressEntity address : list) {
                    address.setDefault(false);
                }
            }

            AddressEntity newAddress = new AddressEntity(addressname, firstname, lastname, phone, city, district, ward, detailaddress,isDefault);
            if (isDefault) {
                list.add(0, newAddress); // thêm vào vị trí 0
            } else {
                list.add(newAddress);    // thêm vào cuối danh sách
            }
            AddressStorage.saveAddresses(this, key, list);
            setResult(RESULT_OK); // Báo kết quả thành công
            finish();
            overridePendingTransition(R.anim.pop_zoom_in,0);
        });

    }
}
