package com.techminds.funclub.ui.activity;


import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techminds.funclub.CustomMethods.JsonClass;
import com.techminds.funclub.adapter.ImageAdapter;
import com.techminds.funclub.entity.SelectedImage;
import com.techminds.funclub.entity.Urls;
import com.techminds.funclub.ui.dialog.DialogBoxes;
import com.techminds.funclub.utils.Constants;
import com.techminds.funclub.utils.UserPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import com.techminds.funclub.R;
import com.techminds.funclub.utils.config.SharedPrefSocialIcons;
import com.techminds.funclub.webServices.WEB_SERVICES_LINKS;

public class PictureGallary extends BaseActivity {
    private final static String TAG = "MainActivity";

    //    private AssetManager assetManager;
    GridView gridView;
    ImageAdapter imageAdapter;
    String imgArray[];
    TextView titleheader;
    String titleStr;
    PictureGallary context;
    AlertDialog.Builder dialogBuilder;


    Button btnOK, btnNO;


    String member_id;
    String picture;
    String xaxis;
    String yaxis;
    String width;
    String height;
    String angle;

    //TEST
    ArrayList<String> urls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickers);
        context = this;
        init();
        setClick(R.id.btnBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;

            default:
                break;
        }
    }

    private void init() {
        titleStr = getIntent().getExtras().getString("activity");
//        assetManager = getAssets();
        titleheader = (TextView) findViewById(R.id.titleheader);
        urls = Constants.getUrls();

        if (titleStr.equalsIgnoreCase("tattooroom"))
            titleheader.setText("Tattoo Room");
        else
            titleheader.setText(titleStr.substring(0, 1).toUpperCase() + titleStr.substring(1));

        gridView = (GridView) findViewById(R.id.grid);

//        Log.i("ImageAdapter()", urls.toString());
        imageAdapter = new ImageAdapter(this, imgArray, titleStr, urls);

        gridView.setAdapter(imageAdapter);
//        Log.i("OnCLICK_path", "wow");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                List<SelectedImage> selectedImageList = new ArrayList<>();
                SelectedImage selectedImage = new SelectedImage();
//                selectedImage.setImgPath(imgArray[position]);
                selectedImage.setImgUrl(context, urls.get(position));
//                Log.i("OnCLICK_path", urls.get(position));
//                Constants.ImageSources.add(imgArray[position]);

                selectedImage.setImgType("s");
                selectedImage.setPostitionX("10");
                selectedImage.setPostitionY("10");
                selectedImage.setHight("250");
                selectedImage.setWidth("250");
                selectedImage.setAngel("0");

                if (titleStr.equalsIgnoreCase("tattooroom")) {
                    selectedImage.setImgType("tattooroom");
                } else if (titleStr.equalsIgnoreCase("newphotos")) {
                    selectedImage.setImgType("newphotos");
                } else if (titleStr.equalsIgnoreCase("headboards")) {
                    selectedImage.setImgType("headboards");
                } else if (titleStr.equalsIgnoreCase("emojis")) {
                    selectedImage.setImgType("emojis");
                } else if (titleStr.equalsIgnoreCase("style")) {
                    selectedImage.setImgType("style");
                }

                //sendSelectedImage(selectedImage, position);
                // POSTSelectedImage(selectedImage, position);
                POSTSelectedImages2(selectedImage, position);


                Gson gson = new Gson();
                if (UserPrefs.getSelectedImage(PictureGallary.this).length() > 0) {
                    try {

                        String data = UserPrefs.getSelectedImage(PictureGallary.this);
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
                            }
                            selectedImageList.add(selectedImage);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    selectedImageList.add(selectedImage);
                }

                String obj = gson.toJson(selectedImageList);
                UserPrefs.setSelectedImage(obj, PictureGallary.this);
                Constants.selectedImages.add(selectedImage);

                /**
                 * Show Dialog for user
                 *
                 * */


                dialogBuilder = new AlertDialog.Builder(context);

                LayoutInflater inflater = context.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_interior_design, null);
                dialogBuilder.setView(dialogView);

                btnOK = (Button) dialogView.findViewById(R.id.btnOK);
                btnNO = (Button) dialogView.findViewById(R.id.btnNO);


            }
        });
    }


    public void method_Show_dialog() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;


        final AlertDialog alertDialog = dialogBuilder.create();
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog.dismiss();

            }
        });

        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //  AlertDialog alertDialo = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout((width / 100) * 100, ((width / 100) * 80) * 80 / 100);

    }


    public ArrayList<String> getUrls() {
        ArrayList<String> urls = new ArrayList<>();
        return urls;
    }


