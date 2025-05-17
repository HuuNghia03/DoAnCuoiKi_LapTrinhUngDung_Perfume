package com.example.perfume;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.perfume.dao.BrandDao;
import com.example.perfume.dao.CartDao;
import com.example.perfume.dao.NoteDao;
import com.example.perfume.dao.OrderDao;
import com.example.perfume.dao.PerfumeDao;
import com.example.perfume.dao.UserDao;
import com.example.perfume.entity.BrandEntity;
import com.example.perfume.entity.CartEntity;
import com.example.perfume.entity.CartItemEntity;
import com.example.perfume.entity.NoteEntity;
import com.example.perfume.entity.OrderEntity;
import com.example.perfume.entity.OrderItemEntity;
import com.example.perfume.entity.PerfumeEntity;
import com.example.perfume.entity.UserEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        PerfumeEntity.class,
        NoteEntity.class,
        BrandEntity.class,
        CartEntity.class,
        CartItemEntity.class,
        UserEntity.class,
        OrderEntity.class,
        OrderItemEntity.class
}, version = 8, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract PerfumeDao perfumeDao();
    public abstract NoteDao noteDao();
    public abstract BrandDao BrandDao();
    public abstract CartDao cartDao();
    public abstract UserDao userDao();
    public abstract OrderDao orderDao();
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "smellory_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }
}
