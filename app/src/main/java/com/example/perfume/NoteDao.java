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
    @Query("SELECT * FROM notes WHERE category = :category")
    List<com.example.perfume.Note> getNotesByCategory(String category);

    @Query("SELECT * FROM notes WHERE notes LIKE '%' || :note || '%'")
    com.example.perfume.Note findCategoryByNote(String note);

}

