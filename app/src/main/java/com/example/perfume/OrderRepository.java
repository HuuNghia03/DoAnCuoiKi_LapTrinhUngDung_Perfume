package com.example.perfume;
import android.content.Context;

import com.example.perfume.AppDatabase;
import com.example.perfume.dao.OrderDao;
import com.example.perfume.relation.OrderWithItems;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class OrderRepository {
    private final OrderDao orderDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public OrderRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        orderDao = db.orderDao();
    }

    public void getOrdersByUserId(int userId, Consumer<List<OrderWithItems>> callback) {
        executorService.execute(() -> {
            List<OrderWithItems> orders = orderDao.getOrdersByUserId(userId);
            callback.accept(orders);
        });
    }

    public void getOrderByOrderId(int orderId, Consumer<OrderWithItems> callback) {
        executorService.execute(() -> {
            OrderWithItems order = orderDao.getOrderByOrderId(orderId);
            callback.accept(order);
        });
    }
}
