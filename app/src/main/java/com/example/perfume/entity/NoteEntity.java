package com.example.perfume.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notes")
public class NoteEntity implements Serializable {
        @PrimaryKey(autoGenerate = true)
        private int id;

        private String note;
        private String imageUrl;
        private String description;

    // Constructor, getter, setter
    public NoteEntity(String note, String imageUrl, String description) {

        this.note = note;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter và Setter


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
