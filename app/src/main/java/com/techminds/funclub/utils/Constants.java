package com.techminds.funclub.utils;

import android.util.Log;

import com.techminds.funclub.entity.SelectedImage;
import com.techminds.funclub.entity.Urls;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by HP 15 on 18-03-2016.
 */
public class Constants {


    public static String TAG = "Activity";
    public static ArrayList ImageSources = new ArrayList<>();
    public static ArrayList<SelectedImage> selectedImages = new ArrayList<>();
    public static ArrayList<SelectedImage> dashBoardStickers = new ArrayList<>();
    public static String currentRoom;
    public static String member_id;

    public static ArrayList<Urls> urls = new ArrayList<>();
    public static ArrayList<Urls> urls2 = new ArrayList<>();

    public static ArrayList<Urls> headboardsList = new ArrayList<>();

    public static ArrayList<String> getUrls() {
        Log.i("getUrls()", currentRoom);
        ArrayList<String> urlArrayList = new ArrayList<>();

        for(int i=0; i<urls.size(); i++) {
            Log.i("getUrl()", " " + urls.get(i).getType() + " " + Constants.currentRoom);

            if(urls.get(i).getType().equals(Constants.currentRoom)) {
//                Log.i("getUrls()", " "+currentRoom+" "+urls.get(i).getUrl());
                urlArrayList.add(urls.get(i).getUrl());
            }
        }

        return urlArrayList;
    }

    public static boolean contains(Urls urlObj) {
        for(int i=0; i<urls.size(); i++) {
            if(urls.get(i).equals(urlObj))
                return true;
        }
        return false;
    }

    public static void updateUrls() {
//        Log.i("urlsLog","urlsSize()"+urls.size());
//        Log.i("urlsLog","urls2Size()"+urls2.size());

        for(int i=0; i<urls2.size(); i++) {
            if(!urlsContains(urls2.get(i))) {
//                Log.i("urlsLog","urls !contains" + urls2.get(i).toString());
                urls.add(urls2.get(i));
            }
        }
    }

    private static boolean urlsContains(Urls urls2Obj) {
        for(int i=0; i<urls.size(); i++) {
            if(urls.get(i).equals(urls2Obj)) {
//                Log.i("urlsLog","urls contains"+urls.get(i).toString());
//                Log.i("urlsLog","urls2 contains"+urls2Obj.toString());
//                Log.i("urlsLog","returning true");
                return true;
            }
        }

        Log.i("urlsLog", "urls !contains"+urls2Obj.toString());
        return false;
    }


    public static void removeDashboardImage(String id) {
        for (int i=0; i<dashBoardStickers.size(); i++) {
            if(dashBoardStickers.get(i).getImageid().equals(id)) {
                Log.i("removingSticker()",dashBoardStickers.get(i).getImageid());
                dashBoardStickers.remove(i);
                break;
            }
        }
    }

}
