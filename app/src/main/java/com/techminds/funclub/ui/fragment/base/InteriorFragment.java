package com.techminds.funclub.ui.fragment.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.techminds.funclub.CustomMethods.JsonClass;
import com.techminds.funclub.R;
import com.techminds.funclub.colorpicker.ColorPickerDialog;
import com.techminds.funclub.entity.SelectedImage;
import com.techminds.funclub.ui.activity.DashBoardActivity;
import com.techminds.funclub.ui.activity.PictureGallary;
import com.techminds.funclub.ui.dialog.DialogBoxes;
import com.techminds.funclub.utils.Constants;
import com.techminds.funclub.utils.UserPrefs;
import com.techminds.funclub.utils.Utils;
import com.techminds.funclub.utils.config.SharedPrefSocialIcons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by waseemakram on 6/15/17.
 */

public class InteriorFragment extends BaseFragment implements View.OnClickListener {

    View view;
    ImageView backgroundColorRoom;
    ImageView tattoRoom;
    ImageView emojisRoom;
    ImageView cameraRoom;
    ImageView headboardsRoom;
    ImageView styleRoom;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    private List<Bitmap> bitmap;
    int initialColor = Color.WHITE;
    private DrawerLayout drawerLayout;
    FragmentManager mFragmentManager;
    private String jsonResult;
    String tattooroom ="Tattoo Room";
    String Emojis = "Emojis";
    String Style = "Style";
    String Headboard = "Headboards";

    DialogBoxes dialogBoxes;

    // Webservices

