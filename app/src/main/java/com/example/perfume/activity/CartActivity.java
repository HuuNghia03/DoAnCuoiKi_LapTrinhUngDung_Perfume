package com.example.perfume.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.example.perfume.AppDatabase;
import com.example.perfume.relation.CartItemWithPerfume;
import com.example.perfume.CartManager;
import com.example.perfume.Navigator;
import com.example.perfume.R;
import com.example.perfume.entity.CartItemEntity;
import com.example.perfume.entity.PerfumeEntity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private LinearLayout cartItemsContainer, noCart;
    private NestedScrollView cartSpace;
    private TextView totalPriceText;
    private Button btnOrder;
    private ImageView btnBack;

    private AppDatabase db;
    private boolean isEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupListeners();

        db = AppDatabase.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartItemsContainer.removeAllViews();
        loadCart();
    }

    private void initViews() {
        cartItemsContainer = findViewById(R.id.cart_items_container);
        totalPriceText = findViewById(R.id.total_price_text);
        btnOrder = findViewById(R.id.order_button);
        btnBack = findViewById(R.id.back_icon);
        noCart = findViewById(R.id.noCart);
        cartSpace = findViewById(R.id.cartSpace);
    }

    private void setupListeners() {
        btnOrder.setOnClickListener(v -> {
            if (isEmpty) {
                Toast.makeText(this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            startActivity(new Intent(this, CheckoutActivity.class));
            overridePendingTransition(R.anim.zoom_in, 0);
        });

        btnBack.setOnClickListener(v -> finish());
    }


    private void loadCart() {
        int userId = Navigator.getUserId(this);
        CartManager.getInstance(this).getCartWithItems(userId, cartWithItems -> {
            if (cartWithItems != null && cartWithItems.items != null && !cartWithItems.items.isEmpty()) {
                noCart.setVisibility(View.GONE);
                cartSpace.setVisibility(View.VISIBLE);
                displayCartItems(cartWithItems.items, userId);
                isEmpty=false;
            } else {
                cartSpace.setVisibility(View.GONE);
                noCart.setVisibility(View.VISIBLE);
                totalPriceText.setText("--.--");
               isEmpty=true;

            }
        });
    }

    private void displayCartItems(List<CartItemWithPerfume> items, int userId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        cartItemsContainer.removeAllViews();

        for (CartItemWithPerfume itemWithPerfume : items) {
            View itemView = inflater.inflate(R.layout.item_cart, cartItemsContainer, false);

            CartItemEntity cartItem = itemWithPerfume.cartItem;
            PerfumeEntity perfume = itemWithPerfume.perfume;

            ImageView img = itemView.findViewById(R.id.image);
            TextView title = itemView.findViewById(R.id.name);
            TextView brand = itemView.findViewById(R.id.brand);
            TextView shipStudio = itemView.findViewById(R.id.shipstudio);
            TextView volume = itemView.findViewById(R.id.volume);
            TextView concentration = itemView.findViewById(R.id.concentration);
            TextView price = itemView.findViewById(R.id.price);
            TextView quantityText = itemView.findViewById(R.id.quantity);
            ImageView btnIncrease = itemView.findViewById(R.id.button_increase);
            ImageView btnDecrease = itemView.findViewById(R.id.button_decrease);
            ImageView btnMoreOption = itemView.findViewById(R.id.button_more_options);

            // Binding data
            Glide.with(this).load(perfume.getImg()).into(img);
            title.setText(perfume.getName());
            brand.setText(perfume.getBrand().toUpperCase());
            shipStudio.setText(perfume.getBrand().toUpperCase());
            volume.setText("Vol: " + cartItem.getVolume() + " mL");
            concentration.setText(perfume.getConcentration().toUpperCase());
            quantityText.setText(String.valueOf(cartItem.getQuantity()));
            price.setText("$ " + String.format(Locale.US,"%.2f", itemWithPerfume.getTotalPrice()));

            // Increase quantity
            btnIncrease.setOnClickListener(v -> {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                quantityText.setText(String.valueOf(cartItem.getQuantity()));
                price.setText("$ " + String.format(Locale.US,"%.2f", itemWithPerfume.getTotalPrice()));
                updateTotalPrice(items);
                CartManager.getInstance(this).updateCartItem(cartItem);
            });

            // Decrease quantity
            btnDecrease.setOnClickListener(v -> {
                int qty = cartItem.getQuantity();
                if (qty > 1) {
                    cartItem.setQuantity(qty - 1);
                    quantityText.setText(String.valueOf(cartItem.getQuantity()));
                    price.setText("$ " + String.format(Locale.US,"%.2f", itemWithPerfume.getTotalPrice()));
                    updateTotalPrice(items);
                    CartManager.getInstance(this).updateCartItem(cartItem);
                }
            });

            // Delete item
            btnMoreOption.setOnClickListener(v -> showDeleteDialog(perfume, userId, itemView, items));

            cartItemsContainer.addView(itemView);
        }

        updateTotalPrice(items);
    }

    private void showDeleteDialog(PerfumeEntity perfume, int userId, View itemView, List<CartItemWithPerfume> items) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.bottom_delete_confirm, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);

        TextView txtTitle = dialogView.findViewById(R.id.txtDeleteTitle);
        TextView txtCancel = dialogView.findViewById(R.id.txtCancel);

        txtTitle.setText("DELETE " + perfume.getName().toUpperCase());

        txtTitle.setOnClickListener(confirm -> {
            CartManager.getInstance(this).removeItemFromCart(perfume, userId);
            cartItemsContainer.removeView(itemView);
            updateTotalPrice(items);
            dialog.dismiss();
        });

        txtCancel.setOnClickListener(cancel -> dialog.dismiss());

        dialog.show();
    }

    private void updateTotalPrice(List<CartItemWithPerfume> items) {
        double totalPrice = 0;
        for (CartItemWithPerfume item : items) {
            totalPrice += item.getTotalPrice();
        }
        totalPriceText.setText("$ " + String.format(Locale.US,"%.2f", totalPrice));
    }
}
