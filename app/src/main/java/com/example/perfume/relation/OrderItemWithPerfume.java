package com.example.perfume.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.perfume.entity.OrderItemEntity;
import com.example.perfume.entity.PerfumeEntity;

public class OrderItemWithPerfume {
    @Embedded
    public OrderItemEntity orderItem;
    @Relation(
            parentColumn = "perfumeId",
            entityColumn = "id"
    )
    public PerfumeEntity perfume;
}
