package com.example.perfume;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.perfume.dao.NoteDao;
import com.example.perfume.entities.NoteEntity;

@Database(entities = {NoteEntity.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}
