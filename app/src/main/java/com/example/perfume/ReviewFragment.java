package com.example.perfume;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.perfume.entities.AddressEntity;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment {
    private TextView nameUser, phoneUser, detailAddress, city, paymentMethod, subPrice, shipPrice, totalPrice;
    private LinearLayout orderItemsContainer, expressDelivery, standardDelivery;
    private AppDatabase appDatabase;
    private int userId;
    CartWithItems cartWithItems;
    List<CartItemWithPerfume> cartItems;
    private float ShipPrice, TotalPrice, SubPrice;

    private Button btnConfirm;
    private AddressEntity selectedAddress;
    private String deliveryMethod;
    private String PaymentMethod;

    public void updateData(AddressEntity address, String delivery, String payment) {
        this.selectedAddress = address;
        this.deliveryMethod = delivery;
        this.PaymentMethod = payment;
        updateUI();
    }
    private void updateUI() {
        // cập nhật giao diện khi dữ liệu thay đổi
        if (selectedAddress != null) {
            String Name = selectedAddress.getFirstName() + " " + selectedAddress.getLastName();
            String City = selectedAddress.getWard() + ", " + selectedAddress.getDistrict() + ", " + selectedAddress.getCity();

            detailAddress.setText(selectedAddress.getDetailAddress());
            city.setText(City);
            nameUser.setText(Name);
            phoneUser.setText(selectedAddress.getPhone());

        }
        if (PaymentMethod != null) {
            Toast.makeText(requireContext(), "paymet::" + PaymentMethod, Toast.LENGTH_SHORT).show();
            paymentMethod.setText(""+PaymentMethod.toUpperCase());

        }
        if (deliveryMethod != null) {
            if (deliveryMethod.equals("standard")) {
                standardDelivery.setVisibility(View.VISIBLE);
                expressDelivery.setVisibility(View.GONE);
                ShipPrice = 7.99f;
                shipPrice.setText(String.format("%.2f $", ShipPrice));

            } else {
                standardDelivery.setVisibility(View.GONE);
                expressDelivery.setVisibility(View.VISIBLE);
                ShipPrice = 13.99f;
                shipPrice.setText(String.format("%.2f $", ShipPrice));

            }

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        appDatabase = AppDatabase.getInstance(requireContext());
        viewBind(view);
        updateUI();
        loadPerfume();
        btnConfirm.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
            performOrder();

            Intent intent = new Intent(requireContext(), HomeActivity.class);
            startActivity(intent);


        });

        return view;
    }

    private void viewBind(View view) {
        standardDelivery = view.findViewById(R.id.standardDelivery);
        expressDelivery = view.findViewById(R.id.expressDelivery);
        nameUser = view.findViewById(R.id.nameUser);
        phoneUser = view.findViewById(R.id.phoneUser);
        detailAddress = view.findViewById(R.id.detailAddress);
        paymentMethod = view.findViewById(R.id.paymentMethod);
        subPrice = view.findViewById(R.id.subPrice);
        shipPrice = view.findViewById(R.id.shipPrice);
        totalPrice = view.findViewById(R.id.totalPrice);
        city = view.findViewById(R.id.city);
        orderItemsContainer = view.findViewById(R.id.order_items_container);
        btnConfirm = view.findViewById(R.id.btnConfirm);
    }

    private void loadPerfume() {
        new Thread(() -> {
            userId = Navigator.getUserId(requireContext());
            cartWithItems = appDatabase.cartDao().getCartWithItems(userId);
            if (cartWithItems == null || cartWithItems.items.isEmpty()) return;

            cartItems = cartWithItems.items;

            // Cập nhật giao diện phải làm trên UI thread
            requireActivity().runOnUiThread(() -> {
                orderItemsContainer.removeAllViews();

                LayoutInflater inflater = LayoutInflater.from(requireContext());
                for (CartItemWithPerfume item : cartItems) {
                    View view = inflater.inflate(R.layout.item_order_perfume, orderItemsContainer, false);
                    ImageView image = view.findViewById(R.id.perfumeImage);
                    TextView name = view.findViewById(R.id.perfumeName);
                    TextView volume = view.findViewById(R.id.perfumeVolume);
                    TextView quantity = view.findViewById(R.id.perfumeQuantity);
                    TextView price = view.findViewById(R.id.perfumePrice);

                    name.setText(item.perfume.getName());
                    volume.setText("Vol: " + item.cartItem.getVolume() + "mL");
                    quantity.setText("x" + item.cartItem.getQuantity());
                    price.setText(String.format("$%.2f", item.cartItem.getPricePerVolume()));

                    Glide.with(this).load(item.perfume.getImg()).into(image);
                    // Tính tổng giá tiền
                    SubPrice += item.cartItem.getPricePerVolume() * item.cartItem.getQuantity();
                    orderItemsContainer.addView(view);
                }
                subPrice.setText(String.format("%.2f $", SubPrice));
                totalPrice.setText(String.format("%.2f $", SubPrice - ShipPrice));
            });
        }).start();
    }

    private void performOrder() {

        new Thread(() -> {
            userId = Navigator.getUserId(requireContext());
            cartWithItems = appDatabase.cartDao().getCartWithItems(userId);
            if (cartWithItems == null || cartWithItems.items.isEmpty()) return;

            OrderEntity order = new OrderEntity();
            order.userId = userId;
            order.address = selectedAddress;
            order.orderDate = System.currentTimeMillis();
            long orderId = appDatabase.orderDao().insertOrder(order);
            List<OrderItemEntity> orderItemList = new ArrayList<>();
            //Thêm các item trong giỏ vào OrderItem
            for (CartItemWithPerfume item : cartWithItems.items) {
                CartItemEntity cartItem = item.cartItem;
                OrderItemEntity orderItem = new OrderItemEntity();
                orderItem.setOrderOwnerId((int) orderId);
                orderItem.setPerfumeId(cartItem.getPerfumeId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setVolume(cartItem.getVolume());
                orderItem.setPricePerVolume(cartItem.getPricePerVolume());
                orderItemList.add(orderItem);
            }
            appDatabase.orderDao().insertOrderItems(orderItemList);
            appDatabase.cartDao().deleteCart(cartWithItems.cart);
        }).start();

    }

}

