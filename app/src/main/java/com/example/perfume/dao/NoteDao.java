package com.example.perfume.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.perfume.entities.NoteEntity;

import java.util.List;

@Dao

public interface NoteDao {
    @Insert
    void insertList(List<NoteEntity> notes);

    @Query("SELECT * FROM notes")
    List<NoteEntity> getAllNotes();
    @Query("SELECT * FROM notes WHERE category = :category")
    List<NoteEntity> getNotesByCategory(String category);

    @Query("SELECT * FROM notes WHERE notes LIKE '%' || :note || '%'")
    NoteEntity findCategoryByNote(String note);

}

