package com.example.perfume.fragment;

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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.perfume.AppDatabase;
import com.example.perfume.activity.AddressConfirmActivity;
import com.example.perfume.relation.CartItemWithPerfume;
import com.example.perfume.relation.CartWithItems;
import com.example.perfume.Navigator;
import com.example.perfume.R;
import com.example.perfume.activity.HomeActivity;
import com.example.perfume.entity.AddressEntity;
import com.example.perfume.entity.CartItemEntity;
import com.example.perfume.entity.OrderEntity;
import com.example.perfume.entity.OrderItemEntity;

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
    private Integer totalQuantity = 0;

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
            paymentMethod.setText("" + PaymentMethod.toUpperCase());

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
        TotalPrice = SubPrice + ShipPrice;
        totalPrice.setText(String.format("%.2f $", TotalPrice));


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
            showAddressDialog();
//            Intent intent = new Intent(requireContext(), HomeActivity.class);
//            startActivity(intent);


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
                    totalQuantity += item.cartItem.getQuantity();
                    quantity.setText("x" + item.cartItem.getQuantity());
                    price.setText(String.format("$%.2f", item.cartItem.getPricePerVolume()));

                    Glide.with(this).load(item.perfume.getImg()).into(image);
                    // Tính tổng giá tiền
                    SubPrice += item.cartItem.getPricePerVolume() * item.cartItem.getQuantity();
                    orderItemsContainer.addView(view);
                }
                TotalPrice = SubPrice + ShipPrice;
                subPrice.setText(String.format("%.2f $", SubPrice));
                totalPrice.setText(String.format("%.2f $", TotalPrice));

            });
        }).start();
    }

    private void performOrder() {

        new Thread(() -> {
            userId = Navigator.getUserId(requireContext());
            cartWithItems = appDatabase.cartDao().getCartWithItems(userId);
            if (cartWithItems == null || cartWithItems.items.isEmpty()) return;

            OrderEntity order = new OrderEntity();
            order.setUserId(userId);
            order.setAddress(selectedAddress);
            order.setOrderDate(System.currentTimeMillis());
            order.setTotalPrice(TotalPrice);
            order.setDeliveryMethod(deliveryMethod);
            order.setPaymentMethod(PaymentMethod);
            order.setShipPrice(ShipPrice);
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

    private void showAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_thanks, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Ánh xạ view

        Button btnContinue = dialogView.findViewById(R.id.btnContinue);
        ImageView btnClose = dialogView.findViewById(R.id.btnClose);
        TextView subPrice = dialogView.findViewById(R.id.subPrice);
        TextView shipPrice = dialogView.findViewById(R.id.shipPrice);
        TextView totalPrice = dialogView.findViewById(R.id.totalPrice);
        TextView Qty = dialogView.findViewById(R.id.qty);
        Qty.setText("Sub_Total (Qty: " + String.valueOf(totalQuantity) + ")");
        subPrice.setText("$ " + String.format("%.2f", SubPrice));
        shipPrice.setText("$ " + String.format("%.2f", ShipPrice));
        totalPrice.setText("$ " + String.format("%.2f", TotalPrice));


        // Xử lý nút
        btnContinue.setOnClickListener(v -> {

            Intent intent = new Intent(requireContext(), HomeActivity.class);
            intent.putExtra("targetFragment", "order_detail"); // đặt tên fragment bạn muốn mở
            startActivity(intent);
            requireActivity().finish();


        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

}

