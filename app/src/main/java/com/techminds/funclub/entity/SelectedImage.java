package com.techminds.funclub.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static com.google.android.gms.R.id.url;

/**
 * Created by TOS on 05/10/2016.
 */
public class SelectedImage {

    private String imageid;
    private String imgPath;
    private String postitionX;
    private String postitionY;
    private String imgType;
    private String hight;
    private String width;
    private String angel;
    private String imgUrl;
    private Bitmap bitmap;

    public SelectedImage() {}

    public SelectedImage(String imageid, String postitionX, String postitionY, String hight, String width, String angel, String imgUrl) {
        this.imageid = imageid;
        this.postitionX = postitionX;
        this.postitionY = postitionY;
        this.hight = hight;
        this.width = width;
        this.angel = angel;
        this.imgUrl = imgUrl;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getImgUrl() { return imgUrl; }

    public void setImgUrl(String url) {
        this.imgUrl = url;
    }

    public void setImgUrl(Context mContext, final String imgUrl) {
        this.imgUrl = imgUrl;

        Picasso.with(mContext)
                .load(url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                        /* Save the bitmap or do something with it here */

                        //Set it in the ImageView
                        setBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Log.i("BitmapLoadingFailed", " " + imgUrl);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.i("BitmapLoadingSuccessful", " " + imgUrl);
                    }
                });

    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getPostitionX() {
        return postitionX;
    }

    public void setPostitionX(String postitionX) {
        this.postitionX = postitionX;
    }

    public String getPostitionY() {
        return postitionY;
    }

    public void setPostitionY(String postitionY) {
        this.postitionY = postitionY;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getHight() {
        return hight;
    }

    public void setHight(String hight) {
        this.hight = hight;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getAngel() {
        return angel;
    }

    public void setAngel(String angel) {
        this.angel = angel;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return imageid+" "+imgPath +" "+ postitionX +" "+ postitionY +" "+ imgType+" "
                + hight+" " + width+" " + angel+" " + imgUrl;
    }

}
