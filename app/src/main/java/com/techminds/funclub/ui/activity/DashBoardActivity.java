package com.techminds.funclub.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techminds.funclub.CustomMethods.JsonClass;
import com.techminds.funclub.R;
import com.techminds.funclub.ui.fragment.base.WallFragment;
import com.techminds.funclub.webServices.WEB_SERVICES_LINKS;
import com.techminds.funclub.entity.Urls;
import com.techminds.funclub.ui.activity.base.BaseActivity;
import com.techminds.funclub.ui.fragment.base.ContentFragment;
import com.techminds.funclub.ui.fragment.base.DashBoardFragment;
import com.techminds.funclub.ui.fragment.base.EventsFragment;
import com.techminds.funclub.ui.fragment.base.InteriorFragment;
import com.techminds.funclub.ui.fragment.base.SouvenirsFragment;
import com.techminds.funclub.ui.fragment.base.UpdateFragment;
import com.techminds.funclub.utils.Constants;
import com.techminds.funclub.utils.config.InteriorDesignPref;
import com.techminds.funclub.utils.config.SessionManager;
import com.techminds.funclub.utils.config.SharedPrefSocialIcons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class DashBoardActivity extends BaseActivity implements View.OnClickListener {

    Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    FragmentManager mFragmentManager;
    SessionManager sessionManager;
    ImageView ivProfilePic;
    DashBoardActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set memeber_id
        Constants.member_id = SharedPrefSocialIcons.getStrings(this, "member_id");

        Constants.urls = getUrls();
        if(Constants.urls == null) {
            Constants.urls = new ArrayList<>();
//            Log.i("getUrls()", Constants.urls.toString());
        }

        Constants.urls2.clear();
//        Log.i("clearedUrls2 ", "size : " + Constants.urls2.size());

        //set Urls in Constant
        room("Tattoo room");
        room("Emojis");
        room("Style");
        room("Headboards");

        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        context = this;

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationAction();
        drawerAction();
        mFragmentManager = getSupportFragmentManager();
        constantFragment(savedInstanceState);
        sessionManager = new SessionManager(this);

    }

    public void saveUrls() {

        Gson gson = new Gson();
        String json = gson.toJson(Constants.urls);
//        Log.i("saveUrls()", json);
        InteriorDesignPref.putString(this, "urls", json);
//        Log.i("saveUrls()","stickers urls saved");

    }

    public ArrayList<Urls> getUrls() {

        Gson gson = new Gson();
        String json = InteriorDesignPref.getStrings(this, "contentUrls");
        if(json != null) {
//            Log.i("getUrls()", json);

            if (!json.equals("[]")) {
//                Log.i("getUrls()", " json : " + json);
                Type type = new TypeToken<ArrayList<Urls>>() {
                }.getType();
                ArrayList<Urls> urls = gson.fromJson(json, type);
//                Log.i("getUrls()", " from Json");
//                Log.i("getUrls()", "returning : " + urls.toString());
                return urls;
            }
        }

//        Log.i("returning null", " from Json");
        return null;

    }

    private void navigationAction() {
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        toolbar.setTitle("Dashboard");
                        replaceFragmentNoHistory(new DashBoardFragment());
                        return true;

                    case R.id.wall:
                        replaceFragmentNoHistory(new WallFragment());
                        return true;

                    case R.id.souvenir:
                        toolbar.setTitle("Souvenirs");
                        replaceFragmentNoHistory(new SouvenirsFragment());
                        return true;
                    case R.id.interior:
                        toolbar.setTitle("Interior Design");
                        InteriorFragment interiorFragment = new InteriorFragment();
                        android.support.v4.app.FragmentTransaction interiorFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        interiorFragmentTransaction.replace(R.id.frame, interiorFragment);
                        interiorFragmentTransaction.commit();
                        return true;
                    case R.id.content:
                        toolbar.setTitle("Content");
                        replaceFragmentNoHistory(new ContentFragment());
                        return true;
                    case R.id.event:
                        toolbar.setTitle("Events");
                        replaceFragmentNoHistory(new EventsFragment());
                        return true;
                    case R.id.Update:
                        toolbar.setTitle("Update Profile");
                        replaceFragmentNoHistory(new UpdateFragment());
                        return true;
                    case R.id.Logout:
                      logout();
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void drawerAction() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                try{

                    super.onDrawerOpened(drawerView);
                    setViews();
                    /***
                     * Setting Profile Pic
                     * */
                    String stype  =  SharedPrefSocialIcons.getStrings(context,"stype");
                    if(stype != null && (stype.equals("FB")|| stype.equals("GMAIL"))){
                        setData();
                        //  Log.i("Method Shared DashBoard","Activity");
                    }

                    else if(null != sessionManager.getUserObject()) {
                        //  Log.i("Method sessio DashBoard","Activity");
                        populateData();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    private void populateData() {

        try{



        String pic = sessionManager.getUserObject().getPicture();

        if(pic.equals("http://dev.technology-minds.com/funclub/manage/webservices/members_profile_picture/")){

        } else {
            Glide.with(this).load(sessionManager.getUserObject().getPicture())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivProfilePic);
        }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void constantFragment(Bundle savedInstanceState) {
        toolbar.setTitle("Dashboard");
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.frame, new DashBoardFragment()).commit();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnBack:
                backButton();
                break;
        }

    }
    public void logout(){
        createCustomDialog();

    }


    public void createCustomDialog(){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_log_out);
        Button dialogButton = (Button) dialog.findViewById(R.id.btnYES);
        Button reserdButton = (Button) dialog.findViewById(R.id.btnNO);
        // if button is clicked, close the custom dialog

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.clearUserInfo();
                SharedPrefSocialIcons.clear(context);
                //Starting login activity
                Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        reserdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        dialog.show();

    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager != null) {

            if (mFragmentManager.findFragmentByTag("HOME") != null && mFragmentManager.getBackStackEntryCount() == 1) {
                finish();
            } else {
                mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                mFragmentManager.beginTransaction()
                        .addToBackStack("HOME")
                        .replace(R.id.frame, new DashBoardFragment(), "HOME")
                        .commit();
            }
        }

    }

    public void setData() {

        String sphoto =  SharedPrefSocialIcons.getStrings(context,"sphoto");

        if(sphoto!= null && !sphoto.isEmpty()) {
            Glide.with(this).load(sphoto)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivProfilePic);
        }

    }


    public void backButton() {

        if (mFragmentManager != null) {

            mFragmentManager.popBackStack();

        }

    }

    @Override
    protected void setOnclickListener() {
    }

    @Override
    protected void setViews() {
         ivProfilePic = (ImageView) findViewById(R.id.profilePicImage);
    }

    @Override
    protected void addFragmentNoHistory(Fragment fragment) {

    }

    @Override
    protected void addFragment(String Tag, Fragment fragment) {
    }

    @Override
    protected void replaceFragmentNoHistory(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }

    @Override
    protected void replaceFragment(String Tag, Fragment fragment) {

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

    //populate urls arraylist
    private void room(final String CategoryInterior) {
        class Room extends AsyncTask<String,Void,String> {
            String room;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                convertingResult(result, room);

                //save URLS
                Gson gson = new Gson();
                String json = gson.toJson(Constants.urls);
                InteriorDesignPref.putString(DashBoardActivity.this, "contentUrls", json);
//                Log.i("requestStickers()", " " + room + " urls saved");
            }

            @Override
            protected String doInBackground(String... params) {
                room = params[0];
                HashMap<String,String> data = new HashMap<>();

                data.put("category", params[0]);

//                Log.i("requestStickers()", "putting data "shrib + params[0]);
                String result = ruc.sendPostRequest(WEB_SERVICES_LINKS.STICKER_URL, data);
//                Log.i("requestStickers()", result);
                return result;
            }
        }

        Room ulc = new Room();
        ulc.execute(CategoryInterior);
    }

    public void convertingResult(String jsonResult, String room){
        try {
            JSONObject obj = new JSONObject(jsonResult);
            JSONArray array = (JSONArray) obj.get("data");
            //   Log.i("JsonArray", String.valueOf(jsonResponse));
            for (int i = 0; i < array.length(); i++) {
                //  JSONObject jsonChildNode = array.getJSONObject(i);

                JSONObject innerObj = array.getJSONObject(i);

                String id = String.valueOf(innerObj.get("id"));
                String name =  String.valueOf(innerObj.get("name"));
                String category =String.valueOf(innerObj.get("category"));
                String imageUrl = String.valueOf(innerObj.get("image"));
                imageUrl = imageUrl.replaceAll(" ","%20");

//                Log.i("ConstantUrl", "added " + imageUrl + " type " + room + " category " + category);

                Constants.urls2.add(new Urls(id, imageUrl, category));

                Constants.updateUrls();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private void bg_Call(String member_id){
//        bg_Call_method(member_id);
//    }
//
//    private void bg_Call_method(final String member_id) {
//        class UserLoginClass extends AsyncTask<String,Void,String> {
////            ProgressDialog loading;
//
//            JsonClass ruc = new JsonClass();
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
////                loading = ProgressDialog.show(context, "Loading Dashboard Content", null, true, true);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//
//                String jsonResult = s;
//                Log.i("JSONRESPONSE",jsonResult);
//
//                try {
//                    if(jsonResult.equals("[]"))
//                        SharedPrefSocialIcons.putString(context, "bg_color", "null");
//                    JSONObject jsonObject = new JSONObject(jsonResult);
//                    String bg_color = jsonObject.getString("bgcolor");
//                    if(bg_color  != null && !bg_color.isEmpty())
//                        SharedPrefSocialIcons.putString(context, "bg_color", bg_color);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
////                loading.dismiss();
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//                HashMap<String,String> data = new HashMap<>();
//                data.put("member_id", params[0]);
//
//                String result = ruc.sendPostRequest(WEB_SERVICES_LINKS.API_SERVER_BG_URL,data);
//
//                return result;
//            }
//        }
//
//        UserLoginClass ulc = new UserLoginClass();
//        ulc.execute(member_id);
//    }

}