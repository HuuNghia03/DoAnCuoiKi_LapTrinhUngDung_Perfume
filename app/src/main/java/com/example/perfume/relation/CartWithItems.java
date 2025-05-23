package com.example.perfume.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.perfume.entity.CartEntity;
import com.example.perfume.entity.CartItemEntity;

import java.util.List;

public class CartWithItems {
    @Embedded
    public CartEntity cart;

    @Relation(
            parentColumn = "cartId",
            entityColumn = "cartOwnerId",
            entity = CartItemEntity.class
    )
    public List<CartItemWithPerfume> items;
}
