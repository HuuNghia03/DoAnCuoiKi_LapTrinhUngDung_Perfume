package com.example.perfume;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {
        com.example.perfume.PerfumeEntity.class,
        com.example.perfume.Note.class,
        com.example.perfume.BrandEntity.class
}, version = 3, exportSchema = false)

public abstract class PerfumeDatabase extends RoomDatabase {

    private static PerfumeDatabase instance;

    public abstract com.example.perfume.PerfumeDao perfumeDao();
    public abstract com.example.perfume.NoteDao noteDao();
    public abstract com.example.perfume.BrandDao BrandDao();

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
