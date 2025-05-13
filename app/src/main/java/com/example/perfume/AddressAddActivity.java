package com.example.perfume;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.perfume.entities.AddressEntity;

import java.util.List;

public class AddressAddActivity extends AppCompatActivity {
    private EditText addressName, firstName, lastName, City, District, Ward, detailAddress, Phone;
    private Button btnSave;
    private ImageView btnBack;

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
        btnSave = findViewById(R.id.save_button);
        btnBack=findViewById(R.id.back_icon);
        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.fade_in_acti,R.anim.fade_out);
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

            String key = "addresses_" + Navigator.getUserId(this); // hoặc user.getId()
            List<AddressEntity> list = AddressStorage.getAddresses(this, key);
            AddressEntity newAddress = new AddressEntity(addressname, firstname, lastname, phone, city, district, ward, detailaddress);
            list.add(newAddress);
            AddressStorage.saveAddresses(this, key, list);
            setResult(RESULT_OK); // Báo kết quả thành công
            finish();
            overridePendingTransition(R.anim.fade_in_acti,R.anim.fade_out);
        });

    }
}
