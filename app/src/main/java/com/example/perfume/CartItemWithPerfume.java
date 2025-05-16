package com.example.perfume;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.perfume.entities.CartItemEntity;
import com.example.perfume.entities.PerfumeEntity;

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
