package com.example.perfume;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NewsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NewsData.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "news";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_DATE = "upload_date";
    private static final String COL_IMAGE_RES_ID = "image_res_id";
    private static final String COL_LOGO_RES_ID = "logo_res_id";

    public NewsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_IMAGE_RES_ID + " INTEGER, " +
                COL_LOGO_RES_ID + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Thêm bài viết mới
    public boolean insertNewsItem(com.example.perfume.NewsItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, item.getTitle());
        values.put(COL_DESCRIPTION, item.getDescription());
        values.put(COL_DATE, item.getUploadDate());
        values.put(COL_IMAGE_RES_ID, item.getImageResId());
        values.put(COL_LOGO_RES_ID, item.getLogoResId());

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    // Lấy danh sách tất cả bài viết
    public List<com.example.perfume.NewsItem> getAllNews() {
        List<com.example.perfume.NewsItem> newsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMAGE_RES_ID));
                int logoResId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LOGO_RES_ID));

                com.example.perfume.NewsItem item = new com.example.perfume.NewsItem(title, description, date, imageResId, logoResId);
                newsList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return newsList;
    }

    // Xóa tất cả bài viết (tùy chọn)
    public void deleteAllNews() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
