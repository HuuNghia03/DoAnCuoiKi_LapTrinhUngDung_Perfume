package com.example.perfume;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.perfume.dao.UserDao;
import com.example.perfume.entity.UserEntity;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;
    private final Executor executor = Executors.newSingleThreadExecutor();


    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.userDao = db.userDao();
        this.executorService = Executors.newSingleThreadExecutor(); // Executor để chạy bất đồng bộ
    }

    // Chèn người dùng vào cơ sở dữ liệu
    public void insertUser(String fname, String lname, String email, String password, boolean isFirstTime, InsertUserCallback callback) {
        executorService.execute(() -> {
            UserEntity user = new UserEntity();
            user.setFirstname(fname);
            user.setLastname(lname);
            user.setEmail(email);
            user.setPassword(password);
            user.setFirstTime(isFirstTime);
            long result = userDao.insertUser(user);
            // Gọi callback khi xong
            callback.onResult(result != -1);
        });
    }

    // Kiểm tra người dùng tồn tại hay không
    public void checkUser(String email, String password, CheckUserCallback callback) {
        executorService.execute(() -> {
            UserEntity user = userDao.checkUser(email, password);
            // Gọi callback với kết quả tìm được
            callback.onResult(user != null);
        });
    }

    // Lấy mật khẩu từ email
    public void getPasswordByEmail(String email, GetPasswordCallback callback) {
        executorService.execute(() -> {
            String password = userDao.getPasswordByEmail(email);
            // Gọi callback khi xong
            callback.onResult(password);
        });
    }

    public void getUserIdByEmail(String email, Callback callback) {
        executor.execute(() -> {
            int userId = userDao.getUserIdByEmail(email);
            new Handler(Looper.getMainLooper()).post(() -> {
                callback.onResult(userId);
            });
        });
    }

    public void updateUserPreferences(int userId, boolean isFirstTime, String gender, Integer age, String perfumePurpose, String categoryList,
                                      String favoriteSeason, String purposeUsage,
                                      boolean requiresUniqueScent, boolean requiresLongLasting,
                                      float pricePer50ml, String favoritePerfume) {
        executorService.execute(() -> {
            userDao.updateUserPreferences(userId, isFirstTime, gender, age, perfumePurpose, categoryList, favoriteSeason,
                    purposeUsage, requiresUniqueScent, requiresLongLasting,
                    pricePer50ml, favoritePerfume);
        });
    }

    public void updateUserProfile(int userId, String firstname, String lastname, String gender, int age) {
        executorService.execute(() -> {
            userDao.updateUserProfile(userId, firstname, lastname, gender, age);
        });
    }

    public void getOlfactiveByUserId(int userId, GetOlfactiveCallback callback) {
        executorService.execute(() -> {
            String categoryStr = userDao.getOlfactiveById(userId);
            new Handler(Looper.getMainLooper()).post(() -> {
                callback.onResult(categoryStr);
            });
        });
    }

    public LiveData<UserEntity> getUserByIdLive(int userId) {
        return userDao.getUserByIdLive(userId);
    }

    public interface Callback {
        void onResult(int userId);
    }

    // Interface callback để nhận kết quả insert
    public interface InsertUserCallback {
        void onResult(boolean isSuccess);
    }

    // Interface callback để nhận kết quả kiểm tra người dùng
    public interface CheckUserCallback {
        void onResult(boolean isValid);
    }

    // Interface callback để nhận kết quả lấy mật khẩu
    public interface GetPasswordCallback {
        void onResult(String password);
    }

    public interface GetOlfactiveCallback {
        void onResult(String olfactiveCategory);
    }

}
