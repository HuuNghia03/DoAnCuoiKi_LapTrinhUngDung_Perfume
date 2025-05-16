
package com.example.perfume.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.perfume.entities.PerfumeEntity;

import java.util.List;

@Dao
public interface PerfumeDao {

    // Thêm một danh sách nước hoa vào database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<PerfumeEntity> perfumes);

    // Xóa toàn bộ nước hoa trong database
    @Query("DELETE FROM perfumes")
    void deleteAll();

    // Truy vấn tất cả nước hoa trong database
    @Query("SELECT * FROM perfumes")
    List<PerfumeEntity> getAllPerfumes();
    // Lấy danh sách brand không trùng nhau, theo thứ tự ABC
//    @Query("SELECT DISTINCT brand, brandImg FROM perfumes ORDER BY brand ASC")
//    List<com.example.perfume.BrandWithImage> getAllBrandsWithImage();
    @Query("SELECT * FROM perfumes WHERE brand = :brand")
    List<PerfumeEntity> getPerfumesByBrand(String brand);

    @Query("SELECT * FROM perfumes WHERE `top` LIKE '%' || :note || '%'")
    List<PerfumeEntity> getPerfumesByTopNote(String note);

    @Query("SELECT * FROM perfumes WHERE `heart` LIKE '%' || :note || '%'")
    List<PerfumeEntity> getPerfumesByHeartNote(String note);

    @Query("SELECT * FROM perfumes WHERE `base` LIKE '%' || :note || '%'")
    List<PerfumeEntity> getPerfumesByBaseNote(String note);

    @Query("SELECT * FROM perfumes WHERE name = :name LIMIT 1")
    PerfumeEntity getPerfumesByName(String name);




}
