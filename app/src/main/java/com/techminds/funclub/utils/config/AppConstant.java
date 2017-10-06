package com.techminds.funclub.utils.config;

/**
 * Created by alkesh.chimnani on 2/6/2017.
 */

public class AppConstant {

    public static final String BASE_URL ="http://dev.technology-minds.com/funclub/manage/webservices";

    public static String  writeExternalStoragePermission = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static String readExternalStoragePermission = "android.permission.READ_EXTERNAL_STORAGE";
    public static String locationPermission = "android.permission.ACCESS_FINE_LOCATION";

    public static boolean IS_DEBUG = true;



    public static final String DATE_FORMAT_ONE = "dd/MM/yyyy";
    public static final String DATE_FORMAT_TWO = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FORMAT_THREE = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_FOUR = "HH:mm a";
    public static final String DATE_FORMAT_ERROR = "Date formatter error";

    public static final String DEVICE_TYPE ="android";
    public static final String CATEGORY  = "category";
    public static final String TYPE_IMAGE  = "image*//*";
    public static String[] gender={"Male","Female"};

    public enum CONTENT_TYPE {
        FORM_DATA("multipart/form-data");

        private String value;

        CONTENT_TYPE(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }
    }



}
