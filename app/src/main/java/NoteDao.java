package com.example.perfume;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao

public interface NoteDao {
    @Insert
    void insertList(List<com.example.perfume.Note> notes);

    @Query("SELECT * FROM notes")
    List<com.example.perfume.Note> getAllNotes();
}

