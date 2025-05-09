package com.example.perfume;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private LinearLayout cartItemsContainer;
    private TextView totalPriceText;
    AppDatabase db;
    private CartDao cartDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        db = AppDatabase.getInstance(this);
        cartItemsContainer = findViewById(R.id.cart_items_container);
        totalPriceText = findViewById(R.id.total_price_text);
        int userId = Navigator.getUserId(this);
        CartManager.getInstance(this).getCartWithItems(userId, new CartManager.CartCallback() {
            @Override
            public void onResult(CartWithItems cartWithItems) {
                // Xử lý kết quả trả về từ cơ sở dữ liệu
                List<CartItemWithPerfume> items = cartWithItems.items;
                displayCartItems(items,userId);
                // Cập nhật UI với dữ liệu giỏ hàng
            }
        });

    }

        private void displayCartItems(List<CartItemWithPerfume> items,int userId) {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (CartItemWithPerfume itemWithPerfume : items) {
            CartItemEntity cartItem = itemWithPerfume.cartItem;
            PerfumeEntity perfume = itemWithPerfume.perfume;

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
            ImageView btnMoreOption = itemView.findViewById(R.id.button_more_options);

            // Gán dữ liệu
            Glide.with(this).load(perfume.getImg()).into(img);
            title.setText(perfume.getName());
            brand.setText(perfume.getBrand());
            shipstudio.setText(perfume.getBrand().toUpperCase());
            volume.setText("Vol: "+cartItem.getVolume() + " mL");
            price.setText("$" + String.format("%.2f", itemWithPerfume.getTotalPrice()));
            concentration.setText(perfume.getConcentration());
            quantityText.setText(String.valueOf(cartItem.getQuantity()));

            // Nút tăng
            btnIncrease.setOnClickListener(v -> {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                quantityText.setText(String.valueOf(cartItem.getQuantity()));
                price.setText("$" + String.format("%.2f", itemWithPerfume.getTotalPrice()));
                updateTotalPrice(items);
               CartManager.getInstance(this).updateCartItem(cartItem);
            });

            // Nút giảm
            btnDecrease.setOnClickListener(v -> {
                int qty = cartItem.getQuantity();
                if (qty > 1) {
                    cartItem.setQuantity(qty - 1);
                    quantityText.setText(String.valueOf(cartItem.getQuantity()));
                    price.setText("$" + String.format("%.2f", itemWithPerfume.getTotalPrice()));
                    updateTotalPrice(items);
                    CartManager.getInstance(this).updateCartItem(cartItem);
                }
            });

            // Nút xóa
            btnMoreOption.setOnClickListener(v -> {
                View view = LayoutInflater.from(this).inflate(R.layout.bottom_delete_confirm, null);
                BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(view);

                TextView txtTitle = view.findViewById(R.id.txtDeleteTitle);
                TextView txtCancel = view.findViewById(R.id.txtCancel);

                txtTitle.setText("DELETE " + perfume.getName().toUpperCase());

                txtTitle.setOnClickListener(confirm -> {
                    CartManager.getInstance(this).removeItemFromCart(perfume,userId);
                    cartItemsContainer.removeView(itemView);
                    updateTotalPrice(items);
                    dialog.dismiss();
                });

                txtCancel.setOnClickListener(cancel -> dialog.dismiss());

                dialog.show();
            });

            cartItemsContainer.addView(itemView);
        }

        updateTotalPrice(items);
    }


    private void updateTotalPrice(List<CartItemWithPerfume> items) {
        double totalPrice = 0;
        TextView priceText = findViewById(R.id.price);
        for (CartItemWithPerfume item : items) {
            totalPrice += item.getTotalPrice();

        }

        totalPriceText.setText("$" + String.format("%.2f", totalPrice));
    }

}
