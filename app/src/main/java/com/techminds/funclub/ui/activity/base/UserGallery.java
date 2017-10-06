package com.techminds.funclub.ui.activity.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.squareup.picasso.Picasso;
import com.techminds.funclub.R;
import com.techminds.funclub.entity.ImageModel;

import java.util.ArrayList;

/**
 * Created by mask on 8/26/17.
 */

public class UserGallery extends Activity {

//    private GridView mGridView;
    private myImageAdapter mAdapter;

    Gallery gallery;
    ImageView imageView;
    Context mContext;

    boolean photo = true;
    boolean video = false;
    boolean liveStreaming = false;

    ArrayList<ImageModel> photos = new ArrayList<>();
    ArrayList<ImageModel> videos = new ArrayList<>();
    ArrayList<ImageModel> liveStreams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mContext = this;

        loadPhotos();
        loadVideos();
        loadLiveStreams();

        imageView = (ImageView) findViewById(R.id.imageView);
        gallery = (Gallery) findViewById(R.id.gallery);

        mAdapter = new myImageAdapter(this);
        gallery.setAdapter(mAdapter);

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int i, long id) {
                Picasso.with(mContext).load(photos.get(i).getUrl()).into(imageView);
                int imagePosition = i + 1;
                Toast.makeText(getApplicationContext(), "You have selected image = " + imagePosition, Toast.LENGTH_LONG).show();
            }
        });

        final TextView photoBtn = (TextView) findViewById(R.id.photoBtn);
        final TextView videoBtn = (TextView) findViewById(R.id.videoBtn);
        final TextView liveBtn = (TextView) findViewById(R.id.liveBtn);

        final LinearLayout layout = (LinearLayout) findViewById(R.id.linerLayout);
        final RelativeLayout r2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
        final RelativeLayout r3 = (RelativeLayout) findViewById(R.id.relativeLayout3);
        final RelativeLayout bg = (RelativeLayout) findViewById(R.id.bg);


        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                photo = true;
                video = false;
                liveStreaming = false;

                Log.i("Color()", String.valueOf(R.color.photoColor));

                layout.setBackgroundColor(Color.parseColor("#007cbb"));
                r2.setBackgroundColor(Color.parseColor("#007cbb"));
                r3.setBackgroundColor(Color.parseColor("#007cbb"));
                bg.setBackgroundColor(Color.parseColor("#007cbb"));

            }
        });

        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                photo = false;
                video = true;
                liveStreaming = false;

                Log.i("Color()", String.valueOf(R.color.videoColor));

                layout.setBackgroundColor(Color.parseColor("#1cb05a"));
                r2.setBackgroundColor(Color.parseColor("#1cb05a"));
                r3.setBackgroundColor(Color.parseColor("#1cb05a"));
                bg.setBackgroundColor(Color.parseColor("#1cb05a"));

            }
        });

        liveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                photo = false;
                video = false;
                liveStreaming = true;

                Log.i("Color()", String.valueOf(R.color.liveColor));

                layout.setBackgroundColor(Color.parseColor("#f43535"));
                r2.setBackgroundColor(Color.parseColor("#f43535"));
                r3.setBackgroundColor(Color.parseColor("#f43535"));
                bg.setBackgroundColor(Color.parseColor("#f43535"));

            }
        });
    }

    public void loadPhotos() {

        photos.add(new ImageModel("1", "https://static.pexels.com/photos/46710/pexels-photo-46710.jpeg"));
        photos.add(new ImageModel("2", "http://www.essentialibiza.com/ecms/wp-content/uploads/2015/06/relaxing-resorts.jpg"));
        photos.add(new ImageModel("3", "https://s-media-cache-ak0.pinimg.com/236x/95/08/20/9508208f4794d2649f9af86016b506a5--pink-sunset-the-sunset.jpg"));
        photos.add(new ImageModel("4", "https://static.pexels.com/photos/46710/pexels-photo-46710.jpeg"));
        photos.add(new ImageModel("5", "http://www.essentialibiza.com/ecms/wp-content/uploads/2015/06/relaxing-resorts.jpg"));
        photos.add(new ImageModel("6", "https://s-media-cache-ak0.pinimg.com/236x/95/08/20/9508208f4794d2649f9af86016b506a5--pink-sunset-the-sunset.jpg"));

    }

    public void loadVideos() {

        videos.add(new ImageModel("1", "http://www.textalibrarian.com/mobileref/wp-content/uploads/2013/08/Video.jpg"));
        videos.add(new ImageModel("2", "http://www.graycell.ru/picture/big/plastinka.jpg"));
        videos.add(new ImageModel("3", "http://rateswizard.com/wp-content/uploads/2015/08/play-icon.png"));
        videos.add(new ImageModel("4", "https://cdn1.iconfinder.com/data/icons/transportation-white-with-multicolor-circle-back-1/2048/Deviation_Sign-256.png"));
        videos.add(new ImageModel("5", "http://www.graycell.ru/picture/big/plastinka.jpg"));
        videos.add(new ImageModel("6", "http://rateswizard.com/wp-content/uploads/2015/08/play-icon.png"));

    }

    public void loadLiveStreams() {

        liveStreams.add(new ImageModel("1", "https://static.pexels.com/photos/46710/pexels-photo-46710.jpeg"));
        liveStreams.add(new ImageModel("2", "http://www.graycell.ru/picture/big/plastinka.jpg"));
        liveStreams.add(new ImageModel("3", "http://rateswizard.com/wp-content/uploads/2015/08/play-icon.png"));
        liveStreams.add(new ImageModel("4", "https://s-media-cache-ak0.pinimg.com/236x/95/08/20/9508208f4794d2649f9af86016b506a5--pink-sunset-the-sunset.jpg"));
        liveStreams.add(new ImageModel("5", "http://www.graycell.ru/picture/big/plastinka.jpg"));
        liveStreams.add(new ImageModel("6", "http://www.essentialibiza.com/ecms/wp-content/uploads/2015/06/relaxing-resorts.jpg"));

    }


    public class myImageAdapter extends BaseAdapter {
        private Context mContext;

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mgalleryView = new ImageView(mContext);
            mgalleryView.setLayoutParams(new Gallery.LayoutParams(150, 150));
            mgalleryView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mgalleryView.setPadding(3, 3, 3, 3);

            if(photo)
                Picasso.with(mContext).load(photos.get(position).getUrl()).into(mgalleryView);
            else if(video)
                Picasso.with(mContext).load(videos.get(position).getUrl()).into(mgalleryView);
            else
                Picasso.with(mContext).load(liveStreams.get(position).getUrl()).into(mgalleryView);

            return mgalleryView;
        }

        public myImageAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return photos.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }
    }
}