    String API_URL = "http://dev.technology-minds.com/funclub/manage/webservices/interior_design.php";
    String API_SERVER_BG_URL_POST = "http://dev.technology-minds.com/funclub/manage/webservices/background_color_insert.php";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_interiors,container,false);

        setViews();
        setOnclickListeners();

        Log.i("ImageSources()",Constants.ImageSources.toString());

        return  view;
    }

    @Override
    protected void setViews() {
        /**
         * Views Defined
         * */
        backgroundColorRoom = (ImageView)view.findViewById(R.id.pickColorImg);
        tattoRoom = (ImageView)view.findViewById(R.id.stickersImg);
        emojisRoom = (ImageView)view.findViewById(R.id.emojiImg);
        cameraRoom  = (ImageView)view.findViewById(R.id.photosImg);
        headboardsRoom = (ImageView)view.findViewById(R.id.textImg);
        styleRoom = (ImageView)view.findViewById(R.id.gallaryImg);
        /**
         * Bottom Slider views may or may not be defined below (Confirmation)
         * */

        /**
         * Dashboard Activity views
         * */
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer);
    }

    @Override
    protected void setOnclickListeners() {
        backgroundColorRoom.setOnClickListener(this);
        tattoRoom.setOnClickListener(this);
        emojisRoom.setOnClickListener(this);
        cameraRoom.setOnClickListener(this);
        cameraRoom.setOnClickListener(this);
        headboardsRoom.setOnClickListener(this);
        styleRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.pickColorImg:
                Log.i("onClick()"," pickColorImg");
                showColorPickerDialogDemo();
                break;

            case R.id.stickersImg:
                Log.i("onClick()", tattooroom);
                Constants.currentRoom = tattooroom;
                gotoRoom(tattooroom);
                startActivity(new Intent(getActivity(), PictureGallary.class).putExtra("activity", "tattooroom"));
                break;

            case R.id.emojiImg:
                Log.i("onClick()", Emojis);
                Constants.currentRoom = Emojis;
                gotoRoom(Emojis);
                startActivity(new Intent(getActivity(), PictureGallary.class).putExtra("activity", "emojis"));
                break;

            case R.id.photosImg:
                Log.i("onClick()", "photoImg");
                selectImage();
                break;

            case R.id.textImg:
                Log.i("onClick()", Headboard);
                Constants.currentRoom = Headboard;
                startActivity(new Intent(getActivity(), PictureGallary.class).putExtra("activity", "headboards"));
                break;

            case R.id.gallaryImg:
                Log.i("onClick()", Style);
                Constants.currentRoom = Style;
                gotoRoom(Style);
                startActivity(new Intent(getActivity(), PictureGallary.class).putExtra("activity", "style"));
                break;

            default:
                break;
        }
    }

    @Override
    protected void changeFragmentNoHistory(String Tag, Fragment fragment) {

    }

    private void showColorPickerDialogDemo() {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getActivity(), initialColor, new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                showToast(color);
            }

            private void showToast(int color) {
                String rgbString = "R: " + Color.red(color) + " B: " + Color.blue(color) + " G: " + Color.green(color);
                UserPrefs.setBackgroundColor(String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color)), getActivity());

                SharedPrefSocialIcons.putString(getActivity(),"bg_color",UserPrefs.getBackgroundColor(getActivity()));
                String bg_color = UserPrefs.getBackgroundColor(getActivity());
                String member_id = SharedPrefSocialIcons.getStrings(getActivity(),"member_id");

                bg_Call_method(member_id,bg_color);

                DashBoardFragment fragment = new DashBoardFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
            }
        });
        colorPickerDialog.show();
    }

    protected void pickProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

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
        Log.i("IntentTest()","Pick Image From Galary");
    }

    protected void pickImageFromCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 2);
        Log.i("IntentTest()","Pick Image From Camera");
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = Utils.getPath(getActivity(), selectedImageUri);
                decodeFile(selectedImagePath);
                UserPrefs.setProfileImage(selectedImagePath, getActivity());
            } else if (requestCode == 2) {
                Bundle extras = data.getExtras();
                //get the cropped bitmap from extras
                Bitmap thePic = extras.getParcelable("data");
                Uri tempUri = getImageUri(getActivity(), thePic);
                Log.i("ImagePathByCamera" , "Image Path By camera" + getRealPathFromURI(tempUri));
                savePath(getRealPathFromURI(tempUri));
            } else if (requestCode == 3) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = Utils.getPath(getActivity(), selectedImageUri);
                Log.i("ImagePathByGallery", selectedImagePath );
                savePath(selectedImagePath);
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void decodeFile(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        final int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap.clear();
        bitmap.add(BitmapFactory.decodeFile(filePath, o2));
       // profilePic.setImageBitmap(bitmap.get(0));
    }

    private void savePath(String imgPath) {
        List<SelectedImage> selectedImageList = new ArrayList<SelectedImage>();
        SelectedImage selectedImage = new SelectedImage();
        selectedImage.setImgPath(imgPath);
        selectedImage.setImgType("c");
        selectedImage.setPostitionX("50");
        selectedImage.setPostitionY("50");
        selectedImage.setImgType("gallary");
        selectedImage.setWidth("250");
        selectedImage.setHight("250");
        selectedImage.setAngel("0");

        Gson gson = new Gson();
        if (UserPrefs.getSelectedImage(getActivity()).length() > 0) {
            try {
                String data = UserPrefs.getSelectedImage(getActivity());
                Object json = new JSONTokener(data).nextValue();
                if (json instanceof JSONObject) {
                   // Log.i("jsonInstanceOf","Object length > 0");
                    JSONObject jsonObject = new JSONObject(data);
                    SelectedImage selectedImage1 = gson.fromJson(jsonObject.toString(), SelectedImage.class);
                    selectedImageList.add(selectedImage1);
                } else if (json instanceof JSONArray) {
                   // Log.i("jsonInstanceOf","ARRAY length > 0");
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                      //  Log.i("jsonInstanceOf","for");
                        SelectedImage selectedImage1 = gson.fromJson(jsonArray.getJSONObject(i).toString(), SelectedImage.class);
                        selectedImageList.add(selectedImage1);
                        sendSelectedImage(selectedImage1);
                    }
                    selectedImageList.add(selectedImage);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
           // Log.i("savePAth()", "else length == 1");
            selectedImageList.add(selectedImage);
        }

        String obj = gson.toJson(selectedImageList);
        UserPrefs.setSelectedImage(obj, getActivity());

        Intent intent = new Intent(getActivity(), DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Bg_Color webservices
     */


    /**
     * Backgroud webservices Insert / POST  ...
     * **/


    private void bg_Call_Post(String member_id,String bg_color){
        bg_Call_method(member_id,bg_color);
    }

    private void bg_Call_method(final String member_id, final String bg_color){
        class UserLoginClass extends AsyncTask<String, Void, String> {
//            ProgressDialog loading;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                dialogBoxes.showProgress(getActivity(), "Setting Dashboard Color", R.string.loading);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

//                dialogBoxes.dismiss();

                jsonResult = s;
                Log.i("JSONRESPONSEINTERIOR",jsonResult);
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String,String> data = new HashMap<>();
                data.put("member_id",params[0]);
                data.put("bgcolor",params[1]);



                String result = ruc.sendPostRequest(API_SERVER_BG_URL_POST,data);
                return result;

            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(member_id,bg_color);
    }



    /**
     *
     *
     * */


    private void gotoRoom(String CategoryInterior){
        room(CategoryInterior);
    }

    private void room(final String CategoryInterior) {
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();
                jsonResult = s;
                convertingResult();
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("category",params[0]);

                String result = ruc.sendPostRequest(API_URL, data);
                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(CategoryInterior);
    }

   public void convertingResult(){
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

           }

       } catch (JSONException e) {
           e.printStackTrace();
       }
   }


    String POST_STICKER_URL = "http://dev.technology-minds.com/funclub/manage/webservices/dashboard_insertnew.php";

    private void sendSelectedImage(SelectedImage selectedImage){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = getBitmapFromURL(selectedImage.getImgUrl());
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        } catch(NullPointerException e) {
            Log.e("sendSelectedImage()", e.toString());
            return;
        }catch (Exception e){
            e.printStackTrace();
        }

        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String member_id = Constants.member_id;
        String picture = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String xaxis = selectedImage.getPostitionX();
        String yaxis = selectedImage.getPostitionY();
        String width = selectedImage.getWidth();
        String height = selectedImage.getHight();
        String angle = selectedImage.getAngel();

        Log.i("POSTSelectedImageClass",member_id+" "+xaxis+" "+yaxis+" "+width+" "+height+" "+angle+" "+picture);

        POSTSelectedImage(member_id, picture, xaxis, yaxis, width, height, angle, selectedImage);
    }

    private void POSTSelectedImage(final String member_id, final String picture, final String xaxis, final String yaxis,
                                   final String width, final String height, final String angle, final SelectedImage selectedImage){
        class POSTSelectedImageClass extends AsyncTask<String, Void, String> {
//            ProgressDialog loading;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                dialogBoxes.showProgress(getActivity(), "Setting Dashboard Color", R.string.loading);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

//                dialogBoxes.dismiss();

                String jsonResult = s;
                Log.i("POSTSelectedImageClass", jsonResult);

                try {
                    JSONObject jsonObj = new JSONObject(jsonResult);
                    if(jsonObj.get("status").equals("1")) {
                        String replace="\\/";
                        String id = jsonObj.getString("id");
                        String url = jsonObj.getString("image").replaceAll(replace,"");
                        Log.i("POSTSelectedImageClaass",": "+url);
                        selectedImage.setImgUrl(getActivity(), url);
                        selectedImage.setImageid(id);
                    }
                } catch (JSONException e) {
                   // Log.i("JSONEXCEPTION"," at onPost");
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String,String> data = new HashMap<>();
                data.put("member_id",params[0]);
                data.put("picture",params[1]);
                data.put("xaxis",params[2]);
                data.put("yaxis",params[3]);
                data.put("width",params[4]);
                data.put("height",params[5]);
                data.put("angel",params[6]);

                String result = ruc.sendPostRequest(POST_STICKER_URL, data);
                return result;

            }
        }

        POSTSelectedImageClass ulc = new POSTSelectedImageClass();
        ulc.execute(member_id, picture, xaxis, yaxis, width, height, angle);
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

}
