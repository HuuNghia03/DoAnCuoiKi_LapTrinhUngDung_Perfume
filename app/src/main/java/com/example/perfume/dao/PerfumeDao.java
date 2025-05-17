
package com.example.perfume.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.perfume.entity.PerfumeEntity;

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

    @Query("SELECT * FROM perfumes WHERE topNoteId= :noteId")
    List<PerfumeEntity> getPerfumesByTopNoteId(int noteId);

    @Query("SELECT * FROM perfumes WHERE heartNoteId= :noteId")
    List<PerfumeEntity> getPerfumesByHeartNoteId(int noteId);

    @Query("SELECT * FROM perfumes WHERE baseNoteId= :noteId")
    List<PerfumeEntity> getPerfumesByBaseNoteId(int noteId);

    @Query("SELECT * FROM perfumes WHERE name = :name LIMIT 1")
    PerfumeEntity getPerfumesByName(String name);




}
