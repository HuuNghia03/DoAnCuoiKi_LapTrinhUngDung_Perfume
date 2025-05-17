package com.example.perfume;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.perfume.entity.AddressEntity;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddressStorage {
    private static final String PREF_NAME = "address_prefs";

    public static void saveAddresses(Context context, String userKey, List<AddressEntity> addresses) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(addresses);
        editor.putString(userKey, json);
        editor.apply();
    }

    public static List<AddressEntity> getAddresses(Context context, String userKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(userKey, null);
        if (json != null) {
            Type type = new TypeToken<List<AddressEntity>>() {}.getType();
            return new Gson().fromJson(json, type);
        }
        return new ArrayList<>();
    }
}

