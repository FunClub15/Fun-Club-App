package com.techminds.funclub.imageEffect;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.Dash;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.techminds.funclub.CustomMethods.JsonClass;
import com.techminds.funclub.entity.SelectedImage;
import com.techminds.funclub.ui.fragment.base.DashBoardFragment;
import com.techminds.funclub.utils.AppMethod;
import com.techminds.funclub.utils.Constants;
import com.techminds.funclub.utils.UserPrefs;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.techminds.funclub.R;
import com.techminds.funclub.utils.config.SharedPrefSocialIcons;
import com.techminds.funclub.webServices.WEB_SERVICES_LINKS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClipArt extends RelativeLayout {

    public String iamgeId;
    int baseh;
    int basew;
    int basex;
    int basey;
    public ImageButton btndel;
    public ImageButton btnrot;
    public ImageButton btnscl;
    public RelativeLayout clip;
    Context cntx;
    public boolean freeze = false;
    int h;
    int i;
    ImageView image;
    String id;
    String imageUri;
    ImageView imgring;
    boolean isShadow;
    int iv;
    public RelativeLayout layBg;
    public RelativeLayout layGroup;
    public LayoutParams layoutParams;
    public LayoutInflater mInflater;
    int margl;
    int margt;
    // DisplayImageOptions op;
    float opacity = 1.0F;
    Bitmap originalBitmap;
    int pivx;
    int pivy;
    int pos;
    Bitmap shadowBitmap;
    float startDegree;
    String[] v;
    public static int index = 0;
    private List<SelectedImage> selectedImageList;
    ClipArt iv_sticker;


    public ClipArt(Context paramContext, final List<SelectedImage> selectedImageList) {
        super(paramContext);
        cntx = paramContext;
        this.selectedImageList = selectedImageList;
        layGroup = this;
        // this.clip = paramRelativeLayout;
        basex = 0;
        basey = 0;
        pivx = 0;
        pivy = 0;
        // .v = paramArrayOfString;
        // this.op = paramDisplayImageOptions;
        mInflater = ((LayoutInflater) paramContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        mInflater.inflate(R.layout.clipart, this, true);
        btndel = ((ImageButton) findViewById(R.id.del));
        btnrot = ((ImageButton) findViewById(R.id.rotate));
        btnscl = ((ImageButton) findViewById(R.id.sacle));
        image = (ImageView) findViewById(R.id.image);
        // imageUri = ("assets://Cliparts/" + paramArrayOfString[paramInt1]);
        layoutParams = new LayoutParams(250, 250);
        layGroup.setLayoutParams(layoutParams);

        image.setImageResource(R.mipmap.ic_launcher);
        image.setTag(Integer.valueOf(0));


        //image.setImageResource(R.mipmap.ic_launcher);
        // ImageLoader.getInstance().displayImage(this.imageUri, this.image,
        // paramDisplayImageOptions);

        setOnTouchListener(new OnTouchListener() {
            final GestureDetector gestureDetector = new GestureDetector(ClipArt.this.cntx,
                    new GestureDetector.SimpleOnGestureListener() {
                        public boolean onDoubleTap(MotionEvent paramAnonymous2MotionEvent) {
                            return false;
                        }
                    });

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            public boolean onTouch(View paramAnonymousView, MotionEvent event) {
                index = paramAnonymousView.getLabelFor();
                ClipArt.this.visiball();
                if (!ClipArt.this.freeze) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            layGroup.invalidate();
                            gestureDetector.onTouchEvent(event);
                            layGroup.bringToFront();
                            layGroup.performClick();
                            basex = ((int) (event.getRawX() - layoutParams.leftMargin));
                            basey = ((int) (event.getRawY() - layoutParams.topMargin));

                            break;
                        case MotionEvent.ACTION_MOVE:
                            int i = (int) event.getRawX();
                            int j = (int) event.getRawY();
                            layBg = (RelativeLayout) (getParent());
                        /*if ((i - basex > -(layGroup.getWidth() * 2 / 3))
								&& (i - basex < layBg.getWidth() - layGroup.getWidth() / 3)) {
							layoutParams.leftMargin = (i - basex);
						}
						if ((j - basey > -(layGroup.getHeight() * 2 / 3))
								&& (j - basey < layBg.getHeight() - layGroup.getHeight() / 3)) {
							layoutParams.topMargin = (j - basey);
						}*/
                            layoutParams.topMargin = (j - basey);
                            layoutParams.leftMargin = (i - basex);
                            layoutParams.rightMargin = -9999999;
                            layoutParams.bottomMargin = -9999999;
                            layGroup.setLayoutParams(layoutParams);
                            break;

                        case MotionEvent.ACTION_UP:
                            String width = String.valueOf(ClipArt.this.getWidth());
                            String height = String.valueOf(ClipArt.this.getHeight());
                            String x = String.valueOf(ClipArt.this.getX());
                            String y = String.valueOf(ClipArt.this.getY());
                            String angle = String.valueOf(ClipArt.this.getRotation());
                            Log.i("Sticker()","\nid: "+ClipArt.this.id+"\nwidth: "+width+"\nheight: "+height+"\nx: "+x+"\ny: "+y+"\nangle: "+angle);
                            updateSticker(ClipArt.this.id, width, height, x, y, angle);
                            break;
                    }


                    try {

                        selectedImageList.get(index).setWidth("" + ClipArt.this.layGroup.getWidth());
                        selectedImageList.get(index).setHight("" + ClipArt.this.layGroup.getHeight());
                        selectedImageList.get(index).setPostitionX(ClipArt.this.layGroup.getX() + "");
                        selectedImageList.get(index).setPostitionY(ClipArt.this.layGroup.getY() + "");
                        selectedImageList.get(index).setAngel(ClipArt.this.layGroup.getRotation() + "");
                        Gson gson = new Gson();
                        String obj = gson.toJson(selectedImageList);
                        UserPrefs.setSelectedImage(obj, cntx);
                        return true;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                return true;
                // freeze;
            }
        });

        this.btnscl.setOnTouchListener(new OnTouchListener() {
            @SuppressLint({"NewApi"})
            public boolean onTouch(View paramAnonymousView, MotionEvent event) {
                ///Log.e(Constants.TAG, "View index is:::> "+ paramAnonymousView.getLabelFor());
                if (!ClipArt.this.freeze) {
                    int j = (int) event.getRawX();
                    int i = (int) event.getRawY();
                    layoutParams = (LayoutParams) layGroup.getLayoutParams();
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            ClipArt.this.layGroup.invalidate();
                            ClipArt.this.basex = j;
                            ClipArt.this.basey = i;
                            ClipArt.this.basew = ClipArt.this.layGroup.getWidth();
                            ClipArt.this.baseh = ClipArt.this.layGroup.getHeight();
                            int[] loaction = new int[2];
                            layGroup.getLocationOnScreen(loaction);
                            margl = layoutParams.leftMargin;
                            margt = layoutParams.topMargin;
                            break;
                        case MotionEvent.ACTION_MOVE:

                            float f2 = (float) Math.toDegrees(Math.atan2(i - ClipArt.this.basey, j - ClipArt.this.basex));
                            float f1 = f2;
                            if (f2 < 0.0F) {
                                f1 = f2 + 360.0F;
                            }
                            j -= ClipArt.this.basex;
                            int k = i - ClipArt.this.basey;
                            i = (int) (Math.sqrt(j * j + k * k) * Math.cos(Math.toRadians(f1
                                    - ClipArt.this.layGroup.getRotation())));
                            j = (int) (Math.sqrt(i * i + k * k) * Math.sin(Math.toRadians(f1
                                    - ClipArt.this.layGroup.getRotation())));
                            k = i * 2 + ClipArt.this.basew;
                            int m = j * 2 + ClipArt.this.baseh;
                            if (k > 150) {
                                layoutParams.width = k;
                                layoutParams.leftMargin = (ClipArt.this.margl - i);
                            }
                            if (m > 150) {
                                layoutParams.height = m;
                                layoutParams.topMargin = (ClipArt.this.margt - j);
                            }
                            ClipArt.this.layGroup.setLayoutParams(layoutParams);
                            ClipArt.this.layGroup.performLongClick();
                            break;

                        case MotionEvent.ACTION_UP:
                            String width = String.valueOf(ClipArt.this.getWidth());
                            String height = String.valueOf(ClipArt.this.getHeight());
                            String x = String.valueOf(ClipArt.this.getX());
                            String y = String.valueOf(ClipArt.this.getY());
                            String angle = String.valueOf(ClipArt.this.getRotation());
                            Log.i("Sticker()","\nid: "+ClipArt.this.id+"\nwidth: "+width+"\nheight: "+height+"\nx: "+x+"\ny: "+y+"\nangle: "+angle);
                            updateSticker(ClipArt.this.id, width, height, x, y, angle);
                            break;
                    }

                    try {

                        selectedImageList.get(index).setWidth("" + ClipArt.this.layGroup.getWidth());
                        selectedImageList.get(index).setHight("" + ClipArt.this.layGroup.getHeight());
                        selectedImageList.get(index).setPostitionX(ClipArt.this.layGroup.getX() + "");
                        selectedImageList.get(index).setPostitionY(ClipArt.this.layGroup.getY() + "");
                        selectedImageList.get(index).setAngel(ClipArt.this.layGroup.getRotation() + "");
                        Gson gson = new Gson();
                        String obj = gson.toJson(selectedImageList);
                        UserPrefs.setSelectedImage(obj, cntx);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    return true;
                }
                return ClipArt.this.freeze;
            }
        });
        this.btnrot.setOnTouchListener(new OnTouchListener() {
            @SuppressLint({"NewApi"})
            public boolean onTouch(View paramAnonymousView, MotionEvent event) {
                if (!ClipArt.this.freeze) {
                    layoutParams = (LayoutParams) ClipArt.this.layGroup.getLayoutParams();
                    ClipArt.this.layBg = ((RelativeLayout) ClipArt.this.getParent());
                    int[] arrayOfInt = new int[2];
                    layBg.getLocationOnScreen(arrayOfInt);
                    int i = (int) event.getRawX() - arrayOfInt[0];
                    int j = (int) event.getRawY() - arrayOfInt[1];
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            ClipArt.this.layGroup.invalidate();
                            ClipArt.this.startDegree = layGroup.getRotation();
                            ClipArt.this.pivx = (layoutParams.leftMargin + ClipArt.this.getWidth() / 2);
                            ClipArt.this.pivy = (layoutParams.topMargin + ClipArt.this.getHeight() / 2);
//                            ClipArt.this.basex = (i - ClipArt.this.pivx);
//                            ClipArt.this.basey = (ClipArt.this.pivy - j);
                            break;

                        case MotionEvent.ACTION_MOVE:
                            int k = ClipArt.this.pivx;
                            int m = ClipArt.this.pivy;
                            j = (int) (Math.toDegrees(Math.atan2(ClipArt.this.basey, ClipArt.this.basex)) - Math
                                    .toDegrees(Math.atan2(m - j, i - k)));
                            i = j;
                            if (j < 0) {
                                i = j + 360;
                            }
                            ClipArt.this.layGroup.setRotation((ClipArt.this.startDegree + i) % 360.0F);
                            break;

                        case MotionEvent.ACTION_UP:
                            String width = String.valueOf(ClipArt.this.getWidth());
                            String height = String.valueOf(ClipArt.this.getHeight());
                            String x = String.valueOf(ClipArt.this.getX());
                            String y = String.valueOf(ClipArt.this.getY());
                            String angle = String.valueOf(ClipArt.this.getRotation());
                            Log.i("Sticker()","\nid: "+ClipArt.this.id+"\nwidth: "+width+"\nheight: "+height+"\nx: "+x+"\ny: "+y+"\nangle: "+angle);
                            updateSticker(ClipArt.this.id, width, height, x, y, angle);
                            break;
                    }


                    selectedImageList.get(index).setWidth("" + ClipArt.this.layGroup.getWidth());
                    selectedImageList.get(index).setHight("" + ClipArt.this.layGroup.getHeight());
                    selectedImageList.get(index).setPostitionX(ClipArt.this.layGroup.getX() + "");
                    selectedImageList.get(index).setPostitionY(ClipArt.this.layGroup.getY() + "");
                    selectedImageList.get(index).setAngel(ClipArt.this.layGroup.getRotation() + "");

                    Gson gson = new Gson();
                    String obj = gson.toJson(selectedImageList);
                    UserPrefs.setSelectedImage(obj, cntx);
                    return true;
                }


                return ClipArt.this.freeze;
            }
        });


        this.btndel.setOnClickListener(new OnClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void onClick(View paramAnonymousView) {
                Log.i("deleting()", "shit");

                Log.e("funclubb()", "image Id is::> " + id);
                DashBoardFragment.selectedImageList = AppMethod.getImageList(cntx);
                DashBoardFragment.selectedImageList = Constants.dashBoardStickers;

                deleteSticker(id);

                // deleteSticker(DashBoardFragment.image_to_be_Deleted_id);

//                Log.i("deleting()id", String.valueOf(ClipArt.index));
//
//				//Log.e(Constants.TAG, "View Delete is:::> "+ paramAnonymousView.getLabelFor());
                if (!ClipArt.this.freeze) {
                    layBg = ((RelativeLayout) ClipArt.this.getParent());
                    layBg.performClick();
                    layBg.removeView(ClipArt.this.layGroup);
                }

//                Constants.removeDashboardImage(selectedImageList.get(ClipArt.index).getImageid());
            }
        });

    }

    public void disableAll() {
        this.btndel.setVisibility(View.INVISIBLE);
        this.btnrot.setVisibility(View.INVISIBLE);
        this.btnscl.setVisibility(View.INVISIBLE);
    }

    public ImageView getImageView() {
        return this.image;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public float getOpacity() {
        return this.image.getAlpha();
    }

    public void resetImage() {
        this.originalBitmap = null;
        this.layGroup.performLongClick();
    }

    public void setColor(int paramInt) {
        this.image.getDrawable().setColorFilter(null);
        ColorMatrixColorFilter localColorMatrixColorFilter = new ColorMatrixColorFilter(new float[]{0.33F, 0.33F,
                0.33F, 0.0F, Color.red(paramInt), 0.33F, 0.33F, 0.33F, 0.0F, Color.green(paramInt), 0.33F, 0.33F,
                0.33F, 0.0F, Color.blue(paramInt), 0.0F, 0.0F, 0.0F, 1.0F, 0.0F});
        this.image.getDrawable().setColorFilter(localColorMatrixColorFilter);
        this.image.setTag(Integer.valueOf(paramInt));
        this.layGroup.performLongClick();
    }

    public void setImageBitmap(Bitmap bmp) {
        this.image.setImageBitmap(bmp);
    }

    public void setImageFromUrl(final Context mContext, final String url) {
        Picasso.with(mContext)
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(getImageView(), new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Picasso", "Image loaded from cache>>>" + url);
                    }

                    @Override
                    public void onError() {
                        Log.d("Picasso", "Try again in ONLINE mode if load from cache is failed");
                        Picasso.with(mContext).load(url).into(getImageView(), new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d("Picasso", "Image loaded from web>>>" + url);
                            }

                            @Override
                            public void onError() {
                                Log.d("Picasso", "Failed to load image online and offline, make sure you enabled INTERNET permission for your app and the url is correct>>>>>>>"
                                        + url);
                            }
                        });
                    }
                });
    }

    public void setImageFromUri(final Context mContext, final String uri) {
//        Picasso.with(mContext).load(uri).into(getImageView());
        Picasso.with(mContext).load("file://" + uri).into(getImageView());

    }


    public void setFreeze(boolean paramBoolean) {
        this.freeze = paramBoolean;
    }


    public void setImageId(String id) {
        this.id = id;
    }

    public void setImageId() {
        this.image.setId(this.layGroup.getId() + this.i);
        this.i += 1;
    }

    public void setLocation() {
        this.layBg = ((RelativeLayout) getParent());
        LayoutParams localLayoutParams = (LayoutParams) this.layGroup.getLayoutParams();
        localLayoutParams.topMargin = ((int) (Math.random() * (this.layBg.getHeight() - 400)));
        localLayoutParams.leftMargin = ((int) (Math.random() * (this.layBg.getWidth() - 400)));
        this.layGroup.setLayoutParams(localLayoutParams);
    }

    public void visiball() {
        this.btndel.setVisibility(View.VISIBLE);
        this.btnrot.setVisibility(View.VISIBLE);
        this.btnscl.setVisibility(View.VISIBLE);
    }

    public static abstract interface DoubleTapListener {
        public abstract void onDoubleTap();
    }


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    private void deleteSticker(final String image_id) {
        class Room extends AsyncTask<String, Void, String> {

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                Log.i("deletingSticker()", "preExecute");
            }


            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.i("deletingSticker()", "deleted : " + result);

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                // Log.i("member_id", params[0]);
                data.put("member_id", SharedPrefSocialIcons.getStrings(cntx, "member_id"));
                data.put("id", params[0]);

                Constants.removeDashboardImage(params[0]);

                String result = ruc.sendPostRequest(WEB_SERVICES_LINKS.DELETE_STICKER, data);
                return result;
            }
        }

        Room ulc = new Room();
        ulc.execute(image_id);
    }

    private void updateSticker(final String image_id, String width, String height, String x, String y, String angle) {
        class updateSticker extends AsyncTask<String, Void, String> {

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                Log.i("updatingSticker()", "preExecute");
            }


            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.i("updatingSticker()", "postExecute\nResult:"+result);

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();

                data.put("member_id", params[0]);
                data.put("image_id", params[1]);
                data.put("width", params[2]);
                data.put("height", params[3]);
                data.put("xaxis", params[4]);
                data.put("yaxis", params[5]);
                data.put("angle", params[6]);

                Log.i("updatingSticker()", "doInBackground");

                String result = ruc.sendPostRequest(WEB_SERVICES_LINKS.UPDATE_STICKER_URL, data);
                return result;
            }
        }

        updateSticker ulc = new updateSticker();
        ulc.execute(Constants.member_id, image_id, width, height, x, y, angle);
    }

}

