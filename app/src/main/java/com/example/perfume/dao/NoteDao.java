package com.example.perfume.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.perfume.entity.NoteEntity;

import java.util.List;

@Dao

public interface NoteDao {
    @Insert
    void insertList(List<NoteEntity> notes);

    @Query("SELECT * FROM notes")
    List<NoteEntity> getAllNotes();


    @Query("SELECT * FROM notes WHERE id = :noteId")
    NoteEntity findNoteByNoteId(int noteId);


}