//    private void sendSelectedImage(SelectedImage selectedImage, int position) {
//
//
//
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        Bitmap bitmap = getBitmapFromURL(selectedImage.getImgUrl());
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//        byte[] byteArray = byteArrayOutputStream .toByteArray();
//
//        String member_id = Constants.member_id;
//        String picture = Base64.encodeToString(byteArray, Base64.DEFAULT);
//        String xaxis = selectedImage.getPostitionX();
//        String yaxis = selectedImage.getPostitionY();
//        String width = selectedImage.getWidth();
//        String height = selectedImage.getHight();
//        String angle = selectedImage.getAngel();
//
//        Log.i("POSTSelectedImageClass",member_id+" "+xaxis+" "+yaxis+" "+width+" "+height+" "+angle+" "+picture);
//
//        POSTSelectedImage(member_id, picture, xaxis, yaxis, width, height, angle, position);
//    }

    //    private void POSTSelectedImage(final String member_id, final String picture, final String xaxis, final String yaxis,
//                                   final String width, final String height, final String angle, final int position)
    private void POSTSelectedImage(final SelectedImage selectedImage, final int position) {
        class POSTSelectedImageClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading = new ProgressDialog(context);


            String member_id;
            String picture;
            String xaxis;
            String yaxis;
            String width;
            String height;
            String angle;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading.setMessage("Sending Image");

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap bitmap = getBitmapFromURL(selectedImage.getImgUrl());

                Log.i("SelectedImage ", selectedImage.getImgUrl());

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                member_id = Constants.member_id;
                picture = Base64.encodeToString(byteArray, Base64.DEFAULT);
                xaxis = selectedImage.getPostitionX();
                yaxis = selectedImage.getPostitionY();
                width = selectedImage.getWidth();
                height = selectedImage.getHight();
                angle = selectedImage.getAngel();

//                Log.i("POSTSelectedImageClass",member_id+" "+xaxis+" "+yaxis+" "+width+" "+height+" "+angle+" "+picture);

                loading.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

//                dialogBoxes.dismiss();
                loading.dismiss();

                String jsonResult = s;
//                Log.i("POSTSelectedImageClass", " jsonResult "+jsonResult);

                try {
                    JSONObject jsonObj = new JSONObject(jsonResult);

                    if (String.valueOf(jsonObj.get("status")).equals("1")) {
                        String replace = "\\/";
                        String id = String.valueOf(jsonObj.get("image_id"));
                        String url = String.valueOf(jsonObj.get("image")).replaceAll(replace, "/");
                        //  Log.i("POSTSelectedImageClass"," url "+url);
                        Constants.selectedImages.get(position).setImgUrl(context, url);
                        Constants.selectedImages.get(position).setImageid(id);
                    }
                } catch (JSONException e) {
                    //  Log.i("JSONEXCEPTION"," at onPost");
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();

                data.put("member_id", member_id);
                data.put("picture", picture);
                data.put("xaxis", xaxis);
                data.put("yaxis", yaxis);
                data.put("width", width);
                data.put("height", height);
                data.put("angel", angle);

//                data.put("member_id",params[0]);
//                data.put("picture",params[1]);
//                data.put("xaxis",params[2]);
//                data.put("yaxis",params[3]);
//                data.put("width",params[4]);
//                data.put("height",params[5]);
//                data.put("angel",params[6]);

                String result = ruc.sendPostRequest(WEB_SERVICES_LINKS.POST_STICKER_URL, data);
                return result;

            }
        }

        POSTSelectedImageClass ulc = new POSTSelectedImageClass();
        ulc.execute();
//        ulc.execute(member_id, picture, xaxis, yaxis, width, height, angle);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    String result;
    ProgressDialog loading;


    private void POSTSelectedImages2(final SelectedImage selectedImage, final int position) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(context, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String image_id = obj.getString("image_id");
                    String image = obj.getString("image");
                    String xaxis = obj.getString("xaxis");
                    String yaxis = obj.getString("yaxis");
                    String width = obj.getString("width");
                    String height = obj.getString("height");
                    String angle = obj.getString("angle");

                    Constants.dashBoardStickers.add(new SelectedImage(image_id, xaxis, yaxis, width, height, angle, image));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loading.dismiss();
                method_Show_dialog();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    Bitmap bitmap = getBitmapFromURL(selectedImage.getImgUrl());

                    //   Log.i("SelectedImage ",selectedImage.getImgUrl());

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    member_id = Constants.member_id;
                    picture = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    xaxis = selectedImage.getPostitionX();
                    yaxis = selectedImage.getPostitionY();
                    width = selectedImage.getWidth();
                    height = selectedImage.getHight();
                    angle = "0";

                    HashMap<String, String> data = new HashMap<>();

                    data.put("member_id", member_id);
                    data.put("picture", picture);
                    data.put("xaxis", xaxis);
                    data.put("yaxis", yaxis);
                    data.put("width", width);
                    data.put("height", height);
                    data.put("angle", angle);

                    result = ruc.sendPostRequest(WEB_SERVICES_LINKS.POST_STICKER_URL, data);
                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return result;
            }
        }

        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(member_id, picture, xaxis, yaxis, width, height, angle);
    }


}
