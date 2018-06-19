package com.theatro.zipgo.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theatro.zipgo.POJO.PlaceList;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PrefUtils {

    private static final String PREF_FILE_NAME = "zipgo";
    private static String PLACE_LIST = "place_list";

    public synchronized static void savePlaceList(ArrayList<PlaceList> featureList, @Nullable Context context) {

        SharedPreferences preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefrencesEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(featureList);
        prefrencesEditor.putString(PLACE_LIST, json);
        prefrencesEditor.commit();
    }

    public static synchronized ArrayList<PlaceList> getPlaceList(@Nullable Context context) {

        SharedPreferences preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = preferences.getString(PLACE_LIST, null);
        Type type = new TypeToken<ArrayList<PlaceList>>() {
        }.getType();
        ArrayList<PlaceList> arrayList = gson.fromJson(json, type);
        if (arrayList == null) return new ArrayList();
        return arrayList;
    }
}
