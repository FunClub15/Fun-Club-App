package com.techminds.funclub.utils;

import android.util.Log;

import com.techminds.funclub.utils.config.AppConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by muzammil.hussain on 3/6/2017.
 */

public class TimeUtil {

   private static final String TAG = "TimeUtil";
    private static final String TEST_DATE = "2017-03-06 16:01:06";
    public static String getCurrentDateInString(String dateFormat){
        return new SimpleDateFormat(dateFormat, Locale.US).format(new Date());
    }

    public static int getTimeDiff(String dateStart, String dateStop){

        SimpleDateFormat format = new SimpleDateFormat(AppConstant.DATE_FORMAT_TWO, Locale.US);
        SimpleDateFormat webDateFormat = new SimpleDateFormat(AppConstant.DATE_FORMAT_THREE, Locale.US);

        Date d1 = null;
        Date d2 = null;

        try {

            d1 = format.parse(dateStart);
            d2 = webDateFormat.parse(dateStop);

        }catch (Exception e) {
            e.printStackTrace();
        }

        long diff = d2.getTime() - d1.getTime();

        return (int)diff;
    }

    public static String getParsedTimeDiff(String dateStart, String dateStop){
//        int diff = getTimeDiff(dateStart, TEST_DATE);
        int diff = getTimeDiff(dateStart, dateStop);

        String parsedDate = "";

        SimpleDateFormat timeFormat = new SimpleDateFormat(AppConstant.DATE_FORMAT_THREE, Locale.US);

        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if(AppConstant.IS_DEBUG){
            System.out.println("Time in seconds: " + diffSeconds + " seconds.");
            System.out.println("Time in minutes: " + diffMinutes + " minutes.");
            System.out.println("Time in hours: " + diffHours + " hours.");
            System.out.println("Time in days: " + diffDays + " days.");
        }

        Calendar calendar = Calendar.getInstance();
        Log.d(TAG,"New Calender"+ calendar);
        try{

            calendar.setTime(timeFormat.parse(dateStop));

            int offset = calendar.get(Calendar.AM_PM);
            String timeOffset="";
            if(offset==0)
                timeOffset="AM";
            else
                timeOffset="PM";

            if(diffDays != 0){
                if(Math.abs(diffDays) == 1){
                    String minute = String.valueOf(calendar.get(Calendar.MINUTE));
                    if(minute.length() == 1)
                        minute="0"+minute;
                        parsedDate = "Yesterday at "+calendar.get(Calendar.HOUR)+":"+minute+" "+timeOffset;

                    if(AppConstant.IS_DEBUG) Log.d(TAG, "diffDate: "+parsedDate);
                }else{
                    parsedDate = theMonth(calendar.get(Calendar.MONTH))+" "
                            +calendar.get(Calendar.DATE)+","+calendar.get(Calendar.YEAR);
                    if(AppConstant.IS_DEBUG) Log.d(TAG, "diffDate: "+parsedDate);
                }
            }else if(Math.abs(diffHours) != 0){
                parsedDate = Math.abs(diffHours)+"h";
                if(AppConstant.IS_DEBUG) Log.d(TAG, "diffHour: "+parsedDate);
            }else if(Math.abs(diffMinutes) != 0){
                parsedDate = Math.abs(diffMinutes)+"m";
                if(AppConstant.IS_DEBUG) Log.d(TAG, "diffMinute: "+parsedDate);
            }else if(Math.abs(diffSeconds) != 0){
                parsedDate = Math.abs(diffSeconds)+"sec";
                if(AppConstant.IS_DEBUG) Log.d(TAG, "diffSec: "+parsedDate);
            }
        }catch(Exception e){e.printStackTrace();}


        return parsedDate;
    }

    public static String theMonth(int month){
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month];
    }

    public static Date getFormattedDate(String date) throws Exception {

        SimpleDateFormat sdfFormat1 = new SimpleDateFormat(AppConstant.DATE_FORMAT_ONE, Locale.US);
        SimpleDateFormat sdfFormat2 = new SimpleDateFormat(AppConstant.DATE_FORMAT_TWO, Locale.US);
        SimpleDateFormat sdfFormat3 = new SimpleDateFormat(AppConstant.DATE_FORMAT_THREE, Locale.US);

        try {
            Log.d(TAG, "sdfFormat1: "+sdfFormat1);
            return sdfFormat1.parse(date);
        } catch (ParseException e) { e.printStackTrace();}

        try {
            Log.d(TAG, "sdfFormat2: "+sdfFormat2);
            return sdfFormat2.parse(date);
        } catch (ParseException e) { e.printStackTrace();}

        try {
            Log.d(TAG, "sdfFormat3: "+sdfFormat3);
            return sdfFormat3.parse(date);
        } catch (ParseException e) { e.printStackTrace();}

        throw new Exception(AppConstant.DATE_FORMAT_ERROR);
    }

    //get date start
    public static String getDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");

        return  sdf.format(cal.getTime());
    }
    //get date end
}
