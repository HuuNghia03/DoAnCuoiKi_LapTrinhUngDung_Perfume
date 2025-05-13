package com.example.perfume;

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

import com.example.perfume.entities.AddressEntity;

import java.util.ArrayList;
import java.util.List;

public class AddressConfirmActivity extends AppCompatActivity {
    private TextView addAddress;
    private LinearLayout addressItemsContainer;
    private RadioButton selectedRadioButton = null;
    private AddressEntity selectedAddress = null;
    private Button btnConfirm;
    CartWithItems cartWithItems;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_confirm);
        addAddress = findViewById(R.id.addAddress);
        btnConfirm = findViewById(R.id.btnConfirm);
        addAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddressAddActivity.class);
            startActivityForResult(intent, 100); // requestCode = 100
            overridePendingTransition(R.anim.zoom_in, R.anim.fade_out);
        });
        btnBack = findViewById(R.id.back_icon);
        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.pop_zoom_in, 0);
        });
        addressItemsContainer = findViewById(R.id.address_items_container);
        String key = "addresses_" + Navigator.getUserId(this); // hoặc user.getId()
        List<AddressEntity> list = AddressStorage.getAddresses(this, key);
        displayAddress(list);

        btnConfirm.setOnClickListener(v -> {
            if (selectedAddress == null) {
                Toast.makeText(this, "Please select an address", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(() -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selected_address", selectedAddress); // selectedAddress là AddressEntity
                setResult(RESULT_OK, resultIntent);
                finish();

                //  AppDatabase db = AppDatabase.getInstance(this);
            //    int userId = Navigator.getUserId(this);

               // cartWithItems = db.cartDao().getCartWithItems(userId);
               // if (cartWithItems == null || cartWithItems.items.isEmpty()) return;


             //   OrderEntity order = new OrderEntity();
              //  order.userId = userId;
             //   order.address = selectedAddress;
//                order.orderDate = System.currentTimeMillis();
//                long orderId = db.orderDao().insertOrder(order);
//                List<OrderItemEntity> orderItemList = new ArrayList<>();
//                //Thêm các item trong giỏ vào OrderItem
//                for (CartItemWithPerfume item : cartWithItems.items) {
//                   CartItemEntity cartItem = item.cartItem;
//                   OrderItemEntity orderItem = new OrderItemEntity();
//                   orderItem.setOrderOwnerId((int)orderId);
//                   orderItem.setPerfumeId(cartItem.getPerfumeId());
//                   orderItem.setQuantity(cartItem.getQuantity());
//                   orderItem.setVolume(cartItem.getVolume());
//                   orderItem.setPricePerVolume(cartItem.getPricePerVolume());
//                    orderItemList.add(orderItem);
//                }
//                db.orderDao().insertOrderItems(orderItemList);
//                db.cartDao().deleteCart(cartWithItems.cart);


            }).start();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Gọi hàm để load lại danh sách địa chỉ
            List<AddressEntity> list = AddressStorage.getAddresses(this, "addresses_" + Navigator.getUserId(this));
            displayAddress(list);
        }
    }

    private void displayAddress(List<AddressEntity> addresses) {
        addressItemsContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);
        for (AddressEntity address : addresses) {
            View view = inflater.inflate(R.layout.item_address, addressItemsContainer, false);
            TextView nameAddress = view.findViewById(R.id.nameAddress);
            TextView firstName = view.findViewById(R.id.firstName);
            TextView lastName = view.findViewById(R.id.lastName);
            TextView detail = view.findViewById(R.id.detail);
            TextView ward = view.findViewById(R.id.ward);
            TextView district = view.findViewById(R.id.district);
            TextView city = view.findViewById(R.id.city);
            TextView phone = view.findViewById(R.id.phone);

            nameAddress.setText(address.getNameAddress().toUpperCase());
            firstName.setText(address.getFirstName());
            lastName.setText(address.getLastName());
            detail.setText(address.getDetailAddress());
            ward.setText(address.getWard() + ", ");
            district.setText(address.getDistrict() + ", ");
            city.setText(address.getCity());
            phone.setText(address.getPhone());

            // Xử lý RadioButton
            RadioButton radioButton = view.findViewById(R.id.radioSelect);
            radioButton.setOnClickListener(v -> {
                if (selectedRadioButton != null) {
                    selectedRadioButton.setChecked(false);
                }
                radioButton.setChecked(true);
                selectedRadioButton = radioButton;
                selectedAddress = address; // lưu lại địa chỉ đã chọn
            });

            addressItemsContainer.addView(view);


        }

    }
}
