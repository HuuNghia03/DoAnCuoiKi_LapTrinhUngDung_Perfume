package com.example.perfume;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.perfume.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.perfume.PerfumeEntity;
import com.example.perfume.CartItem;
import com.example.perfume.CartManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CartActivity extends AppCompatActivity {

    private LinearLayout cartItemsContainer;
    private TextView totalPriceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartItemsContainer = findViewById(R.id.cart_items_container);
        totalPriceText = findViewById(R.id.total_price_text);


        List<CartItem> cartItems = CartManager.getInstance().getCartItems();

        displayCartItems(cartItems);
    }

    private void displayCartItems(List<CartItem> items) {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (CartItem cartItem : items) {
            PerfumeEntity item = cartItem.getPerfume();
            View itemView = inflater.inflate(R.layout.item_cart, cartItemsContainer, false);

            // Ánh xạ view
            ImageView img = itemView.findViewById(R.id.image);
            TextView title = itemView.findViewById(R.id.name);
            TextView brand = itemView.findViewById(R.id.brand);
            TextView volume = itemView.findViewById(R.id.volume);
            TextView price = itemView.findViewById(R.id.price);
            TextView quantityText = itemView.findViewById(R.id.quantity);
            TextView concentration = itemView.findViewById(R.id.concentration);
            TextView shipstudio = itemView.findViewById(R.id.shipstudio);
            ImageView btnIncrease = itemView.findViewById(R.id.button_increase);
            ImageView btnDecrease = itemView.findViewById(R.id.button_decrease);
            ImageView btnMoreOption= itemView.findViewById(R.id.button_more_options);

            // Gán dữ liệu
            Glide.with(this).load(item.getImg()).into(img);
            title.setText(item.getName());
            brand.setText(item.getBrand());
            volume.setText("50ml"); // Giả sử cố định hoặc bạn có thể thêm thuộc tính `volume` vào entity
            price.setText("$" + item.getPrice());
            concentration.setText(item.getConcentration());
            shipstudio.setText("ShipStudio"); // Hoặc giá trị thực tế nếu có
            quantityText.setText(String.valueOf(cartItem.getQuantity()));

            // Nút tăng số lượng
            btnIncrease.setOnClickListener(v -> {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                quantityText.setText(String.valueOf(cartItem.getQuantity()));
                updateTotalPrice();
            });

            // Nút giảm số lượng
            btnDecrease.setOnClickListener(v -> {
                int qty = cartItem.getQuantity();
                if (qty > 1) {
                    cartItem.setQuantity(qty - 1);
                    quantityText.setText(String.valueOf(cartItem.getQuantity()));
                    updateTotalPrice();
                }
            });

            btnMoreOption.setOnClickListener(v -> {
                View view = LayoutInflater.from(this).inflate(R.layout.bottom_delete_confirm, null);
                BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(view);

                TextView txtTitle = view.findViewById(R.id.txtDeleteTitle);
                TextView txtCancel = view.findViewById(R.id.txtCancel);

                txtTitle.setText("DELETE " + item.getName().toUpperCase());

                txtTitle.setOnClickListener(confirm -> {
                    CartManager.getInstance().removeFromCart(item);
                    cartItemsContainer.removeView(itemView);
                    updateTotalPrice();
                    dialog.dismiss();
                });

                txtCancel.setOnClickListener(cancel -> dialog.dismiss());

                dialog.show();
            });



            // Thêm view vào layout chính
            cartItemsContainer.addView(itemView);
        }
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        for (int i = 0; i < cartItemsContainer.getChildCount(); i++) {
            View itemView = cartItemsContainer.getChildAt(i);
            TextView quantityText = itemView.findViewById(R.id.quantity);
            TextView priceText = itemView.findViewById(R.id.price);

            int quantity = Integer.parseInt(quantityText.getText().toString().replace("x", ""));
            double price = Double.parseDouble(
                    priceText.getText().toString().replace("$", "").replace(",", ".")
            );


            totalPrice += price * quantity;
        }

        totalPriceText.setText("$" + String.format("%.2f", totalPrice));
    }

}
