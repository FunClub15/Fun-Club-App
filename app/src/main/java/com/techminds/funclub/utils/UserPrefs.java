package com.techminds.funclub.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;
import com.techminds.funclub.CustomMethods.JsonClass;
import com.techminds.funclub.entity.SelectedImage;
import com.techminds.funclub.utils.config.SharedPrefSocialIcons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class UserPrefs {

    public static SharedPreferences sp;

    private static void init(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getUser(Context con) {
        init(con);
        return sp.getString("user", "");
    }

    public static void setUser(String user, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("user", user);
        edit.commit();
    }

    public static String getStateId(Context con) {
        init(con);
        return sp.getString("stateId", "");
    }

    public static void setStateId(String stateId, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("stateId", stateId);
        edit.commit();
    }

    public static String getState(Context con) {
        init(con);
        return sp.getString("state", "");
    }

    public static void setState(String state, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("state", state);
        edit.commit();
    }

    public static String getCityId(Context con) {
        init(con);
        return sp.getString("cityId", "");
    }

    public static void setCityId(String cityId, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("cityId", cityId);
        edit.commit();
    }

    public static String getCity(Context con) {
        init(con);
        return sp.getString("city", "");
    }

    public static void setCity(String city, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("city", city);
        edit.commit();
    }

    public static String getTehshilId(Context con) {
        init(con);
        return sp.getString("tehshilId", "");
    }

    public static void setTehshilId(String tehshilId, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("tehshilId", tehshilId);
        edit.commit();
    }

    public static String getTehshil(Context con) {
        init(con);
        return sp.getString("tehshil", "");
    }

    public static void setTehshil(String tehshil, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("tehshil", tehshil);
        edit.commit();
    }

    public static String getDeviceToken(Context con) {
        init(con);
        return sp.getString("deviceToken", "");
    }

    public static void setDeviceToken(String longStr, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("deviceToken", longStr);
        edit.commit();
    }

    public static boolean isLogined(Context con) {
        init(con);
        return sp.getBoolean("isLogined", false);
    }

    public static void setLogin(boolean isLogined, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putBoolean("isLogined", isLogined);
        edit.commit();
    }

    public static boolean isLocationUpdated(Context con) {
        init(con);
        return sp.getBoolean("locationUpdated", false);
    }

    public static void setLocation(boolean locationUpdated, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putBoolean("locationUpdated", locationUpdated);
        edit.commit();
    }

    public static String getToCityId(Context con) {
        init(con);
        return sp.getString("toCityId", "");
    }

    public static void setToCityId(String toCityId, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("toCityId", toCityId);
        edit.commit();
    }

    public static String getToCity(Context con) {
        init(con);
        return sp.getString("toCity", "");
    }

    public static void setToCity(String toCity, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("toCity", toCity);
        edit.commit();
    }

    public static String getFromCityId(Context con) {
        init(con);
        return sp.getString("fromCityId", "");
    }

    public static void setFromCityId(String FromCityId, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("fromCityId", FromCityId);
        edit.commit();
    }

    public static String getFromCity(Context con) {
        init(con);
        return sp.getString("fromCity", "");
    }

    public static void setFromCity(String fromCity, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("fromCity", fromCity);
        edit.commit();
    }

    public static String getBackgroundColor(Context con) {
        init(con);
        return sp.getString("backgroundColor", null);
    }

    public static void setBackgroundColor(String backgroundColor, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("backgroundColor", backgroundColor);
        edit.commit();
    }

    public static String getSelectedImage(Context con) {
        init(con);

        Log.i("setSelectedImage()", sp.getString("selectedImage", ""));
        if(!result.equals("[]")) {
            getStickers();
            return result;
        }
        return sp.getString("selectedImage", "");
    }

    public static void setSelectedImage(String selectedImage, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("selectedImage", selectedImage);
        edit.commit();

//        sendStickers();

    }

    public static void setSelectedImageArray(String selectedImage, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("selectedImage", selectedImage);
        edit.commit();
    }


    public static String getSelectedPhotoGallery(Context con) {
        init(con);
        return sp.getString("selectedPhotoGallery", "");
    }

    public static void setSelectedPhotoGallery(String selectedPhotoGallery, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("selectedPhotoGallery", selectedPhotoGallery);
        edit.commit();
    }

    public static String getProfileImage(Context con) {
        init(con);
        return sp.getString("profileimage", "");
    }

    public static void setProfileImage(String profileimage, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("profileimage", profileimage);
        edit.commit();
    }

    public static String getPersonName(Context con) {
        init(con);
        return sp.getString("personname", "");
    }

    public static void setPersonName(String personname, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("personname", personname);
        edit.commit();
    }

    public static String getSharedImage(Context con) {
        init(con);
        return sp.getString("sharedimage", "");
    }

    public static void setSharedImage(String sharedimage, Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("sharedimage", sharedimage);
        edit.commit();
    }

    public static void resetPrefs(Context con) {
        init(con);
        Editor edit = sp.edit();
        edit.putString("user", "");
        edit.commit();
    }


    private static void sendStickers(){
        String member_id, picture, xaxis, yaxis, width, height, angle;
        member_id = Constants.member_id;

            for (int i = 0; i < Constants.selectedImages.size(); i++) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Constants.selectedImages.get(i).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                picture = encoded;
                xaxis = Constants.selectedImages.get(i).getPostitionX();
                yaxis = Constants.selectedImages.get(i).getPostitionY();
                width = Constants.selectedImages.get(i).getWidth();
                height = Constants.selectedImages.get(i).getHight();
                angle = Constants.selectedImages.get(i).getAngel();

                sendStickersParams(member_id, picture, xaxis, yaxis, width, height, angle);
            }

        return;

    }

    static String jsonResult;
    static String SEND_STICKERS_URL = "http://dev.technology-minds.com/funclub/manage/webservices/dashboard_insertnew.php";

    private static void sendStickersParams(final String member_id, final String picture, final String xaxis, final String yaxis,
                                           final String width, final String height, final String angle){

        class sendStickers extends AsyncTask<String,Void,String> {
//            ProgressDialog loading;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(context, "Loading Dashboard Content", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                jsonResult = s;
                Log.i("JSONRESPONSE", jsonResult);

//                loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("member_id", params[0]);
                data.put("picture", params[1]);
                data.put("xaxis", params[2]);
                data.put("yaxis", params[3]);
                data.put("width", params[4]);
                data.put("height", params[5]);
                data.put("angle", params[6]);

                String result = ruc.sendPostRequest(SEND_STICKERS_URL,data);

                return result;
            }
        }

        sendStickers ulc = new sendStickers();
        ulc.execute(member_id);
    }

    static String GET_STICKERS_URL = "http://dev.technology-minds.com/funclub/manage/webservices/dashboard_imagesnew.php";

    static String result= "[]";

    public static void getStickers() {

        class sendStickers extends AsyncTask<String,Void,String> {
//            ProgressDialog loading;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(context, "Loading Dashboard Content", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                jsonResult = s;
                Log.i("JSONRESPONSE", jsonResult);

                JSONObject obj = null;
                try {
                    obj = new JSONObject(s);
                    result = String.valueOf(obj.get("data"));
                    result = s;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("member_id", params[0]);

                String result = ruc.sendPostRequest(GET_STICKERS_URL, data);

                return result;
            }
        }

        sendStickers ulc = new sendStickers();
        ulc.execute(Constants.member_id);
    }

}
