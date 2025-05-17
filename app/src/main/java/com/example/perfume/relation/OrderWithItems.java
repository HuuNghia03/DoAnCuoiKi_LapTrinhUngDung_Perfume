package com.example.perfume.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.perfume.entity.OrderEntity;
import com.example.perfume.entity.OrderItemEntity;

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
