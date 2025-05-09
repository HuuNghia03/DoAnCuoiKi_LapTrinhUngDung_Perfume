package com.example.perfume;
import androidx.annotation.NonNull;
import androidx.room.Entity;

import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "brand")
public class BrandEntity implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;

    private String name;
    private String banner;
    private String logo;
    private String perfumes;
    private String founded;
    private String founder;
    private String country;
    private String notablePerfumes;
    private String segment;
    private String popularity;
    private String style;
    private String link;
    private String description;

    // Getter & Setter
    public BrandEntity(){}

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPerfumes() {
        return perfumes;
    }

    public void setPerfumes(String perfumes) {
        this.perfumes = perfumes;
    }

    public String getFounded() {
        return founded;
    }

    public void setFounded(String founded) {
        this.founded = founded;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNotablePerfumes() {
        return notablePerfumes;
    }

    public void setNotablePerfumes(String notablePerfumes) {
        this.notablePerfumes = notablePerfumes;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
