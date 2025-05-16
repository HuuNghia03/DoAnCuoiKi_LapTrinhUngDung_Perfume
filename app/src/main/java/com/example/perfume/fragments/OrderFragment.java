package com.example.perfume.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.perfume.Navigator;
import com.example.perfume.OrderItemWithPerfume;
import com.example.perfume.OrderViewModel;
import com.example.perfume.OrderWithItems;
import com.example.perfume.R;
import com.example.perfume.activities.HomeActivity;

public class OrderFragment extends Fragment {

    private OrderViewModel orderViewModel;
    private LinearLayout orderContainer;
    private TextView noOrder;
    private ImageView btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, container, false);

        orderContainer = view.findViewById(R.id.orderContainer);
        noOrder = view.findViewById(R.id.noOrder);
        btnBack = view.findViewById(R.id.btnBack);

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

        orderViewModel.fetchOrdersByUserId(requireContext(), userId);

        btnBack.setOnClickListener(v -> {
            ((HomeActivity) requireActivity()).setDetailFragment(
                    getParentFragmentManager().findFragmentByTag("4"));
            requireActivity().getSupportFragmentManager().popBackStack();
        });

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
        final int MAX_VISIBLE = 1;
        boolean shouldShowToggle = order.items.size() > MAX_VISIBLE;

        final int delayBetweenItems = 150; // ms
        int visibleIndex = 0;

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

            Glide.with(this).load(item.perfume.getImg()).into(image);

            totalPrice += item.orderItem.getPricePerVolume() * item.orderItem.getQuantity();
            totalProducts += item.orderItem.getQuantity();

            if (i >= MAX_VISIBLE) {
                perfumeView.setVisibility(View.GONE);
            }

            perfumeView.setAlpha(0f);
            perfumeList.addView(perfumeView);

            int finalI = i;
            perfumeView.postDelayed(() -> {
                if (finalI < MAX_VISIBLE) {
                    AlphaAnimation animation = new AlphaAnimation(0f, 1f);
                    animation.setDuration(400);
                    perfumeView.startAnimation(animation);
                    perfumeView.setAlpha(1f);
                }
            }, visibleIndex * delayBetweenItems);

            visibleIndex++;
        }

        tvOrderTotal.setText("Total price (" + totalProducts + " product" + (totalProducts > 1 ? "s" : "") + "): $" + String.format("%.2f", totalPrice));

        if (shouldShowToggle) {
            btnToggle.setVisibility(View.VISIBLE);
            btnToggle.setText("Show more");

            btnToggle.setOnClickListener(v -> {
                boolean isExpanded = btnToggle.getText().toString().equals("Show less");
                int childCount = perfumeList.getChildCount();

                if (isExpanded) {
                    for (int i = MAX_VISIBLE; i < childCount; i++) {
                        perfumeList.getChildAt(i).setVisibility(View.GONE);
                    }
                    btnToggle.setText("Show more");
                } else {
                    for (int i = MAX_VISIBLE; i < childCount; i++) {
                        View view = perfumeList.getChildAt(i);
                        view.setAlpha(0f);
                        view.setVisibility(View.VISIBLE);

                        int delay = (i - MAX_VISIBLE) * delayBetweenItems;
                        view.postDelayed(() -> {
                            AlphaAnimation anim = new AlphaAnimation(0f, 1f);
                            anim.setDuration(400);
                            view.startAnimation(anim);
                            view.setAlpha(1f);
                        }, delay);
                    }
                    btnToggle.setText("Show less");
                }
            });
        } else {
            btnToggle.setVisibility(View.GONE);
        }

        return orderView;
    }
}
