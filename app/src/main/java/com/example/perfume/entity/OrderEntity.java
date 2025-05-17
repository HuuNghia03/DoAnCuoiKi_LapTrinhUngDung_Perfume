package com.example.perfume.entity;

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
    private int orderId;

    private int userId;
    private long orderDate;

    @Embedded
    private AddressEntity address;

    private float totalPrice;
    private float shipPrice;
    private String deliveryMethod;
    private String paymentMethod;
    // Getter và Setter
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public long getOrderDate() { return orderDate; }
    public void setOrderDate(long orderDate) { this.orderDate = orderDate; }

    public AddressEntity getAddress() { return address; }
    public void setAddress(AddressEntity address) { this.address = address; }

    public float getTotalPrice() { return totalPrice; }
    public void setTotalPrice(float totalPrice) { this.totalPrice = totalPrice; }

    public float getShipPrice() { return shipPrice; }
    public void setShipPrice(float shipPrice) { this.shipPrice = shipPrice; }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}

