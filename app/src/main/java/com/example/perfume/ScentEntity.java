package com.example.perfume;

public class ScentEntity {
    private String title;
    private String description;
    private int imageResId;
    private String category;

    public ScentEntity(String title, String description, int imageResId, String category) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getCategory() {
        return category;
    }
}
