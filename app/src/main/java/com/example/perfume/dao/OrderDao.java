package com.example.perfume.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.perfume.OrderWithItems;
import com.example.perfume.entities.OrderEntity;
import com.example.perfume.entities.OrderItemEntity;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    long insertOrder(OrderEntity order);

    @Insert
    void insertOrderItems(List<OrderItemEntity> items);

    @Transaction
    @Query("SELECT * FROM orders WHERE userId = :userId")
    List<OrderWithItems> getOrdersByUserId(int userId);

}
