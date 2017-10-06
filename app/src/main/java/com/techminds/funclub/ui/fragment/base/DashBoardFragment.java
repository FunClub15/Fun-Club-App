package com.techminds.funclub.ui.fragment.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.techminds.funclub.CustomMethods.JsonClass;
import com.techminds.funclub.webServices.WEB_SERVICES_LINKS;
import com.techminds.funclub.ui.activity.BaseFragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.techminds.funclub.R;
import com.techminds.funclub.entity.SelectedImage;
import com.techminds.funclub.imageEffect.ClipArt;
import com.techminds.funclub.ui.dialog.DialogBoxes;
import com.techminds.funclub.utils.Constants;
import com.techminds.funclub.utils.config.SessionManager;
import com.techminds.funclub.utils.config.SharedPrefSocialIcons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by waseemakram on 6/15/17.
 */


public class DashBoardFragment extends BaseFragment {
    View view;
    Activity mContext;
    SessionManager sessionManager;
    DashBoardFragment context;

    public static String memid = null;
    public static String image_to_be_Deleted_id;

    Bitmap bitmap;
    ImageView ivProfilePic;
    private AssetManager assetManager;
    public static List<SelectedImage> selectedImageList;
    ClipArt iv_sticker;

    private RelativeLayout root;
    private RelativeLayout canvas;

    DialogBoxes dialogBoxes;


    int i;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        getStickers();

        context = this;
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mContext = getActivity();
        assetManager = getActivity().getAssets();
        sessionManager = new SessionManager(mContext);
        canvas = (RelativeLayout) view.findViewById(R.id.frame);
        root = (RelativeLayout) view.findViewById(R.id.root);


        ivProfilePic = (ImageView) view.findViewById(R.id.profilepic);

        // Log.i("method","callFrag");

        try {
            String stype = SharedPrefSocialIcons.getStrings(getActivity(), "stype");

            if (stype != null && (stype.equals("FB") || stype.equals("GMAIL")))
                setData();

            else if (null != sessionManager.getUserObject()) {
                populateData();
            }
        } catch (Exception e) {


        }


