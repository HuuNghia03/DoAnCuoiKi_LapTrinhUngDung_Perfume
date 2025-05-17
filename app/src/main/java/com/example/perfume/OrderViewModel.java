package com.example.perfume;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.perfume.relation.OrderWithItems;

import java.util.List;
public class OrderViewModel extends AndroidViewModel {
    private final MutableLiveData<List<OrderWithItems>> ordersLiveData = new MutableLiveData<>();
    private final MutableLiveData<OrderWithItems> orderLiveData = new MutableLiveData<>();

    public LiveData<List<OrderWithItems>> getOrdersLiveData() {
        return ordersLiveData;
    }

    public LiveData<OrderWithItems> getOrderLiveData() {
        return orderLiveData;
    }

    public OrderViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchOrdersByUserId(Context context, int userId) {
        OrderRepository repo = new OrderRepository(context);
        repo.getOrdersByUserId(userId, ordersLiveData::postValue);
    }

    public void fetchOrderByOrderId(Context context, int orderId) {
        OrderRepository repo = new OrderRepository(context);
        repo.getOrderByOrderId(orderId, orderLiveData::postValue);
    }
}
