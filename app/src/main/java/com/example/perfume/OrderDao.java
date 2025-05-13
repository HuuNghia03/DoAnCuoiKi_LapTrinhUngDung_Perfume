package com.example.perfume;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

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
