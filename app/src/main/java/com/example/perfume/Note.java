package com.example.perfume;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notes")
public class Note  implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String category;
    private String notes;
    private String imageUrl;
    private String description;

    // Constructor, getter, setter
    public Note(String category, String notes, String imageUrl, String description) {
        this.category = category;
        this.notes = notes;
        this.imageUrl = imageUrl;
        this.description=description;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter và Setter
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description){ this.description=description;}
    public String getDescription() {return description;}
}
