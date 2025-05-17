package com.example.perfume.entity;


import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "oder_item",
        foreignKeys = {
                @ForeignKey(
                        entity = OrderEntity.class,
                        parentColumns = "orderId",
                        childColumns = "orderOwnerId",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = PerfumeEntity.class,
                        parentColumns = "id",
                        childColumns = "perfumeId"
                )
        },
        indices = {@Index("orderOwnerId"),@Index("perfumeId")}
)
public class OrderItemEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "orderItemId")
    private int orderItemId;

    private int orderOwnerId;
    private int perfumeId;
    private int quantity;
    private int volume;
    private float pricePerVolume;
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getPerfumeId() {
        return perfumeId;
    }

    public float getPricePerVolume() {
        return pricePerVolume;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getVolume() {
        return volume;
    }

    public void setOrderOwnerId(int orderOwnerId) {
        this.orderOwnerId = orderOwnerId;
    }

    public int getOrderOwnerId() {
        return orderOwnerId;
    }

    public void setPerfumeId(int perfumeId) {
        this.perfumeId = perfumeId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setPricePerVolume(float pricePerVolume) {
        this.pricePerVolume = pricePerVolume;
    }
}
