package com.example.perfume;//package com.example.perfume;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.perfume.entities.UserEntity;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<UserEntity> userLiveData;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void setUserId(int userId) {
        userLiveData = repository.getUserByIdLive(userId);
    }

    public LiveData<UserEntity> getUserLiveData() {
        return userLiveData;
    }
}

