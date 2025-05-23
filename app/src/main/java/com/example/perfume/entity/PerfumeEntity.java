package com.example.perfume.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "perfumes")
public class PerfumeEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String brand;
    private String name;
    private String img;
    private String imgs;
    private String volumes;
    private Integer year;
    private Float rating;
    private Float longevity;
    private String concentration;
    private String brandImg;
    private String designers;
    private int topNoteId;
    private int heartNoteId;
    private int baseNoteId;
    private String description, olfactory;
    private Float price;
    private String prices;

    // 🔧 Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVolumes(String volumes) {
        this.volumes = volumes;
    }

    public String getVolumes() {
        return volumes;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getPrices() {
        return prices;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public String getConcentration() {
        return concentration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOlfactory() {
        return olfactory;
    }

    public void setOlfactory(String olfactory) {
        this.olfactory = olfactory;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }


    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }


    public Float getLongevity() {
        return longevity;
    }

    public void setLongevity(Float longevity) {
        this.longevity = longevity;
    }


    public String getBrandImg() {
        return brandImg;
    }

    public void setBrandImg(String brandImg) {
        this.brandImg = brandImg;
    }


    public String getDesigners() {
        return designers;
    }

    public void setDesigners(String designers) {
        this.designers = designers;
    }


    public int getBaseNoteId() {
        return baseNoteId;
    }

    public int getHeartNoteId() {
        return heartNoteId;
    }

    public int getTopNoteId() {
        return topNoteId;
    }

    public void setBaseNoteId(int baseNoteId) {
        this.baseNoteId = baseNoteId;
    }

    public void setHeartNoteId(int heartNoteId) {
        this.heartNoteId = heartNoteId;
    }

    public void setTopNoteId(int topNoteId) {
        this.topNoteId = topNoteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PerfumeEntity that = (PerfumeEntity) o;

        // So sánh bằng tên
        return name != null && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

}
