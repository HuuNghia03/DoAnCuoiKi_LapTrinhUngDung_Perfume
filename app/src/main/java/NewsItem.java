package com.example.perfume;

public class NewsItem {
    private String title;
    private String description;
    private String uploadDate;
    private int imageResId;
    private int logoResId;

    public NewsItem(String title, String description, String uploadDate, int imageResId, int logoResId) {
        this.title = title;
        this.description = description;
        this.uploadDate = uploadDate;
        this.imageResId = imageResId;
        this.logoResId = logoResId;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getUploadDate() { return uploadDate; }
    public int getImageResId() { return imageResId; }
    public int getLogoResId() { return logoResId; }
}
