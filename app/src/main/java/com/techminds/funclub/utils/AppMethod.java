package com.techminds.funclub.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.techminds.funclub.entity.SelectedImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP 15 on 12-10-2016.
 */
public class AppMethod {

    public static List<SelectedImage> getImageList(Context context) {
        Log.i("getImageList","");
        ArrayList<SelectedImage> selectedImageList = new ArrayList<>();
        selectedImageList.clear();
        Gson gson = new Gson();
        if (UserPrefs.getSelectedImage(context).length() > 0) {
            try {
                String data = UserPrefs.getSelectedImage(context);
                Object json = new JSONTokener(data).nextValue();
                if (json instanceof JSONObject) {
                    JSONObject jsonObject = new JSONObject(data);
                    SelectedImage selectedImage1 = gson.fromJson(jsonObject.toString(), SelectedImage.class);
                    selectedImageList.add(selectedImage1);
                } else if (json instanceof JSONArray) {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        SelectedImage selectedImage1 = gson.fromJson(jsonArray.getJSONObject(i).toString(), SelectedImage.class);
                        selectedImageList.add(selectedImage1);

                        Log.i("getImageList", selectedImage1.toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {

        }

        return selectedImageList;
    }

    public static ArrayList<String> getSharedImageList(Context context) {
        ArrayList<String> selectedSharedImageList = new ArrayList<>();
        selectedSharedImageList.clear();
        Gson gson = new Gson();
        if (UserPrefs.getSharedImage(context).length() > 0) {
            try {
                String data = UserPrefs.getSharedImage(context);
                Object json = new JSONTokener(data).nextValue();
                if (json instanceof JSONObject) {
                    Log.e(Constants.TAG, "Single image uri:::> "+data);
                    selectedSharedImageList.add(data);
                } else if (json instanceof JSONArray) {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.e(Constants.TAG, "Image uri:::> index is "+i+" URI is::> "+jsonArray.getString(i));
                        selectedSharedImageList.add(jsonArray.getString(i));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return selectedSharedImageList;
    }
}
