package com.techminds.funclub.utils.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mask on 8/19/17.
 */

public class SelectedImagesPref {
    public static final String MY_PREFS_NAME = "SelectedImagesPref";

    public static SharedPreferences sharedPreferences;

//    public InteriorDesignPref(Context context) {
//        sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
//    }

    public static String getStrings(Context mContext, String key) {
        SharedPreferences pref = mContext.getSharedPreferences(MY_PREFS_NAME, Activity.MODE_PRIVATE);
        return pref.getString(key, null);
    }

    public static void putString(Context mContext, String key, String value ) throws JSONException {
        SharedPreferences pref = mContext.getSharedPreferences(MY_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String selectedImagesArray = getStrings(mContext, "stickers");

        JSONObject obj = new JSONObject(value);

        JSONArray jr = new JSONArray(selectedImagesArray);
        jr.put(obj);

        Gson gson = new Gson();
        value = gson.toJson(jr);

        Log.i("dashboardSticker", value);

        editor.putString(key, value);
        editor.commit();
    }
}
