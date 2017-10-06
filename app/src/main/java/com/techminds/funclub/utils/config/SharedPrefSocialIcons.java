package com.techminds.funclub.utils.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.techminds.funclub.ui.activity.LoginActivity;

/**
 * Created by Malik on 7/31/2017.
 */

public class SharedPrefSocialIcons {

    public static final String MY_PREFS_NAME = "MyPrefsSocial_Fb_Gmail_File";

     public static SharedPreferences sharedPreferences;


    public SharedPrefSocialIcons(Context context) {
        sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
    }


    public static String getStrings(Context mContext, String key){

           SharedPreferences pref = mContext.getSharedPreferences(MY_PREFS_NAME,Activity.MODE_PRIVATE);

        return pref.getString(key, null);
    }

    public static void putString(Context mContext, String key, String value ){
        try{

            SharedPreferences pref= mContext.getSharedPreferences(MY_PREFS_NAME, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key, value);
            editor.commit();
        }catch (NullPointerException e ){
            e.printStackTrace();
        }

    }

    private static void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public void set_USERID(String userID,Context context){

        init(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USERID", userID);
        editor.commit();
    }

    public void set_FB_GMAIL_EMAIL(String email,Context context){
        init(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FB_GMAIL_EMAIL",email);
        editor.commit();
    }

    public void set_FB_GMAIL_USERNAME(String username,Context context){
        init(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FB_GMAIL_USERNAME",username);
        editor.commit();
    }

    public void set_FB_GMAIL_PHOTO(String photo,Context context){
        init(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FB_GMAIL_PHOTO",photo);
        editor.commit();
    }

    public void set_FB_GMAIL_GENDER(String gender,Context context){
        init(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FB_GMAIL_GENDER",gender);
        editor.commit();
    }

    public String get_USERID(Context context){
        init(context);
       String userId = sharedPreferences.getString("USERID","");
        return  userId;

    }

    public String get_FB_GMAIL_EMAIL(Context context){
        init(context);
        String email = sharedPreferences.getString("FB_GMAIL_EMAIL","");
        return email;

    }

    public String get_FB_GMAIL_USERNAME(Context context){
        init(context);
        String username = sharedPreferences.getString("FB_GMAIL_USERNAME","");
        return username;

    }

    public String get_FB_GMAIL_PHOTO(Context context){
        init(context);
        String photo = sharedPreferences.getString("FB_GMAIL_PHOTO","");
        return  photo;

    }

    public String get_FB_GMAIL_GENDER(Context context){
        init(context);
        String gender = sharedPreferences.getString("FB_GMAIL_GENDER","");
        return  gender;
    }

    public static void clear(Context mContext){
        SharedPreferences pref= mContext.getSharedPreferences(MY_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }


    public static void clearStickersPreference(Context mContext){

        SharedPreferences pref= mContext.getSharedPreferences(MY_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("dashboardStickers"); //we are removing prodId by key
        editor.commit();
    }




}
