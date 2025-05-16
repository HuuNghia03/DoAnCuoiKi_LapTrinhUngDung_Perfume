package com.example.perfume;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.perfume.entities.OrderItemEntity;
import com.example.perfume.entities.PerfumeEntity;

public class OrderItemWithPerfume {
    @Embedded
    public OrderItemEntity orderItem;
    @Relation(
            parentColumn = "perfumeId",
            entityColumn = "id"
    )
    public PerfumeEntity perfume;
}
