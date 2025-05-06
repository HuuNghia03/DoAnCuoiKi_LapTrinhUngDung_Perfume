
package com.example.perfume;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PerfumeDao {

    // Thêm một danh sách nước hoa vào database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<com.example.perfume.PerfumeEntity> perfumes);

    // Xóa toàn bộ nước hoa trong database
    @Query("DELETE FROM perfumes")
    void deleteAll();

    // Truy vấn tất cả nước hoa trong database
    @Query("SELECT * FROM perfumes")
    List<com.example.perfume.PerfumeEntity> getAllPerfumes();
    // Lấy danh sách brand không trùng nhau, theo thứ tự ABC
//    @Query("SELECT DISTINCT brand, brandImg FROM perfumes ORDER BY brand ASC")
//    List<com.example.perfume.BrandWithImage> getAllBrandsWithImage();
    @Query("SELECT * FROM perfumes WHERE brand = :brand")
    List<com.example.perfume.PerfumeEntity> getPerfumesByBrand(String brand);

    @Query("SELECT * FROM perfumes WHERE `top` LIKE '%' || :note || '%'")
    List<com.example.perfume.PerfumeEntity> getPerfumesByTopNote(String note);

    @Query("SELECT * FROM perfumes WHERE `heart` LIKE '%' || :note || '%'")
    List<com.example.perfume.PerfumeEntity> getPerfumesByHeartNote(String note);

    @Query("SELECT * FROM perfumes WHERE `base` LIKE '%' || :note || '%'")
    List<com.example.perfume.PerfumeEntity> getPerfumesByBaseNote(String note);




}
