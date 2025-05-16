package com.example.perfume;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.perfume.entities.OrderEntity;
import com.example.perfume.entities.OrderItemEntity;

import java.util.List;

public class OrderWithItems {
    @Embedded
    public OrderEntity order;
    @Relation(
            parentColumn = "orderId",
            entityColumn = "orderOwnerId",
            entity = OrderItemEntity.class
    )
    public List<OrderItemWithPerfume> items;
}
