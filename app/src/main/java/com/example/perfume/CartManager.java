package com.example.perfume;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.perfume.CartItem;
import com.example.perfume.PerfumeEntity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CartManager {
    private static CartManager instance;
    private final List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Thêm item, tăng quantity nếu đã tồn tại
    public void addItem(PerfumeEntity item,int quantity, int volume, float pricePerVolume) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getPerfume().getName().equals(item.getName())) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(item, quantity,volume, pricePerVolume));
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public void clearCart() {
        cartItems.clear();
    }

    public void removeFromCart(PerfumeEntity perfume) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getPerfume().equals(perfume)) {
                cartItems.remove(i);
                break;
            }
        }
    }

    public static void addToCart(Bundle bundle) {
        PerfumeEntity item = new PerfumeEntity();
        item.setName(bundle.getString("perfumeName"));
        item.setBrand(bundle.getString("brand"));
        item.setImg(bundle.getString("img"));
        item.setPrice(bundle.getFloat("price"));
        item.setConcentration(bundle.getString("concentration"));
     //   CartManager.getInstance().addItem(item);
    }

    public static void showAddToCartDialog(Context context, PerfumeEntity perfume,
                                           List<Integer> volumeList, List<Float> priceList) {
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_addcart_confirm, null);

        int defaultVolume = volumeList.get(0);
        float defaultPrice = priceList.get(0);
        CartItem cartItem = new CartItem(perfume, 1, defaultVolume, defaultPrice);

        TextView name = view.findViewById(R.id.name);
        TextView brand = view.findViewById(R.id.brand);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        TextView price = view.findViewById(R.id.price);
        ImageView buttonDecrease = view.findViewById(R.id.button_decrease);
        ImageView buttonIncrease = view.findViewById(R.id.button_increase);
        TextView quantityText = view.findViewById(R.id.quantity);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        LinearLayout volContainer = view.findViewById(R.id.volContainer);

        List<Float> priceListParsed = new ArrayList<>();
        for (String s : perfume.getPrices().split(",")) {
            priceListParsed.add(Float.parseFloat(s.trim()));
        }

        name.setText(perfume.getName());
        brand.setText(perfume.getBrand());
        quantityText.setText(String.valueOf(cartItem.getQuantity()));

        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);

        volContainer.removeAllViews();
        List<Button> buttonList = new ArrayList<>();

        for (int i = 0; i < volumeList.size(); i++) {
            Integer vol = volumeList.get(i);
            Button btn = new Button(context);
            btn.setText(vol + " mL");
            btn.setAllCaps(false);
            btn.setBackgroundColor(Color.parseColor("#f2f2f2"));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 0, 10, 0);
            btn.setLayoutParams(params);

            volContainer.addView(btn);
            buttonList.add(btn);

            btn.setOnClickListener(b -> {
                for (Button bItem : buttonList) {
                    bItem.setBackgroundColor(Color.parseColor("#f2f2f2"));
                    bItem.setTextColor(Color.BLACK);
                }
                btn.setBackgroundColor(Color.BLACK);
                btn.setTextColor(Color.WHITE);

                cartItem.setVolume(vol);
                int index = volumeList.indexOf(vol);
                float selectedPrice = priceListParsed.get(index);
                cartItem.setPricePerVolume(selectedPrice);
                updateTotalPrice(price, cartItem, volumeList, priceListParsed);
            });
        }

        buttonIncrease.setOnClickListener(view1 -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            quantityText.setText(String.valueOf(cartItem.getQuantity()));
            updateTotalPrice(price, cartItem, volumeList, priceListParsed);
        });

        buttonDecrease.setOnClickListener(view1 -> {
            int qty = cartItem.getQuantity();
            if (qty > 1) {
                cartItem.setQuantity(qty - 1);
                quantityText.setText(String.valueOf(cartItem.getQuantity()));
                updateTotalPrice(price, cartItem, volumeList, priceListParsed);
            }
        });

        price.setText(String.format(Locale.getDefault(), "$ %.2f", priceListParsed.get(0)));

        btnConfirm.setOnClickListener(view1 -> {
            CartManager.getInstance().addItem(perfume,
                    cartItem.getQuantity(),
                    cartItem.getVolume(),
                    cartItem.getPricePerVolume());
            dialog.dismiss();
            android.widget.Toast.makeText(context, perfume.getName() + " added to cart!", android.widget.Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    public static void updateTotalPrice(TextView priceTextView, CartItem cartItem,
                                        List<Integer> volumeList, List<Float> priceList) {
        int volume = cartItem.getVolume();
        int quantity = cartItem.getQuantity();
        int index = volumeList.indexOf(volume);

        if (index >= 0 && index < priceList.size()) {
            float unitPrice = priceList.get(index);
            float totalPrice = unitPrice * quantity;
            priceTextView.setText(String.format(Locale.getDefault(), "$ %.2f", totalPrice));
        }
    }
}
