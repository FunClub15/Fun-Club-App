package com.techminds.funclub.utils.config;

/**
 * Created by mask on 8/13/17.
 */


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class InteriorDesignPref {
    public static final String MY_PREFS_NAME = "InteriorDesignPref";

    public static SharedPreferences sharedPreferences;

//    public InteriorDesignPref(Context context) {
//        sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
//    }

    public static String getStrings(Context mContext, String key){
        SharedPreferences pref = mContext.getSharedPreferences(MY_PREFS_NAME,Activity.MODE_PRIVATE);
        return pref.getString(key, null);
    }

    public static void putString(Context mContext, String key, String value ){
        SharedPreferences pref= mContext.getSharedPreferences(MY_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
