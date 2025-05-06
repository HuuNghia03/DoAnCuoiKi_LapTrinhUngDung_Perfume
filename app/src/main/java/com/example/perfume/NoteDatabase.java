package com.example.perfume;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {com.example.perfume.Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract com.example.perfume.NoteDao noteDao();
}
