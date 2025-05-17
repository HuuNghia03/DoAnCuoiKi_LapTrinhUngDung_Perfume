package com.example.perfume.entity;

public class ScentEntity {
    private String title;
    private String description;
    private int imageResId;
    private String note;

    public ScentEntity(String title, String description, int imageResId, String note) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.note = note;
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

    public String getNote() {
        return note;
    }
}
