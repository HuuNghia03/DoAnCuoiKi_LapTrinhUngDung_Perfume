package com.example.perfume;

public class BrandWithImage {
    private String brand;
    private String brandImg;

    // Constructor
    public BrandWithImage(String brand, String brandImg) {
        this.brand = brand;
        this.brandImg = brandImg;
    }

    // Getter và Setter
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrandImg() {
        return brandImg;
    }

    public void setBrandImg(String brandImg) {
        this.brandImg = brandImg;
    }
}
