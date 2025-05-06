package com.example.perfume;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import com.example.perfume.CartItem;
import com.example.perfume.PerfumeEntity;
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
    public void addItem(PerfumeEntity item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getPerfume().getName().equals(item.getName())) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(item, 1));
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
        CartManager.getInstance().addItem(item);
    }
}
