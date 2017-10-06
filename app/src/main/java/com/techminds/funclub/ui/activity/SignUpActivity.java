package com.techminds.funclub.ui.activity;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;
import com.techminds.funclub.CustomMethods.JsonClass;
import com.techminds.funclub.R;
import com.techminds.funclub.model.bean.UserModel;
import com.techminds.funclub.singleton.VolleySingleton;
import com.techminds.funclub.ui.dialog.DialogBoxes;
import com.techminds.funclub.utils.ImageUtil;
import com.techminds.funclub.utils.ServiceUtil;
import com.techminds.funclub.utils.SnackBarUtils;
import com.techminds.funclub.utils.config.APIService;
import com.techminds.funclub.utils.config.AppConstant;
import com.techminds.funclub.utils.config.SessionManager;
import com.techminds.funclub.utils.config.SharedPrefSocialIcons;
import com.techminds.funclub.utils.config.Validations;
import com.techminds.funclub.webServices.WEB_SERVICES_LINKS;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final int SETTINGS_SCREEN_PERM = 123;
    private static final int MEDIA_SCREEN_PERM = 124;

    SignUpActivity context;

    ImageUtil imageUtil;

    String photoBase64 = "null";

    // Information icon IMAGEVIEWS
    ImageView emailinfo, usernameinfo, passwordinfo, confirmpasswordinfo, nameinfo, phoneinfo;
    // CallbackManager callbackManager;
    TextView tvRegister, tvBack;
    EditText etEmail, etUserName, etPassword, etConformPassword, etName, etPhone;
    AutoCompleteTextView autGender;
    String[] genderFields = {"Male", "Female"};
    ImageView ivBack, ivProfilePic;
    Validations validations;
    String email, username, password, name, confromPassword, phone, gender;
    TypedFile imageFile;
    DialogBoxes dialogBoxes;
    private final String TAG = this.getClass().getSimpleName();
    SessionManager sessionManager;
    private boolean emailValid, passwordValid, conformPasswordValid, nameValid, usernameValid, phoneValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = this;
        validations = new Validations();
        setViews();
        setOnClickListeners();
        validations = new Validations();
        dialogBoxes = new DialogBoxes();
        sessionManager = new SessionManager(this);
        imageUtil = new ImageUtil(this);
        autGender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setGenderAdapter();
                return false;
            }
        });
        setData();


    }

    public void setData() {
        Bundle extras = getIntent().getExtras();
        String email, photo, username, password, name, confromPassword, phone, gender;
        if (extras != null) {
            email = extras.getString("email");
            name = extras.getString("name");
            gender = extras.getString("gender");
            photo = extras.getString("photo");
            if (email != null && !email.isEmpty()) {
                etEmail.setText(email);
            }
            if (name != null && !name.isEmpty()) {
                etName.setText(name);
            }
            if (gender != null && !gender.isEmpty()) {
                autGender.setText(gender);
            }
            if (photo != null && !photo.isEmpty()) {
                Glide.with(this).load(photo)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivProfilePic);
            }
        }

    }

    private void setViews() {
        etEmail = (EditText) findViewById(R.id.email);
        etUserName = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);
        etConformPassword = (EditText) findViewById(R.id.confrom_password);
        etName = (EditText) findViewById(R.id.name);
        etPhone = (EditText) findViewById(R.id.phone);
        tvRegister = (TextView) findViewById(R.id.btnRegister);
        autGender = (AutoCompleteTextView) findViewById(R.id.gender);
        ivBack = (ImageView) findViewById(R.id.btnBack);
        ivProfilePic = (ImageView) findViewById(R.id.profileUpdatePic);
        autGender.setText("Male");


        // Information icons imageviews

        emailinfo = (ImageView) findViewById(R.id.emailinfo);
        usernameinfo = (ImageView) findViewById(R.id.usernameinfo);
        passwordinfo = (ImageView) findViewById(R.id.passwordinfo);
        confirmpasswordinfo = (ImageView) findViewById(R.id.confirmation_codeinfo);
        nameinfo = (ImageView) findViewById(R.id.nameinfo);
        phoneinfo = (ImageView) findViewById(R.id.phoneinfo);

    }

    private void setOnClickListeners() {
        tvRegister.setOnClickListener(this);
        autGender.setOnClickListener(this);
        ivProfilePic.setOnClickListener(this);
        ivBack.setOnClickListener(this);


        //imageviews callbacks
        emailinfo.setOnClickListener(this);
        usernameinfo.setOnClickListener(this);
        passwordinfo.setOnClickListener(this);
        confirmpasswordinfo.setOnClickListener(this);
        nameinfo.setOnClickListener(this);
        phoneinfo.setOnClickListener(this);

    }

    /**
     * Dialog boxes for the Information imageviews
     **/

    public void createCustomDialogemailinfo() {


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_signup_information);
        TextView signuptextView = (TextView) dialog.findViewById(R.id.text_desp);
        Button buttonOK = (Button) dialog.findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        signuptextView.setText(getString(R.string.emailinformation));
        dialog.show();

    }

    public void createCustomDialogusernameinfo() {


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_signup_information);
        TextView signuptextView = (TextView) dialog.findViewById(R.id.text_desp);
        Button buttonOK = (Button) dialog.findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        signuptextView.setText(getString(R.string.usernameinformation));
        dialog.show();

    }

    public void createCustomDialogpasswordinfo() {


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_signup_information);
        TextView signuptextView = (TextView) dialog.findViewById(R.id.text_desp);
        Button buttonOK = (Button) dialog.findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        signuptextView.setText(getString(R.string.passwordinformation));
        dialog.show();

    }

    public void createCustomDialogconfirmpasswordinfo() {


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_signup_information);
        TextView signuptextView = (TextView) dialog.findViewById(R.id.text_desp);
        Button buttonOK = (Button) dialog.findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        signuptextView.setText(getString(R.string.confirminformation));
        dialog.show();

    }

    public void createCustomDialognameinfo() {


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_signup_information);
        TextView signuptextView = (TextView) dialog.findViewById(R.id.text_desp);
        Button buttonOK = (Button) dialog.findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        signuptextView.setText(getString(R.string.nameinformation));
        dialog.show();

    }

    public void createCustomDialogphoneinfo() {


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_signup_information);
        TextView signuptextView = (TextView) dialog.findViewById(R.id.text_desp);
        Button buttonOK = (Button) dialog.findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        signuptextView.setText(getString(R.string.phoneinformation));
        dialog.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister: {
                validations();
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

            case R.id.btnBack: {
                finish();
                break;
            }


            case R.id.emailinfo: {
                createCustomDialogemailinfo();

                break;
            }

            case R.id.usernameinfo: {
                createCustomDialogusernameinfo();
                break;
            }

            case R.id.passwordinfo: {
                createCustomDialogpasswordinfo();
                break;
            }

            case R.id.confirmation_codeinfo: {
                createCustomDialogconfirmpasswordinfo();
                break;
            }

            case R.id.nameinfo: {
                createCustomDialognameinfo();
                break;
            }
            case R.id.phoneinfo: {
                createCustomDialogphoneinfo();
                break;
            }


        }
    }

    private void setGenderAdapter() {
        ArrayAdapter<String> genderAdapter = new ArrayAdapter(this, R.layout.simple_list_custom_item, genderFields);
        genderAdapter.setDropDownViewResource(R.layout.simple_list_custom_item);
        autGender.setAdapter(genderAdapter);
        autGender.showDropDown();
    }

    public void showThankYouPopup() {
        DialogBoxes.dialogThankyou(this);
    }

        private void networkRegister(String email, String username, String password, String gender, String phone, String type,
                                 String name, TypedFile imageFile) {

        dialogBoxes.showProgress(this, "", R.string.loading);

        if (AppConstant.IS_DEBUG)
            Log.d(TAG, "email: " + email + " password: " + password + " fullName: " + name + " phone: " + phone
                    + " imageFile: " + imageFile + "type :" +type);

        try {
            APIService api = ServiceUtil.restAdapter().create(APIService.class);

            api.signUp(email, username, password, gender, phone, type, name, imageFile, new Callback<UserModel>() {
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


                        try {

                            JSONObject jsonObject = new JSONObject(res);
                            System.out.println("responseisthis"+jsonObject);
                            System.out.println("responseisthis"+res);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            Log.i("message()",message);
                            Log.i("response",jsonResult);

                            if (status.equals("1")) {

                                DialogBoxes.dialogThankyou(context);
                            } else if (status.equals("0")) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }

                    if (userResponse.getStatus() == 1) {
                        dialogBoxes.hideProgress();
                        SnackBarUtils.showSnackBar(findViewById(android.R.id.content), userResponse.getMessage(), Snackbar.LENGTH_SHORT);
                       // sessionManager.setUserObject(userResponse);
                        showThankYouPopup();


                    } else {
                        SnackBarUtils.showSnackBar(findViewById(android.R.id.content), userResponse.getMessage(), Snackbar.LENGTH_SHORT);
                        dialogBoxes.hideProgress();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "failure: ", error);
                    SnackBarUtils.showSnackBar(findViewById(android.R.id.content), getResources().getString(R.string.error), Snackbar.LENGTH_SHORT);
                    dialogBoxes.hideProgress();
                }
            });

        } catch (Exception Exp) {
            Exp.printStackTrace();
        }
    }

    private void validations() {


        email = etEmail.getText().toString();
        username = etUserName.getText().toString();
        password = etPassword.getText().toString();
        confromPassword = etConformPassword.getText().toString();
        gender = autGender.getText().toString();
        phone = etPhone.getText().toString();
        name = etName.getText().toString();

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
            etPassword.setError(getResources().getString(R.string.error_password));
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
        if (emailValid && passwordValid && conformPasswordValid && nameValid && usernameValid && phoneValid) {
            signup_Method(email, username, password, gender, phone, "member", name, photoBase64);
        }
    }


    String jsonResult;


    private void signup_Method(String email, String username, String password, String gender, String phone, String type,
                               String name, String photoBase64) {

        signup_Method_asyn(email, username, password, gender, phone, type, name, photoBase64);
    }

    private void signup_Method_asyn(final String email, final String username, final String password, final String gender, final String phone, final String type,
                                    final String name, final String photoBase64) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(context, "Please Wait", null, true, true);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();
                jsonResult = s;
                System.out.println("Resultisthis"+jsonResult);

                try {
                    JSONObject jsonObject = new JSONObject(jsonResult);
                    System.out.println("Resultisthis"+jsonObject);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    Log.i("message()",message);
                    Log.i("response",jsonResult);

                    if (status.equals("1")) {

                        DialogBoxes.dialogThankyou(context);
                    } else if (status.equals("0")) {
                        Toast.makeText(context,"Email or username already Exists", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();
                data.put("email", params[0]);
                data.put("userName", params[1]);
                data.put("password", params[2]);
                data.put("gender", params[3]);
                data.put("phoneNumber", params[4]);
                data.put("type", params[5]);
                data.put("fullName", params[6]);
                data.put("picture", params[7]);

                String result = ruc.sendPostRequest(WEB_SERVICES_LINKS.API_Signup_Web_Services, data);
                return result;

            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(email, username, password, gender, phone, type, name, photoBase64);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:

                if (data != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    File compress_image = ImageUtil.storeImage(photo);
                    File new_file = ImageUtil.compressImage(compress_image.getPath());
                    imageFile = new TypedFile("multipart/form-data", new_file);


                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    photoBase64 = encodeTobase64(photo);
                    Log.i("base64Photo()", photoBase64);

                    //  Glide.with(getActivity()).invalidate(new_file);
                    Glide.with(this).load(new_file).into(ivProfilePic);
                } else {
                   // Log.v("URI", data + "");
                }
                break;
            case 1:
                if (data != null) {
                    String selectedImagePath = ImageUtil.getPath(data.getData(), this);
                    //  File file = new File(selectedImagePath);
                    File newFIle = ImageUtil.compressImage(selectedImagePath);
                    imageFile = new TypedFile("multipart/form-data", newFIle);
                    Glide.with(this).load(newFIle).into(ivProfilePic);

                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                    photoBase64 =  encodeTobase64(bitmap);
                    Log.i("base64Photo()", photoBase64);

                } else {
                   // Log.v("URI", data + "");
                }
                break;
        }

    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

     //   Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    @AfterPermissionGranted(MEDIA_SCREEN_PERM)
    private void requestPermissions() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // DoctorSignup();
            DialogBoxes.chooseFrom(this);
        } else {
            EasyPermissions.requestPermissions(this, "Permission Required", MEDIA_SCREEN_PERM, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d("", "onPermissionsGranted:" + requestCode + ":" + perms.size());
        DialogBoxes.chooseFrom(this);
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


    private void Sign_up_Volley_Method(final String email, final String userName, final String password, final String gender,
                                       final String phoneNumber, final String type , final String fullName , final String photobase64) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait");
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WEB_SERVICES_LINKS.API_Signup_Web_Services,
                new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        String result = response;
                        System.out.println("Response()"+result);



                        try {

                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            Log.i("message()",message);
                            Log.i("response",result);

                            if (status.equals("1")) {

                                DialogBoxes.dialogThankyou(context);
                            } else if (status.equals("0")) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                        }






                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<>();
//                params.put("member_id", member_id);
//                params.put("post_description", post_Description);
//                params.put("picture", picture);
//                params.put("video", video);

                params.put("email",email);
                params.put("userName",userName);
                params.put("password",password);
                params.put("gender",gender);
                params.put("phoneNumber",phoneNumber);
                params.put("type",type);
                params.put("fullName",fullName);
                params.put("picture",photobase64);



//                data.put("email", params[0]);
//                data.put("userName", params[1]);
//                data.put("password", params[2]);
//                data.put("gender", params[3]);
//                data.put("phoneNumber", params[4]);
//                data.put("type", params[5]);
//                data.put("fullName", params[6]);
//                data.put("picture", params[7]);



                //Bank Information

                return params;
            }


        };


        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }



}