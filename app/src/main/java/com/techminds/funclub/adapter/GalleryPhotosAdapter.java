package com.techminds.funclub.adapter;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.techminds.funclub.R;

/**
 * Created by TOS on 28/09/2016.
 */
public class GalleryPhotosAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imageUrls;
    private SparseBooleanArray mSparseBooleanArray;//Variable to store selected Images
    private DisplayImageOptions options;
    private ArrayList<String> assestImage;
    private boolean isCustomGalleryActivity;//Variable to check if gridview is to setup for Custom Gallery or not
    private AssetManager assetManager;
    public GalleryPhotosAdapter(Context context, ArrayList<String> imageUrls, boolean isCustomGalleryActivity, ArrayList<String> assestImage) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.assestImage = assestImage;
        this.isCustomGalleryActivity = isCustomGalleryActivity;
        mSparseBooleanArray = new SparseBooleanArray();

        assetManager = context.getAssets();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .resetViewBeforeLoading(true).cacheOnDisk(true)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .build();

            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int i) {
        return imageUrls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.gridview_item, viewGroup, false);//Inflate layout


        final ImageView imageView = (ImageView) view.findViewById(R.id.galleryImageView);


        if (!isCustomGalleryActivity)
            imageView.setVisibility(View.GONE);

           InputStream is = null;
            try {
                is = assetManager.open("newphotos/" + imageUrls.get(position));
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                if (bitmap != null) {
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                    imageView.setImageBitmap(resizedBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

       /*// }*//*else{
            ImageLoader.getInstance().displayImage("file://" + imageUrls.get(position), imageView, options);//Load Images over ImageView
        }*/



        return view;
    }

}
