package com.techminds.funclub.ui.fragment.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.techminds.funclub.R;
import com.techminds.funclub.adapter.FeedListAdapter;
import com.techminds.funclub.app.AppController;
import com.techminds.funclub.data.FeedItem;
import com.techminds.funclub.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by malik on 9/26/17.
 */

public class WallFragment extends BaseFragment {


    View view;
    FragmentActivity context;

    Button postButton;
    ImageButton postVideoButton, postImageButton;
    EditText statusEdittext;
    TextView usernametextview;
    ImageView userProfilePic;

    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 4;
    private static final int TAKE_VIDEO = 5;
    private static final String TAG = WallFragment.class.getSimpleName();
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;

    private String URL_FEED = "https://api.androidhive.info/feed/feed.json";
    private String selectedImagePath;
    String workFolder = null;
    String demoVideoFolder = null;
    String demoVideoPath = null;
    String vkLogPath = null;
    String filepath;
    String outputpath;

    private boolean commandValidationFailedFlag = false;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_wall, container, false);
        postButton = (Button) view.findViewById(R.id.postonwallbtn);
        postImageButton = (ImageButton) view.findViewById(R.id.postpicturebtn);
        postVideoButton = (ImageButton) view.findViewById(R.id.postvideobtn);
        statusEdittext = (EditText) view.findViewById(R.id.userstatusedittext);
        usernametextview = (TextView) view.findViewById(R.id.username);
        userProfilePic = (ImageView) view.findViewById(R.id.userprofilePic);
        listView = (ListView) view.findViewById(R.id.list);

        context = getActivity();


        feedItems = new ArrayList<FeedItem>();
        listAdapter = new FeedListAdapter(getActivity(), feedItems);
        listView.setAdapter(listAdapter);

        getNewsFeedfromWebServices();


        demoVideoFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/videokit/";
        demoVideoPath = demoVideoFolder + "in.mp4";



        /*
        * Post video Button
        * */

        postVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectVideo();
            }
        });




        /*
        * Post image wall Button
        * */

        postImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        /*
        * Post on wall Button
        * */

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "post Button Clicked", Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }


    public void getNewsFeedfromWebServices() {


        try {
            com.android.volley.Cache cache = (com.android.volley.Cache) AppController.getInstance().getRequestQueue().getCache();
            com.android.volley.Cache.Entry entry = cache.get(URL_FEED);


            // We first check for cached request

            if (entry != null) {
                // fetch the data from cache
                try {
                    String data = new String(entry.data, "UTF-8");
                    try {
                        parseJsonFeed(new JSONObject(data));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } else {
                // making fresh volley request and getting json
                JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                        URL_FEED, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(TAG, "Response: " + response.toString());
                        if (response != null) {
                            parseJsonFeed(response);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });

                // Adding request to volley request queue
                AppController.getInstance().addToRequestQueue(jsonReq);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    @Override
    protected void setViews() {

    }

    @Override
    protected void setOnclickListeners() {

    }

    @Override
    protected void changeFragmentNoHistory(String Tag, Fragment fragment) {

    }

    /*select image from Gallery and Camera for Post*/

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    pickImageFromCamera();
                } else if (items[item].equals("Choose from Library")) {
                    pickImageFromGallary();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    protected void pickImageFromGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
        Log.i("IntentTest()", "Pick Image From Galary");
    }

    protected void pickImageFromCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 2);
        Log.i("IntentTest()", "Pick Image From Camera");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = Utils.getPath(getActivity(), selectedImageUri);
//                decodeFile(selectedImagePath);
//                UserPrefs.setProfileImage(selectedImagePath, getActivity());
            } else if (requestCode == 2) {
                Bundle extras = data.getExtras();
                //get the cropped bitmap from extras
                Bitmap thePic = extras.getParcelable("data");
                //              Uri tempUri = getImageUri(getActivity(), thePic);
//                Log.i("ImagePathByCamera" , "Image Path By camera" + getRealPathFromURI(tempUri));
//                savePath(getRealPathFromURI(tempUri));
            } else if (requestCode == 3) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = Utils.getPath(getActivity(), selectedImageUri);
                //Log.i("ImagePathByGallery", selectedImagePath );
//                savePath(selectedImagePath);
            } else if (requestCode == TAKE_VIDEO) {

                try {

                    AssetFileDescriptor videoAsset = context.getContentResolver().openAssetFileDescriptor(data.getData(), "r"); //.openAssetFileDescriptor(data.getData(), "r");
                    FileInputStream fis = videoAsset.createInputStream();
                    File root = new File(Environment.getExternalStorageDirectory(), "/RecordVideo");

                    if (!root.exists()) {
                        root.mkdirs();
                    }

                    File file;
                    file = new File(root, "funclub" + System.currentTimeMillis() + ".mp4");

                    File outputfile = new File(root, "funclubCompressed" + System.currentTimeMillis() + ".mp4");

                    Log.i("Path", String.valueOf(file));
                    filepath = String.valueOf(file);
                    outputpath = String.valueOf(outputfile);

                    FileOutputStream fos = new FileOutputStream(file);

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = fis.read(buf)) > 0) {
                        fos.write(buf, 0, len);
                    }
                    fis.close();
                    fos.close();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
    }


    /*Select video from Gallery or Camera for Post*/
    // Selected Video will be compressed and then uploaded..!!!
    private void selectVideo() {
        final CharSequence[] items = {"Take Video", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Video")) {
                    pickVideoFromCamera();
                } else if (items[item].equals("Choose from Library")) {
                    pickVideoFromGallary();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void pickVideoFromGallary() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), 4);
    }

    private void pickVideoFromCamera() {
        Intent photoPickerIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Take Video"), TAKE_VIDEO);
    }




    /*
    * Parsing the Response from Web-Service.*/

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);


                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

