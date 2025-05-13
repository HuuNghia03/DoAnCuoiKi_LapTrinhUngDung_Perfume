package com.example.perfume;

import androidx.room.Embedded;
import androidx.room.Relation;

public class OrderItemWithPerfume {
    @Embedded
    public OrderItemEntity orderItem;
    @Relation(
            parentColumn = "perfumeId",
            entityColumn = "id"
    )
    public PerfumeEntity perfume;
}
