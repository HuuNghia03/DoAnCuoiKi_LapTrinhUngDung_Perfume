package com.example.perfume.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders",
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = CASCADE
        ),
        indices = {@Index("userId")}
)
public class OrderEntity {
    @PrimaryKey(autoGenerate = true)
    public int orderId;

    public int userId;
    public long orderDate;

    @Embedded
    public AddressEntity address;

    public int getOrderId() {
        return orderId;
    }
}
