package com.example.perfume;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.util.Date;

public class OrderFragment extends Fragment {

    private OrderViewModel orderViewModel;
    private LinearLayout orderContainer;
    private TextView noOrder;
    private TextView btnToggle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, container, false);
        orderContainer = view.findViewById(R.id.orderContainer);
        noOrder = view.findViewById(R.id.noOrder);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        int userId = Navigator.getUserId(requireContext());

        orderViewModel.getOrdersLiveData().observe(getViewLifecycleOwner(), orders -> {
            orderContainer.removeAllViews();

            if (orders == null || orders.isEmpty()) {
                noOrder.setVisibility(View.VISIBLE);
            } else {
                noOrder.setVisibility(View.GONE);
                for (OrderWithItems order : orders) {
                    View orderView = createOrderView(order);
                    orderContainer.addView(orderView);
                }
            }
        });
        ;

        orderViewModel.fetchOrdersByUserId(requireContext(), userId);

        return view;
    }

    private View createOrderView(OrderWithItems order) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View orderView = inflater.inflate(R.layout.item_order, orderContainer, false);

        TextView tvOrderId = orderView.findViewById(R.id.tvOrderId);
        TextView tvOrderTotal = orderView.findViewById(R.id.tvOrderTotal);
        LinearLayout perfumeList = orderView.findViewById(R.id.perfumeList);
        TextView btnToggle = orderView.findViewById(R.id.btnToggle);

        tvOrderId.setText("Order #" + order.order.getOrderId());

        double totalPrice = 0.0;
        int totalProducts = 0;
        // Chỉ hiện tối đa 2 sản phẩm ban đầu
        int MAX_VISIBLE = 1;
        boolean shouldShowToggle = order.items.size() > MAX_VISIBLE;

        for (int i = 0; i < order.items.size(); i++) {
            OrderItemWithPerfume item = order.items.get(i);

            View perfumeView = inflater.inflate(R.layout.item_order_perfume, perfumeList, false);

            ImageView image = perfumeView.findViewById(R.id.perfumeImage);
            TextView name = perfumeView.findViewById(R.id.perfumeName);
            TextView volume = perfumeView.findViewById(R.id.perfumeVolume);
            TextView quantity = perfumeView.findViewById(R.id.perfumeQuantity);
            TextView price = perfumeView.findViewById(R.id.perfumePrice);

            name.setText(item.perfume.getName());
            volume.setText("Vol: " + item.orderItem.getVolume() + "mL");
            quantity.setText("x" + item.orderItem.getQuantity());
            price.setText(String.format("$%.2f", item.orderItem.getPricePerVolume()));

            // Load ảnh nếu có url (tuỳ bạn cập nhật sau)
            Glide.with(this).load(item.perfume.getImg()).into(image);

            // Tính tổng giá tiền
            totalPrice += item.orderItem.getPricePerVolume() * item.orderItem.getQuantity();
            totalProducts += item.orderItem.getQuantity();

            // Mặc định ẩn nếu quá 2 sản phẩm
            if (i >= MAX_VISIBLE) {
                perfumeView.setVisibility(View.GONE);
            }

            perfumeList.addView(perfumeView);
        }

        tvOrderTotal.setText("Total price (" + totalProducts + " product" + (totalProducts > 1 ? "s" : "") + "): $" + String.format("%.2f", totalPrice));

        if (shouldShowToggle) {
            btnToggle.setVisibility(View.VISIBLE);
            btnToggle.setText("Show more");
            btnToggle.setOnClickListener(v -> {
                boolean isExpanded = btnToggle.getText().toString().equals("Show less");
                for (int i = MAX_VISIBLE; i < perfumeList.getChildCount(); i++) {
                    perfumeList.getChildAt(i).setVisibility(isExpanded ? View.GONE : View.VISIBLE);
                }
                btnToggle.setText(isExpanded ? "Show more" : "Show less");

            });
        } else {
            btnToggle.setVisibility(View.GONE);
        }


        return orderView;
    }


}
