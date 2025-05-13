package com.example.perfume;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    private final MutableLiveData<List<OrderWithItems>> ordersLiveData = new MutableLiveData<>();

    public LiveData<List<OrderWithItems>> getOrdersLiveData() {
        return ordersLiveData;
    }

    public OrderViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchOrdersByUserId(Context context, int userId) {
        OrderRepository repo = new OrderRepository(context);
        repo.getOrdersByUserId(userId, ordersLiveData::postValue);
    }
}
