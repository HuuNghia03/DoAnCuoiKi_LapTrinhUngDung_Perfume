package com.example.perfume.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "firstname")
    private String firstname;

    @ColumnInfo(name = "lastname")
    private String lastname;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;
    private boolean isFirstTime;
    private String gender;
    private Integer age;
    private String perfumePurpose;
    private String categoryList;

    private String favoriteSeason;
    private String purposeUsage;
    private boolean requiresUniqueScent; // yêu cầu mùi hương độc đáo
    private boolean requiresLongLasting; // yêu cầu lưu hương lâu
    private float pricePer50ml;
    public String favoritePerfume;



    public String getFavoritePerfume() {
        return favoritePerfume;
    }

    public void setFavoritePerfume(String favoritePerfume) {
        this.favoritePerfume = favoritePerfume;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }


    public void setAge(Integer age) {
        this.age = age;

    }

    public void setPerfumePurpose(String perfumePurpose) {
        this.perfumePurpose = perfumePurpose;
    }

    public String getPerfumePurpose() {
        return perfumePurpose;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(String categoryList) {
        this.categoryList = categoryList;
    }

    public boolean isRequiresLongLasting() {
        return requiresLongLasting;
    }

    public boolean isRequiresUniqueScent() {
        return requiresUniqueScent;
    }

    public float getPricePer50ml() {
        return pricePer50ml;
    }

    public String getFavoriteSeason() {
        return favoriteSeason;
    }

    public String getPurposeUsage() {
        return purposeUsage;
    }

    public void setFavoriteSeason(String favoriteSeason) {
        this.favoriteSeason = favoriteSeason;
    }

    public void setPricePer50ml(float pricePer50ml) {
        this.pricePer50ml = pricePer50ml;
    }

    public void setPurposeUsage(String purposeUsage) {
        this.purposeUsage = purposeUsage;
    }

    public void setRequiresLongLasting(boolean requiresLongLasting) {
        this.requiresLongLasting = requiresLongLasting;
    }

    public void setRequiresUniqueScent(boolean requiresUniqueScent) {
        this.requiresUniqueScent = requiresUniqueScent;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getEmail() {
        return email;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }
}
