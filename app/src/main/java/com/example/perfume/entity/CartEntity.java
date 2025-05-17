package com.example.perfume.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart",
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "id",
                childColumns  = "userId",
                onDelete = CASCADE
        ),
        indices = {@Index("userId")}
)
public class CartEntity {
    @PrimaryKey(autoGenerate = true)
    public int cartId;

    public int userId; // liên kết với người dùng
}