        canvas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                disableall();
            }
        });

        return view;
    }

    private void populateData() {
        Glide.with(this).load(sessionManager.getUserObject().getPicture())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivProfilePic);
    }

    public void setData() {
        String sphoto = SharedPrefSocialIcons.getStrings(getActivity(), "sphoto");
        Log.i("userResponse()", "sphoto: " + sphoto);
        if (sphoto != null && !sphoto.isEmpty()) {
            Glide.with(this).load(sphoto)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivProfilePic);
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        root.setBackgroundResource(R.drawable.fragment_bg);

        if (SharedPrefSocialIcons.getStrings(getActivity(), "bg_color") != null) {
            try {
                root.setBackgroundColor(Color.parseColor(SharedPrefSocialIcons.getStrings(getActivity(), "bg_color")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {

            bg_Call_method(Constants.member_id);


            Constants.dashBoardStickers.clear();
            requestDashboardStickers(SharedPrefSocialIcons.getStrings(getActivity(), "member_id"));

            setImageOnScreen();
            disableall();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setImageOnScreen() {

        Log.i("dashBoardStickersSize()", " size : " + Constants.dashBoardStickers.size());

        if (Constants.dashBoardStickers.size() > 0) {
            canvas.removeAllViews();

            for (i = 0; i < Constants.dashBoardStickers.size(); i++) {
                try {

                    iv_sticker = new ClipArt(getActivity(), Constants.dashBoardStickers);
                    iv_sticker.setLabelFor(i);

                    int x = (int) Float.parseFloat(Constants.dashBoardStickers.get(i).getPostitionX());
                    int y = (int) Float.parseFloat(Constants.dashBoardStickers.get(i).getPostitionY());
                    int width = (int) Float.parseFloat(Constants.dashBoardStickers.get(i).getWidth());
                    int height = (int) Float.parseFloat(Constants.dashBoardStickers.get(i).getHight());
                    float angle;
                    try {
                        angle = Float.parseFloat(Constants.dashBoardStickers.get(i).getAngel());
                    } catch (NumberFormatException e) {
                        angle = 0;
                    }

                    Log.i("Sticker()","\nx: "+x+"\ny: "+y+"\nwidth: "+width+"\nheight: "+height+"\nangle: "+angle);

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                    params.topMargin = y;
                    params.leftMargin = x;
                    iv_sticker.setRotation(angle);
                    iv_sticker.layoutParams = params;
                    iv_sticker.setLayoutParams(params);

                    iv_sticker.setImageId(Constants.dashBoardStickers.get(i).getImageid());

                    iv_sticker.index = i;

                    if (Constants.dashBoardStickers.get(i).getImgUrl() == null) {
                        iv_sticker.setImageFromUri(mContext, Constants.dashBoardStickers.get(i).getImgPath());
                    } else {
                        iv_sticker.setImageFromUrl(mContext, Constants.dashBoardStickers.get(i).getImgUrl());
                    }

                    canvas.addView(iv_sticker);
                    disableall();

                    iv_sticker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            disableall();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }

    }

    public void disableall() {
        for (int i = 0; i < canvas.getChildCount(); i++) {
            if (canvas.getChildAt(i) instanceof ClipArt) {
                ((ClipArt) canvas.getChildAt(i)).disableAll();
            }
        }
    }

    private Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    private void setBgAsync() {
//        dialogBoxes = new DialogBoxes();

        class setBgAsync extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                dialogBoxes.showProgress(getActivity(), "Loading Dashboard Content", R.string.loading);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s.equals("null"))
                    root.setBackgroundResource(R.drawable.fragment_bg);
                else
                    root.setBackgroundColor(Color.parseColor(s));

//                dialogBoxes.hideProgress();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (SharedPrefSocialIcons.getStrings(getActivity(), "bg_color") == null) {
                    return "null";
                }

                return SharedPrefSocialIcons.getStrings(getActivity(), "bg_color");
            }
        }

        setBgAsync ulc = new setBgAsync();
        ulc.execute();
    }


    static String result = "[]";

    public static void getStickers() {

        class sendStickers extends AsyncTask<String, Void, String> {
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

                String jsonResult = s;
                Log.i("JSONRESPONSE", jsonResult);

                JSONObject obj = null;
                try {
                    obj = new JSONObject(s);
                    result = String.valueOf(obj.get("data"));
                    Log.i("jsonResult()", result);
                    result = s;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("member_id", params[0]);

                String result = ruc.sendPostRequest(WEB_SERVICES_LINKS.GET_STICKERS_URL, data);

                return result;
            }
        }

        sendStickers ulc = new sendStickers();
        ulc.execute(Constants.member_id);
    }

    //populate dashboard Stickers arraylist
    //populate urls arraylist

    private void requestDashboardStickers(String member_id) {
        class Room extends AsyncTask<String, Void, String> {

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                Log.i("dashboardStickers()", "preExecute");
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                if (result.equals("[]")) {

                } else {
                    try {
                        convertingResultDashboardStickers(result);

                        //save URLS
                        Gson gson = new Gson();
                        String json = gson.toJson(Constants.dashBoardStickers);
                        Log.i("saveDashboardStickers()", "Request" + json);
                        Log.i("saveDashboardStickers()", "dashboardStickers saved");

                        SharedPrefSocialIcons.putString(getActivity(), "dashboardStickers", json);

                        setImageOnScreen();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                // Log.i("member_id", params[0]);
                data.put("member_id", params[0]);

                String result = ruc.sendPostRequest(WEB_SERVICES_LINKS.DASHBOARD_STICKER_URL, data);
                return result;
            }
        }

        Room ulc = new Room();
        ulc.execute(member_id);
    }

    public void convertingResultDashboardStickers(String jsonResult) {


        try {

            JSONObject obj = new JSONObject(jsonResult);
            JSONArray array = (JSONArray) obj.get("data");

            for (int i = 0; i < array.length(); i++) {

                JSONObject innerObj = array.getJSONObject(i);

                String id = String.valueOf(innerObj.get("id"));
                String xaxis = String.valueOf(innerObj.get("xaxis"));
                String yaxis = String.valueOf(innerObj.get("yaxis"));
                String width = String.valueOf(innerObj.get("width"));
                String height = String.valueOf(innerObj.get("height"));
                String angle = String.valueOf(innerObj.get("angle"));
                String image = String.valueOf(innerObj.get("image"));

                //    Log.i("dashboardStickers()", " added : " + id);
                Constants.dashBoardStickers.add(new SelectedImage(id, xaxis, yaxis, width, height, angle, image));
            }


            try {

                SharedPrefSocialIcons.putString(getActivity(), "dashboardStickers", obj.toString());

            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bg_Call_method(final String member_id) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
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

                String jsonResult = s;
                //    Log.i("JSONRESPONSE",jsonResult);

                try {
                    if (jsonResult.equals("[]"))
                        SharedPrefSocialIcons.putString(getActivity(), "bg_color", "null");

                    JSONObject jsonObject = new JSONObject(jsonResult);
                    String bg_color = jsonObject.getString("bgcolor");

                    if (bg_color != null && !bg_color.isEmpty()) {
                        SharedPrefSocialIcons.putString(getActivity(), "bg_color", bg_color);
                        root.setBackgroundColor(Color.parseColor(bg_color));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("member_id", params[0]);

                String result = ruc.sendPostRequest(WEB_SERVICES_LINKS.API_SERVER_BG_URL, data);

                return result;
            }
        }

        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(member_id);
    }

}