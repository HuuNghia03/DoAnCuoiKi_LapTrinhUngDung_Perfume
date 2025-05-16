package com.example.perfume.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.perfume.UserDatabase;
import com.example.perfume.entities.UserEntity;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertUser(UserEntity user);

    // Truy vấn userId bằng cách sử dụng email
    @Query("SELECT id FROM users WHERE email = :email LIMIT 1")
    int getUserIdByEmail(String email);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    UserEntity checkUser(String email, String password);

    @Query("SELECT password FROM users WHERE email = :email LIMIT 1")
    String getPasswordByEmail(String email);

    @Query("SELECT categoryList FROM users WHERE id = :id LIMIT 1")
    String getOlfactiveById(int id);

    @Query("SELECT * FROM users WHERE id = :userId")
    UserEntity getUserById(int userId);

    @Delete
    void deleteUser(UserEntity user);

    @Query("SELECT firstname, lastname FROM users WHERE id = :userId")
    UserDatabase getUserNameById(int userId);


    @Query("UPDATE users SET isFirstTime= :isFirstTime, gender = :gender, age = :age, perfumePurpose=:perfumePurpose,categoryList = :categoryList, favoriteSeason = :favoriteSeason, " +
            "purposeUsage = :purposeUsage, requiresUniqueScent = :requiresUniqueScent, requiresLongLasting = :requiresLongLasting, " +
            "pricePer50ml = :pricePer50ml, favoritePerfume = :favoritePerfume WHERE id = :id")
    void updateUserPreferences(int id, boolean isFirstTime, String gender, Integer age,String perfumePurpose, String categoryList, String favoriteSeason,
                               String purposeUsage, boolean requiresUniqueScent, boolean requiresLongLasting,
                               float pricePer50ml, String favoritePerfume);

    @Query("UPDATE users SET firstname= :firstname, gender = :gender, age = :age, lastname = :lastname WHERE id = :id")
    void updateUserProfile(int id, String firstname, String lastname, String gender, int age);

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    LiveData<UserEntity> getUserByIdLive(int userId);
}
