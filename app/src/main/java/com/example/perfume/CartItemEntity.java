package com.example.perfume;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "cart_item",
        foreignKeys = {
                @ForeignKey(
                        entity = CartEntity.class,
                        parentColumns = "cartId",
                        childColumns = "cartOwnerId",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = PerfumeEntity.class,
                        parentColumns = "id",
                        childColumns = "perfumeId",
                        onDelete = CASCADE
                )
        },
        indices = {@Index("cartOwnerId"), @Index("perfumeId")}
)
public class CartItemEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cartItemId")
    private int cartItemId; // ID của mỗi item trong giỏ hàng

    @ColumnInfo(name = "cartOwnerId")
    private int cartOwnerId; // Liên kết với CartEntity (cart)

    @ColumnInfo(name = "perfumeId")
    private int perfumeId; // Liên kết với PerfumeEntity (nước hoa)

    private int quantity; // Số lượng
    private int volume; // Dung tích (ml)
    private float pricePerVolume; // Giá theo mỗi đơn vị thể tích

    // Constructor
    public CartItemEntity(int cartOwnerId, int perfumeId, int quantity, int volume, float pricePerVolume) {
        this.cartOwnerId = cartOwnerId;
        this.perfumeId = perfumeId;
        this.quantity = quantity;
        this.volume = volume;
        this.pricePerVolume = pricePerVolume;
    }

    // Getter và Setter
    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartOwnerId() {
        return cartOwnerId;
    }

    public void setCartOwnerId(int cartOwnerId) {
        this.cartOwnerId = cartOwnerId;
    }

    public int getPerfumeId() {
        return perfumeId;
    }

    public void setPerfumeId(int perfumeId) {
        this.perfumeId = perfumeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public float getPricePerVolume() {
        return pricePerVolume;
    }

    public void setPricePerVolume(float pricePerVolume) {
        this.pricePerVolume = pricePerVolume;
    }
}
