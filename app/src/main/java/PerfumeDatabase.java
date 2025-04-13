package com.example.perfume;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {com.example.perfume.PerfumeEntity.class,com.example.perfume.Note.class}, version = 2, exportSchema = false)
public abstract class PerfumeDatabase extends RoomDatabase {

    private static PerfumeDatabase instance;

    public abstract com.example.perfume.PerfumeDao perfumeDao();
    public abstract com.example.perfume.NoteDao noteDao();

    public static synchronized PerfumeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            PerfumeDatabase.class, "perfume_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
