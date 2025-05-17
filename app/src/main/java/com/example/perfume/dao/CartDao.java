package com.example.perfume.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.perfume.entity.CartItemEntity;
import com.example.perfume.relation.CartWithItems;
import com.example.perfume.entity.CartEntity;

import java.util.List;

@Dao
public interface CartDao {

    @Insert
    long insertCart(CartEntity cart);  // Thêm một giỏ hàng

    @Insert
    void insertCartItem(CartItemEntity item);  // Thêm một item vào giỏ

    @Query("DELETE FROM cart_item WHERE cartOwnerId = :cartId")
    void clearCartItems(int cartId);  // Xóa toàn bộ item của 1 giỏ hàng

    @Query("SELECT * FROM cart WHERE userId = :userId LIMIT 1")
    CartEntity getCartByUserId(int userId);  // Lấy giỏ hàng của 1 user

    @Query("SELECT * FROM cart_item WHERE cartOwnerId = :cartId AND perfumeId = :perfumeId AND volume = :volume LIMIT 1")
    CartItemEntity getExistingItem(int cartId, int perfumeId, int volume);  // Kiểm tra item đã có trong giỏ chưa

    @Update
    void updateCartItem(CartItemEntity item);  // Cập nhật số lượng nếu đã có item

    @Transaction
    @Query("SELECT * FROM cart WHERE userId = :userId")
    List<CartWithItems> getCartWithItemsByUser(int userId);  // Lấy giỏ hàng với các item của user

    @Transaction
    @Query("SELECT * FROM cart WHERE userId = :userId LIMIT 1")
    CartWithItems getCartWithItems(int userId);
    @Query("SELECT * FROM cart_item WHERE cartOwnerId = :cartId AND perfumeId = :perfumeId LIMIT 1")
    CartItemEntity getCartItemByPerfumeId(int cartId, int perfumeId);  // Lấy item theo perfumeId trong giỏ hàng


    @Delete
    void deleteCartItem(CartItemEntity cartItem);  // Cách này đúng khi bạn truyền cả đối tượng

    @Delete
    void deleteCart(CartEntity cart);


}
