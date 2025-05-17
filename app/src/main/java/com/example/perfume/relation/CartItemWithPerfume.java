package com.example.perfume.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.perfume.entity.CartItemEntity;
import com.example.perfume.entity.PerfumeEntity;

public class CartItemWithPerfume {

    @Embedded
    public CartItemEntity cartItem;

    @Relation(
            parentColumn = "perfumeId",
            entityColumn = "id"
    )
    public PerfumeEntity perfume;

    public float getTotalPrice() {
        return cartItem.getQuantity()  * cartItem.getPricePerVolume();
    }
}
