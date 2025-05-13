package com.example.perfume;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartManager {
    private static CartManager instance;
    private CartDao cartDao;
    private AppDatabase db;

    private CartManager(Context context) {
        db = AppDatabase.getInstance(context);
        cartDao = db.cartDao();
    }

    public static synchronized CartManager getInstance(Context context) {
        if (instance == null) {
            instance = new CartManager(context.getApplicationContext());
        }
        return instance;
    }

    // Thêm item, tăng quantity nếu đã tồn tại
    // Thêm một item vào giỏ hàng, nếu đã có thì tăng số lượng
    public void addItemToCart(PerfumeEntity perfume, int userId, int quantity, int volume, float pricePerVolume) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            // Lấy giỏ hàng của người dùng
            CartEntity cart = cartDao.getCartByUserId(userId);
            if (cart == null) {
                // Nếu không có giỏ hàng cho người dùng, tạo mới giỏ hàng
                cart = new CartEntity();
                cart.userId = userId;
                long cartId = cartDao.insertCart(cart); // Thêm giỏ hàng mới vào DB

                // Thêm item vào giỏ hàng
                CartItemEntity cartItem = new CartItemEntity(
                        (int) cartId, // Lưu cartId vào CartItem
                        perfume.getId(), // Lưu perfumeId vào CartItem
                        quantity, // Số lượng của item
                        volume, // Lưu volume của item
                        pricePerVolume // Lưu pricePerVolume của item
                );
                cartDao.insertCartItem(cartItem); // Thêm item vào DB
            } else {
                // Nếu giỏ hàng đã có, kiểm tra xem item đã có trong giỏ chưa
                CartItemEntity cartItem = cartDao.getExistingItem(cart.cartId, perfume.getId(), volume);
                if (cartItem != null) {
                    // Nếu đã có item trong giỏ, chỉ cần tăng số lượng và cập nhật lại giá
                    cartItem.setQuantity(cartItem.getQuantity() + quantity); // Cập nhật số lượng
                    cartItem.setPricePerVolume(pricePerVolume); // Cập nhật lại giá nếu có thay đổi
                    cartDao.updateCartItem(cartItem); // Cập nhật item trong DB
                } else {
                    // Nếu item chưa có trong giỏ, tạo mới và thêm vào giỏ
                    cartItem = new CartItemEntity(
                            cart.cartId, // Lưu cartId vào CartItem
                            perfume.getId(), // Lưu perfumeId vào CartItem
                            quantity, // Số lượng của item
                            volume, // Lưu volume của item
                            pricePerVolume // Lưu pricePerVolume của item
                    );
                    cartDao.insertCartItem(cartItem); // Thêm item mới vào giỏ
                }
            }
        });
    }


    // Lấy giỏ hàng của người dùng, bao gồm tất cả các món hàng trong giỏ
    public void getCartWithItems(int userId, CartCallback callback) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            // Truy vấn cơ sở dữ liệu trên thread phụ
            CartWithItems cartWithItems = db.cartDao().getCartWithItems(userId);

            // Sau khi truy vấn xong, bạn có thể trả kết quả về main thread
            new Handler(Looper.getMainLooper()).post(() -> {
                // Gọi callback để xử lý kết quả trên main thread
                callback.onResult(cartWithItems);
            });
        });
    }

    public void updateCartItem(CartItemEntity cartItem) {
        new Thread(() -> cartDao.updateCartItem(cartItem)).start();
    }

    // Định nghĩa một interface để nhận kết quả trả về
    public interface CartCallback {
        void onResult(CartWithItems cartWithItems);
    }

    // Xóa một item khỏi giỏ hàng của người dùng
    public void removeItemFromCart(PerfumeEntity perfume, int userId) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            CartEntity cart = cartDao.getCartByUserId(userId);
            if (cart != null) {
                CartItemEntity cartItem = cartDao.getCartItemByPerfumeId(cart.cartId, perfume.getId());
                if (cartItem != null) {
                    cartDao.deleteCartItem(cartItem); // Xóa item khỏi giỏ hàng
                }
            }
        });
    }

    // Xóa toàn bộ giỏ hàng của người dùng
    public void clearCart(int userId) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            CartEntity cart = cartDao.getCartByUserId(userId);
            if (cart != null) {
                cartDao.clearCartItems(cart.cartId); // Xóa toàn bộ item trong giỏ
                cartDao.deleteCart(cart); // Xóa giỏ hàng
            }
        });
    }


    public static void showAddToCartDialog(Context context, PerfumeEntity perfume,
                                           List<Integer> volumeList, List<Float> priceList) {
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_addcart_confirm, null);

        // Kiểm tra nếu danh sách volumeList hoặc priceList là rỗng
        if (volumeList.isEmpty() || priceList.isEmpty()) {
            Toast.makeText(context, "Volume or price list is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        final int[] Quantity = {1};
        final int[] Volume = {0};
        final float[] PricePerVolume = new float[1];


        // Ánh xạ các view
        TextView name = view.findViewById(R.id.name);
        TextView brand = view.findViewById(R.id.brand);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        TextView price = view.findViewById(R.id.price);
        ImageView buttonDecrease = view.findViewById(R.id.button_decrease);
        ImageView buttonIncrease = view.findViewById(R.id.button_increase);
        TextView quantityText = view.findViewById(R.id.quantity);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        LinearLayout volContainer = view.findViewById(R.id.volContainer);

        // Cập nhật các view với thông tin từ perfume và cartItemEntity
        name.setText(perfume.getName());
        brand.setText(perfume.getBrand());
        quantityText.setText(String.valueOf(Quantity[0]));

        // Tạo dialog BottomSheet
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);

        // Xử lý các nút chọn volume
        volContainer.removeAllViews();
        List<Button> buttonList = new ArrayList<>();
        for (int i = 0; i < volumeList.size(); i++) {
            Integer vol = volumeList.get(i);
            Button btn = new Button(context);
            btn.setText(vol + " mL");
            btn.setAllCaps(false);
            btn.setBackgroundColor(Color.parseColor("#f2f2f2"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 0, 10, 0);
            btn.setLayoutParams(params);

            volContainer.addView(btn);
            buttonList.add(btn);

            // Xử lý sự kiện khi chọn volume
            btn.setOnClickListener(b -> {
                for (Button bItem : buttonList) {
                    bItem.setBackgroundColor(Color.parseColor("#f2f2f2"));
                    bItem.setTextColor(Color.BLACK);
                }
                btn.setBackgroundColor(Color.BLACK);
                btn.setTextColor(Color.WHITE);

                // Cập nhật volume và giá theo volume đã chọn
                // cartItemEntity.setVolume(vol);
                Volume[0] = vol;
                int index = volumeList.indexOf(vol);
                PricePerVolume[0] = priceList.get(index); // cập nhật giá
                updateTotalPrice(price, PricePerVolume[0], Quantity[0]);
            });
        }

        // Cập nhật giá và số lượng khi nhấn tăng/giảm
        buttonIncrease.setOnClickListener(view1 -> Quantity[0] = updateQuantity(Quantity[0], PricePerVolume[0], quantityText, price, true));
        buttonDecrease.setOnClickListener(view1 -> Quantity[0] = updateQuantity(Quantity[0], PricePerVolume[0], quantityText, price, false));
        // Cập nhật giá mặc định ban đầu
        price.setText(String.format(Locale.getDefault(), "$ %.2f", priceList.get(0)));

        // Xử lý sự kiện khi nhấn xác nhận
        btnConfirm.setOnClickListener(view1 -> {
            int userId = Navigator.getUserId(context); // lấy userId

            CartManager.getInstance(context).addItemToCart(perfume,
                    userId,  // Truyền userId thực tế vào đây
                    Quantity[0],
                    Volume[0],
                    PricePerVolume[0]);

            dialog.dismiss();
            Toast.makeText(context, perfume.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
        });


        dialog.show();
    }

    // Hàm để cập nhật tổng giá
    private static void updateTotalPrice(TextView priceView, float Price, int Quantity) {
        float totalPrice = Price * Quantity;
        priceView.setText(String.format(Locale.getDefault(), "$ %.2f", totalPrice));
    }

    // Hàm để cập nhật số lượng
    private static int updateQuantity(int Quantity, float Price, TextView quantityText, TextView priceText, boolean increase) {

        if (increase) {
            Quantity = Quantity + 1;
        } else if (Quantity > 1) {
            Quantity = Quantity - 1;
        }
        quantityText.setText(String.valueOf(Quantity));
        updateTotalPrice(priceText, Price, Quantity);
        return Quantity;
    }


}
