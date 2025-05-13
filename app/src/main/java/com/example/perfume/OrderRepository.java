package com.example.perfume;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderRepository {
    private final OrderDao orderDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public interface OrderCallback {
        void onOrdersLoaded(List<OrderWithItems> orders);
    }

    public OrderRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        orderDao = db.orderDao();
    }

    public void getOrdersByUserId(int userId, OrderCallback callback) {
        executorService.execute(() -> {
            List<OrderWithItems> orders = orderDao.getOrdersByUserId(userId);
            callback.onOrdersLoaded(orders);
        });
    }
}

