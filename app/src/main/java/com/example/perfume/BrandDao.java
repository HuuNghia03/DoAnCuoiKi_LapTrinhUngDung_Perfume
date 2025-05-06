package com.example.perfume;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.perfume.BrandEntity;

import java.util.List;

@Dao
public interface BrandDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBrand(BrandEntity brand);

    @Query("SELECT * FROM brand")
    List<BrandEntity> getAllBrands();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBrandList(List<BrandEntity> brands);

    @Query("SELECT * FROM brand WHERE id = :brandId")
    BrandEntity getBrandById(String brandId);

    @Query("DELETE FROM brand")
    void deleteAll();
    @Query("SELECT * FROM brand WHERE logo IS NOT NULL AND logo != ''")
    List<BrandEntity> getAllBrandsWithImage();

}
