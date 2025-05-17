package com.example.perfume.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.perfume.AppDatabase;
import com.example.perfume.OrderViewModel;
import com.example.perfume.R;
import com.example.perfume.activity.HomeActivity;
import com.example.perfume.entity.AddressEntity;
import com.example.perfume.entity.OrderEntity;
import com.example.perfume.relation.OrderItemWithPerfume;
import com.example.perfume.relation.OrderWithItems;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDetailFragment extends Fragment {
    private int orderId;
    private TextView orderNumber, orderDate, orderQty, orderUpdate, subPrice, shipPrice, totalPrice, nameUser, phoneUser, detailAddress, city;
    private LinearLayout OrderItemContainer;
 private ImageView btnBack ;
    private OrderViewModel orderViewModel;

    public OrderDetailFragment(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_detail, container, false);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        viewBind(view);
        orderViewModel.fetchOrderByOrderId(requireContext(), orderId);
        displayOrder();
        btnBack.setOnClickListener(v -> {
            ((HomeActivity) requireActivity()).setDetailFragment(
                    getParentFragmentManager().findFragmentByTag("4"));
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        return view;


    }
        private void viewBind (View view){
            orderNumber = view.findViewById(R.id.orderNumber);
            orderDate = view.findViewById(R.id.orderDate);
            orderQty = view.findViewById(R.id.orderQty);
            orderUpdate = view.findViewById(R.id.orderUpdate);
            subPrice = view.findViewById(R.id.subPrice);
            shipPrice = view.findViewById(R.id.shipPrice);
            totalPrice = view.findViewById(R.id.totalPrice);
            nameUser = view.findViewById(R.id.nameUser);
            phoneUser = view.findViewById(R.id.phoneUser);
            detailAddress = view.findViewById(R.id.detailAddress);
            city = view.findViewById(R.id.city);
            btnBack = view.findViewById(R.id.btnBack);
            OrderItemContainer = view.findViewById(R.id.order_items_container);

        }
        private void displayOrder(){
            orderViewModel.getOrderLiveData().observe(getViewLifecycleOwner(), order -> {
                orderNumber.setText("ORDER #"+order.order.getOrderId());
                long timeMillis = order.order.getOrderDate(); // giá trị timestamp lấy ra
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                String formattedDate = sdf.format(new Date(timeMillis));
                orderDate.setText("Order date: "+formattedDate);
                orderUpdate.setText("Order date: "+formattedDate);
                shipPrice.setText(String.format(Locale.US,"$ %.2f",order.order.getShipPrice()));
                totalPrice.setText(String.format(Locale.US,"$ %.2f", order.order.getTotalPrice()));
                AddressEntity addressEntity;
                addressEntity=order.order.getAddress();
                String name = addressEntity.getFirstName() + " " + addressEntity.getLastName();
                String cityText = addressEntity.getWard() + ", " +
                        addressEntity.getDistrict() + ", " +
                        addressEntity.getCity();

                detailAddress.setText(addressEntity.getDetailAddress());
                city.setText(cityText);
                nameUser.setText(name);
                phoneUser.setText(addressEntity.getPhone());
                displayOrderItem(order.items);



            });

        }
        private void displayOrderItem(List<OrderItemWithPerfume> items){
            int totalProducts = 0;
            float SubPrice=0.0f;
            LayoutInflater inflater = LayoutInflater.from(requireContext());
            for(OrderItemWithPerfume item:items){
                View perfumeView = inflater.inflate(R.layout.item_order_perfume, OrderItemContainer, false);
                ImageView image = perfumeView.findViewById(R.id.perfumeImage);
                TextView name = perfumeView.findViewById(R.id.perfumeName);
                TextView volume = perfumeView.findViewById(R.id.perfumeVolume);
                TextView quantity = perfumeView.findViewById(R.id.perfumeQuantity);
                TextView price = perfumeView.findViewById(R.id.perfumePrice);
                TextView tvReturn= perfumeView.findViewById(R.id.tvReturn);
                TextView brand= perfumeView.findViewById(R.id.brand);

                tvReturn.setVisibility(View.VISIBLE);
                name.setText(item.perfume.getName());
                name.setText(item.perfume.getBrand());
                volume.setText("Vol: " + item.orderItem.getVolume() + "mL");
                quantity.setText("x" + item.orderItem.getQuantity());
                price.setText(String.format(Locale.US,"$ %.2f", item.orderItem.getPricePerVolume()));

                Glide.with(this).load(item.perfume.getImg()).into(image);

                totalProducts += item.orderItem.getQuantity();
                SubPrice+=item.orderItem.getPricePerVolume()+item.orderItem.getQuantity();
                OrderItemContainer.addView(perfumeView);

            }
            orderQty.setText(totalProducts+" Item(s)");
            subPrice.setText(String.format(Locale.US,"$ %.2f", SubPrice));
        }
    }