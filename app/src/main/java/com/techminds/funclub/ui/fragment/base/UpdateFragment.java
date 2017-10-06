package com.techminds.funclub.ui.fragment.base;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.techminds.funclub.CustomMethods.JsonClass;
import com.techminds.funclub.R;
import com.techminds.funclub.model.bean.UserModel;
import com.techminds.funclub.ui.dialog.DialogBoxes;
import com.techminds.funclub.utils.ImageUtil;
import com.techminds.funclub.utils.ServiceUtil;
import com.techminds.funclub.utils.SnackBarUtils;
import com.techminds.funclub.utils.config.APIService;
import com.techminds.funclub.utils.config.AppConstant;
import com.techminds.funclub.utils.config.SessionManager;
import com.techminds.funclub.utils.config.SharedPrefSocialIcons;
import com.techminds.funclub.utils.config.Validations;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by waseemakram on 6/15/17.
 */

public class UpdateFragment extends BaseFragment implements ViewStub.OnClickListener,EasyPermissions.PermissionCallbacks {
    private static final int SETTINGS_SCREEN_PERM = 123;
    private static final int MEDIA_SCREEN_PERM = 124;
    View view;
    ImageUtil imageUtil;
    TypedFile file;
    //CallbackManager callbackManager;
    TextView tvUpdate, tvBack;
    EditText etEmail, etUserName, etPassword, etConformPassword, etName, etPhone;
    AutoCompleteTextView autGender;
    String[] genderFields = {"Male", "Female"};
    ImageView ivBack, ivProfilePic;
    Validations validations;
    String email, username, password, confromPassword, name, phone, gender;
    TypedFile imageFile;
    DialogBoxes dialogBoxes;
    private final String TAG = this.getClass().getSimpleName();
    SessionManager sessionManager;
    private boolean emailValid, passwordValid, conformPasswordValid, nameValid, usernameValid, phoneValid = false;
    Activity mContext;
    FragmentManager mFragmentManager;

//    String API_URL_FB = "http://dev.technology-minds.com/funclub/manage/webservices/facebooknew_register.php";
//    String API_URL_GMAIL = "http://dev.technology-minds.com/funclub/manage/webservices/gmail_register.php";
    String API_FB_GMAIL_UPDATE = "http://dev.technology-minds.com/funclub/manage/webservices/update_fb_gmail.php";
    String jsonResult;


    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState){

        view = inflater.inflate(R.layout.fragment_update, container, false);
        validations = new Validations();
        setViews();
        setOnClickListeners();
        mContext = getActivity();
        validations = new Validations();
        dialogBoxes = new DialogBoxes();
        sessionManager = new SessionManager(getActivity());
        imageUtil = new ImageUtil(getActivity());
        mFragmentManager = getFragmentManager();
        autGender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setGenderAdapter();
                return false;
            }
        });

        String type = SharedPrefSocialIcons.getStrings(getActivity(),"stype");
        if(type != null && (type.equals("FB") || type.equals("GMAIL")))
            setData();
        else
            populateUserData();

        if(type!=null && type.equals("FB")){

            etName.setText(SharedPrefSocialIcons.getStrings(getActivity(),"fbname"));
            etPhone.setText(SharedPrefSocialIcons.getStrings(getActivity(),"fbphone"));
            autGender.setText(SharedPrefSocialIcons.getStrings(getActivity(),"fbgender"));
        }else if(type!=null && type.equals("GMAIL")){
            etName.setText(SharedPrefSocialIcons.getStrings(getActivity(),"gmailname"));
            etPhone.setText(SharedPrefSocialIcons.getStrings(getActivity(),"gmailphone"));
            autGender.setText(SharedPrefSocialIcons.getStrings(getActivity(),"gmailgender"));

        }

        return view;
    }

    private void populateUserData() {
        if (null != sessionManager.getUserObject()) {
            UserModel userData = sessionManager.getUserObject();
            etEmail.setText(userData.getEmail().toString());
            etUserName.setText(userData.getUsername().toString());
            //            etPassword.setText(userData.getPassword().toString());
            etName.setText(userData.getFullname().toString());
            etPhone.setText(userData.getPhoneNumber().toString());
            autGender.setText(userData.getGender().toString());

          //  String pic = userData.getPicture();
            String pic = sessionManager.getUserObject().getPicture();

            try{

                if(pic.equals("http://dev.technology-minds.com/funclub/manage/webservices/members_profile_picture/")){

                }else{
                    Glide.with(this).load(pic)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivProfilePic);
                }

            }catch (Exception e){
                e.printStackTrace();
            }




        }
    }

    public void setData() {
        String email,photo,username, password, name, confromPassword, phone, gender,stype;

        String userId =  SharedPrefSocialIcons.getStrings(getActivity(),"suserID");
        if (userId != null && !userId.isEmpty()) {

            name    =  SharedPrefSocialIcons.getStrings(getActivity(),"sname");
            email   =  SharedPrefSocialIcons.getStrings(getActivity(),"semail");
            gender  =  SharedPrefSocialIcons.getStrings(getActivity(),"sgender");
            photo   =  SharedPrefSocialIcons.getStrings(getActivity(),"sphoto");
            stype   =  SharedPrefSocialIcons.getStrings(getActivity(),"stype");


            etPassword.setEnabled(false);
            etConformPassword.setEnabled(false);
            etConformPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password2, 0,0,0);
            etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password2, 0, 0, 0);

            if(email!= null && !email.isEmpty()){etEmail.setText(email);}
            if(name!= null && !name.isEmpty()){etName.setText(name);}
            if(gender!= null && !gender.isEmpty()){autGender.setText(gender);}
            if(photo!= null && !photo.isEmpty()) {
                Glide.with(this).load(photo)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivProfilePic);
            }
        }

    }

    protected void setViews() {
        etEmail = (EditText) view.findViewById(R.id.emailUpdate);
        etUserName = (EditText) view.findViewById(R.id.usernameUpdate);
        etPassword = (EditText) view.findViewById(R.id.password);
        etConformPassword = (EditText) view.findViewById(R.id.confrom_password);
        etName = (EditText) view.findViewById(R.id.nameUpdate);
        etPhone = (EditText) view.findViewById(R.id.phoneUpdate);
        tvUpdate = (TextView) view.findViewById(R.id.btnUpdate);
        autGender = (AutoCompleteTextView) view.findViewById(R.id.gender);
        ivProfilePic = (ImageView) view.findViewById(R.id.profileUpdatePic);

        autGender.setText("Male");
    }



    @Override
    protected void setOnclickListeners() {

    }

    @Override
    protected void changeFragmentNoHistory(String Tag, Fragment fragment) {
        mFragmentManager.beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }

    private void setOnClickListeners() {
        tvUpdate.setOnClickListener(this);
        autGender.setOnClickListener(this);
        ivProfilePic.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate: {
                String type = SharedPrefSocialIcons.getStrings(getActivity(),"stype");
                if(type!= null && ( type.equals("FB") || type.equals("GMAIL")))
                {
                    fb_gmail_Validations();

                }
               else{
                    validations();
                }
                break;
            }
            case R.id.gender: {
                setGenderAdapter();
                break;
            }
            case R.id.profileUpdatePic: {
                requestPermissions();
                break;
            }


        }
    }

    private void fb_gmail_Validations() {

        gender = autGender.getText().toString();
        phone = etPhone.getText().toString();
        name = etName.getText().toString();
       String userid;
        String type = SharedPrefSocialIcons.getStrings(getActivity(),"stype");

        if (name.trim().isEmpty()) {
            etName.setError(getResources().getString(R.string.error_name));
            nameValid = false;
        } else {
            nameValid = true;
        }
        if (nameValid && type.equals("FB")) {

            SharedPrefSocialIcons.putString(getActivity(),"fbphone",phone);
            SharedPrefSocialIcons.putString(getActivity(),"fbname",name);
            SharedPrefSocialIcons.putString(getActivity(),"fbgender",gender);

            userid = SharedPrefSocialIcons.getStrings(getActivity(),"uniqueidfb");
            fb_Login(userid,name,gender,phone);
           // Toast.makeText(getActivity(),"FB UPDATE METHOD",Toast.LENGTH_LONG).show();


        }

        if (nameValid && type.equals("GMAIL")) {

            SharedPrefSocialIcons.putString(getActivity(),"gmailphone",phone);
            SharedPrefSocialIcons.putString(getActivity(),"gmailname",name);
            SharedPrefSocialIcons.putString(getActivity(),"gmailgender",gender);

            userid = SharedPrefSocialIcons.getStrings(getActivity(),"uniqueidgmail");
            gmail_Login(userid,name,gender,phone);
          //  Toast.makeText(getActivity(),"GMAIL UPDATE METHOD",Toast.LENGTH_LONG).show();

            etName.setText(SharedPrefSocialIcons.getStrings(getActivity(),"gmailname"));
            etPhone.setText(SharedPrefSocialIcons.getStrings(getActivity(),"gmailphone"));
            autGender.setText(SharedPrefSocialIcons.getStrings(getActivity(),"gmailgender"));
        }
    }



    private void setGenderAdapter() {
        ArrayAdapter<String> genderAdapter = new ArrayAdapter(mContext, R.layout.simple_list_custom_item, genderFields);
        genderAdapter.setDropDownViewResource(R.layout.simple_list_custom_item);
        autGender.setAdapter(genderAdapter);
        autGender.showDropDown();
    }

    private void networkUpdate(String email, String password, String gender, String phone, String userName,
                               String name, TypedFile imageFile) {
        dialogBoxes.showProgress(getActivity(), "", R.string.loading);

        if (AppConstant.IS_DEBUG)
            Log.d(TAG, "email: " + email + " password: " + password + " fullName: " + name + " phone: " + phone
                    + " imageFile: " + imageFile);

        try {
            APIService api = ServiceUtil.restAdapter().create(APIService.class);
            api.update(sessionManager.getUserObject().getId(), email, password, name, phone, gender, "member", imageFile, userName, new Callback<UserModel>() {
                @Override
                public void success(UserModel userResponse, Response response) {

                    if (AppConstant.IS_DEBUG) {
                        String res = new Gson().toJson(userResponse);
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            Log.d(TAG, "jsonObject: " + jsonObject.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (userResponse.getStatus() == 1) {
                        try {
                            String res2 = new Gson().toJson(userResponse);
                            JSONObject obj = new JSONObject(res2);
                            try {
                                Log.i("userResponse()", String.valueOf(obj.getString("picture")));
                            } catch(JSONException f) {
                                Log.i("userResponse()", "No value for picture");
                                userResponse.setPicture(sessionManager.getUserObject().getPicture());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sessionManager.setUserObject(userResponse);
                        dialogBoxes.hideProgress();
                        changeFragmentNoHistory("DashBoard", new DashBoardFragment());

                    } else {
                        SnackBarUtils.showSnackBar(getActivity().findViewById(android.R.id.content), userResponse.getMessage(), Snackbar.LENGTH_SHORT);
                        dialogBoxes.hideProgress();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "failure: ", error);
                    SnackBarUtils.showSnackBar(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.error), Snackbar.LENGTH_SHORT);
                    dialogBoxes.hideProgress();
                }
            });
        } catch (Exception Exp) {
            Exp.printStackTrace();
        }
    }


    /**
     * Facebook Login
     **/

    private void fb_Login(String fbid, String fullname , String gender, String phone){

        fb_userLogin(fbid, fullname, gender, phone);
    }
    private void fb_userLogin(final String fbid,final String fullname ,final String gender,final String phone){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(getActivity(),"Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();
                jsonResult = s;

              //  Log.i("FB RESULT",s);



                //  convertingResult();
            }

            //in this pass id,fullname,phoneNumber,gender

            @Override
            protected String doInBackground(String... params) {

                HashMap<String,String> data = new HashMap<>();
                data.put("id",params[0]);
                data.put("fullname",params[1]);
                data.put("gender",params[2]);
                data.put("phoneNumber",params[3]);


                String result = ruc.sendPostRequest(API_FB_GMAIL_UPDATE,data);
                return result;

            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(fbid,fullname,gender,phone);
    }


    /**
     * Gmail Data sending methods are here
     *
     * **/


    private void gmail_Login(String gmailid, String fullname,String gender,String phone ){

        gmail_userLogin(gmailid,fullname,gender,phone);
    }
    private void gmail_userLogin(final String gmailid, final String fullname, final String gender , final String phone){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(getActivity(),"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();
                jsonResult = s;


             //   Log.i("GMAIL UPDATE ",s);


            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String,String> data = new HashMap<>();
                data.put("id",params[0]);
                data.put("fullname",params[1]);
                data.put("gender",params[2]);
                data.put("phoneNumber",params[3]);


                String result = ruc.sendPostRequest(API_FB_GMAIL_UPDATE,data);
                return result;

            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(gmailid,fullname,gender,phone);
    }

    private void validations() {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        confromPassword = etConformPassword.getText().toString();
        gender = autGender.getText().toString();
        phone = etPhone.getText().toString();
        name = etName.getText().toString();
        username = etUserName.getText().toString();

        if (phone.trim().isEmpty()) {
            etPhone.setError(getResources().getString(R.string.error_phone));
            phoneValid = false;
        } else {
            phoneValid = true;
        }
        if (name.trim().isEmpty()) {
            etName.setError(getResources().getString(R.string.error_name));
            nameValid = false;
        } else {
            nameValid = true;
        }
        if (username.trim().isEmpty()) {
            etUserName.setError(getResources().getString(R.string.error_user_name));
            usernameValid = false;
        } else {
            usernameValid = true;
        }
        if (!email.contains(".") || !email.contains("@")) {
            etEmail.setError(getResources().getString(R.string.error_email));
            emailValid = false;
        } else {
            emailValid = true;
        }
        if (email.trim().isEmpty()) {
            etEmail.setError(getResources().getString(R.string.error_email2));
            emailValid = false;
        } else {
            emailValid = true;
        }
        if (password.length() < 5 || password.equals("")) {
          //  etPassword.setError(getResources().getString(R.string.error_password));
            passwordValid = false;
        } else {
            passwordValid = true;
        }
        if (!password.equals(confromPassword)) {
            etConformPassword.setError(getResources().getString(R.string.error_re_password));
            conformPasswordValid = false;
        } else {
            conformPasswordValid = true;
        }
        if (emailValid && nameValid && usernameValid && phoneValid) {

            networkUpdate(email, password, gender, phone, username, name, imageFile);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (data != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    File compress_image = ImageUtil.storeImage(photo);
                    File new_file = ImageUtil.compressImage(compress_image.getPath());
                  //  Log.i("Method file", String.valueOf(new_file));
                    imageFile = new TypedFile("multipart/form-data", new_file);
                    //  Glide.with(getActivity()).invalidate(new_file);
                    Glide.with(this).load(new_file).into(ivProfilePic);
                } else {
                    Log.v("URI", data + "");
                }
                break;
            case 3:
                if (data != null) {
                    String selectedImagePath = ImageUtil.getPath(data.getData(), getActivity());
                    //  File file = new File(selectedImagePath);
                    File newFIle = ImageUtil.compressImage(selectedImagePath);
                 //   Log.i("Method file", String.valueOf(newFIle));
                    imageFile = new TypedFile("multipart/form-data", newFIle);
                    Glide.with(this).load(newFIle).into(ivProfilePic);
                } else {
                 //   Log.v("URI", data + "");
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    public  void chooseFrom(){
        final Dialog choose_pic;
        choose_pic = new Dialog(getActivity());
        choose_pic.requestWindowFeature(Window.FEATURE_NO_TITLE);
        choose_pic.setContentView(R.layout.dialog_choose_from);
        choose_pic.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        choose_pic.setCancelable(true);
        choose_pic.show();


        LinearLayout camera = (LinearLayout) choose_pic.findViewById(R.id.ll_camera);
        LinearLayout gallery = (LinearLayout) choose_pic.findViewById(R.id.ll_gallery);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);
                choose_pic.dismiss();
            }
        });


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), 3);
                choose_pic.dismiss();
            }
        });
    }
    @AfterPermissionGranted(MEDIA_SCREEN_PERM)
    private void requestPermissions() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            chooseFrom();

        } else {
            EasyPermissions.requestPermissions(this, "Permission Required", MEDIA_SCREEN_PERM, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d("", "onPermissionsGranted:" + requestCode + ":" + perms.size());
        DialogBoxes.chooseFrom(getActivity());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d("", "onPermissionsDenied:" + requestCode + ":" + perms.size());

    /*    if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.rationale_ask_again))
                    .setPositiveButton("OK")
                    .setNegativeButton("Cancel", null)
                    .setRequestCode(SETTINGS_SCREEN_PERM)
                    .build()
                    .show();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}

