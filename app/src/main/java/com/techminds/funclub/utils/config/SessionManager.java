package com.techminds.funclub.utils.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techminds.funclub.model.bean.UserModel;

/**
 * Created by alkesh.chimnani on 2/8/2017.
 */

public class SessionManager {


    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private final static String KEY_CACHE_LOCATION = "key_cache_location";

    private SharedPreferences sharedPrefs;
    private String prefsKey = "smartShopAppSharedPrefs";
    private SharedPreferences.Editor editor;


    public SessionManager(Context context) {
        sharedPrefs = context.getSharedPreferences(prefsKey, context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
    }

    public void setCacheLocation(int cacheLocation) {
        sharedPrefs.edit().putInt(KEY_CACHE_LOCATION, cacheLocation).commit();
    }

    public int getCacheLocation() {
        return sharedPrefs.getInt(KEY_CACHE_LOCATION, 0);
    }

    public void setUserID(String userID){
        editor.putString("USER_ID", userID);
        editor.commit();
    }

    public String getUserID(){
        return sharedPrefs.getString("USER_ID", null);
    }

    public void setUserObject(UserModel userModel){
        Gson gson = new GsonBuilder().create();
        String userModelStr = gson.toJson(userModel);

        editor.putString("USER_OBJECT", userModelStr);
        editor.commit();
    }



    public UserModel getUserObject(){
        UserModel model=null;
        String userModelStr = sharedPrefs.getString("USER_OBJECT", null);

        if (userModelStr != null) {
            Gson gson = new GsonBuilder().create();
            model = gson.fromJson(userModelStr, UserModel.class);
        }
        return model;
    }

    public void clearUserInfo(){
        editor.putString("USER_OBJECT", null);
        editor.commit();
    }


}
