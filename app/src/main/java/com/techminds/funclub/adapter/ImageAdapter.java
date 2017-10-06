package com.techminds.funclub.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toolbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.techminds.funclub.R;

/**
 * Created by TOS on 03/10/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
//    private String imgPath[];
    private String titleStr;
    private AssetManager assetManager;

    //TEST
    ArrayList<String> urls = new ArrayList<>();

    // Constructor
    public ImageAdapter(Context c, String imgPath[], String titleStr, ArrayList<String> urls) {
        mContext = c;
//        this.imgPath = imgPath;
        this.titleStr = titleStr;
        assetManager = mContext.getAssets();

        //TEST
        this.urls = urls;
    }

    public int getCount() {
        return urls.size();
//        return imgPath.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

        View gridView = convertView;
        ImageView imageView;
        if (gridView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.customgridview_item, parent, false);

        } else {
            gridView = (View) convertView;
        }

        imageView = (ImageView) gridView
                .findViewById(R.id.galleryImageView);


//        InputStream is = null;
        try {
//            is = assetManager.open(titleStr + "/" + imgPath[position]);

            Activity activity = (Activity) mContext;
            Display display = activity.getWindowManager().getDefaultDisplay();

            int screenWidth = display.getWidth();
            int screeHeight = display.getHeight();

//            Bitmap bitmap = BitmapFactory.decodeStream(is);

//            if (bitmap != null) {
//                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
//
//                imageView = new ImageView(mContext);
//                imageView.setLayoutParams(new GridView.LayoutParams((screenWidth * 30) / 100, (screeHeight * 20) / 105));
//                imageView.setBackgroundResource(R.drawable.roundcorners);
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                // imageView.setScaleType( ImageView.ScaleType.CENTER_CROP );
//                imageView.setPadding(12, 12, 12, 12);
////                imageView.setImageBitmap(resizedBitmap);
//
//                Picasso.with(mContext).load(urls.get(position)).into(imageView);
//
//                Log.e("new memory for :: >", (titleStr + "/" + imgPath[position]));
//                Log.e("memory amount is :: >", resizedBitmap.toString());
//            }

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams((screenWidth * 30) / 100, (screeHeight * 20) / 105));
            imageView.setBackgroundResource(R.drawable.roundcorners);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(12, 12, 12, 12);

            Log.i("ImageAdapter()", urls.get(position).toString());

//            Picasso.with(mContext).load(urls.get(position)).into(imageView);

            //Cache TEST
            final String url = urls.get(position);
            final ImageView imView = imageView;
            Picasso.with(mContext)
                    .load(url)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(imView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Picasso", "Image loaded from cache>>>" + url);
                        }

                        @Override
                        public void onError() {
                            Log.d("Picasso", "Try again in ONLINE mode if load from cache is failed");
                            Picasso.with(mContext).load(url).into(imView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Log.d("Picasso", "Image loaded from web>>>" + url);
                                }

                                @Override
                                public void onError() {
                                    Log.d("Picasso", "Failed to load image online and offline, make sure you enabled INTERNET permission for your app and the url is correct>>>>>>>" + url);
                                }
                            });
                        }
                    });

//            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageView;
    }
}