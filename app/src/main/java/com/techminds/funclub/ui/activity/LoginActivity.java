package com.techminds.funclub.ui.activity;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.techminds.funclub.CustomMethods.JsonClass;
import com.techminds.funclub.R;
import com.techminds.funclub.Singleton.MySingleton;
import com.techminds.funclub.webServices.WEB_SERVICES_LINKS;
import com.techminds.funclub.model.bean.UserModel;
import com.techminds.funclub.ui.dialog.DialogBoxes;
import com.techminds.funclub.utils.ServiceUtil;
import com.techminds.funclub.utils.SnackBarUtils;
import com.techminds.funclub.utils.config.APIService;
import com.techminds.funclub.utils.config.AppConstant;
import com.techminds.funclub.utils.config.SessionManager;
import com.techminds.funclub.utils.config.SharedPrefSocialIcons;
import com.techminds.funclub.utils.config.Validations;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    EditText edUsername, edPassword;
    TextView tvForgotPassword, tvLogIn, tvSignUp;
    ImageView btnFbLogIn, btnGoogleLogIn, btnShare;
    Validations validations;
    boolean usernameValid, passwordValid = false;
    DialogBoxes dialogBoxes;
    private final String TAG = this.getClass().getSimpleName();
    SessionManager sessionManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    CallbackManager callbackManager;
    LoginButton facebookLoginButton;


    private ProgressDialog mProgressDialog;


    //TEST
    String rooms[] = {"TaToo room", "Emojis", "Style", "Headborads"};
    String token;
    String jsonResult;

    LoginActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        setViews();
        setOnClickListeners();
        validations = new Validations();
        dialogBoxes = new DialogBoxes();
        sessionManager = new SessionManager(this);
        if (null != sessionManager.getUserObject()) {
            startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
            finish();
        }

        String userId = SharedPrefSocialIcons.getStrings(context, "suserID");
        if (userId != null && !userId.isEmpty()) {
            startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
            finish();

        }


        /**
         * New methods for google Sign in
         * */
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

    }

    private void setOnClickListeners() {
        tvLogIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        btnFbLogIn.setOnClickListener(this);
        btnGoogleLogIn.setOnClickListener(this);
//        btnShare.setOnClickListener(this);
    }

    private void setViews() {
        edUsername = (EditText) findViewById(R.id.userName);
        edPassword = (EditText) findViewById(R.id.password);
        tvLogIn = (TextView) findViewById(R.id.btnlogin);
        tvSignUp = (TextView) findViewById(R.id.btnsignup);
        tvForgotPassword = (TextView) findViewById(R.id.forgotpassword);
        //  btnShare = (ImageView) findViewById(R.id.share);
        btnFbLogIn = (ImageView) findViewById(R.id.fb);
        btnGoogleLogIn = (ImageView) findViewById(R.id.google);
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton = (LoginButton) findViewById(R.id.loginButton_facebook);
    }


    public void createCustomDialog() {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_un_active);
        Button dialogButton = (Button) dialog.findViewById(R.id.btnOk);
        Button resendButton = (Button) dialog.findViewById(R.id.btnResend);
        // if button is clicked, close the custom dialog

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        resendButton.setOnClickListener(this);

        dialog.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogin:
                validations();
                break;
            case R.id.btnsignup:
                signUp();
                break;
            case R.id.forgotpassword:
                DialogBoxes.dialogForgotPassword(this);
                break;
            case R.id.fb:
                socialIconClick(this, "FB");
                break;
            case R.id.google:
                signIn();
                break;
            case R.id.btnResend:
                DialogBoxes.dialogResendActivationLink(this);
        }
    }

    /***
     * Gmail Sign In Method
     * */

    private void validations() {
        usernameValid = validations.isValidText(edUsername, "Username");
        passwordValid = validations.isValidText(edPassword, "Password");
        if (usernameValid && passwordValid)
            networkLogin(edUsername.getText().toString(), edPassword.getText().toString());
    }

    public void socialIconClick(final Activity activity, final String type) {

        final Dialog forgotpassword = new Dialog(activity);
        forgotpassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotpassword.setContentView(R.layout.dilog_social);
        forgotpassword.setCancelable(true);
        forgotpassword.show();

        Button login = (Button) forgotpassword.findViewById(R.id.btnSocialLogin);
        final Button share = (Button) forgotpassword.findViewById(R.id.btnSocialShare);
        login.setText("Login with " + type);
        share.setText("Share on " + type);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case "FB": {
                        facebookLoginButton.performClick();
                        facebookSignupMethod();
                        forgotpassword.dismiss();
                        break;
                    }
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case "FB": {
           /*             Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,"Hey, there is an exciting app to join with the name of Fun Club, Lets connect together through fun club for unlimited entertainment!");
                        sendIntent.setType("text/plain");
                        sendIntent.setPackage("com.facebook.katana");
                        startActivity(sendIntent);*/

                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, there is an exciting app to join with the name of Fun Club, Lets connect together through fun club for unlimited entertainment!");
                        PackageManager pm = getPackageManager();
                        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
                        for (final ResolveInfo app : activityList) {
                            if ((app.activityInfo.name).contains("facebook")) {
                                final ActivityInfo activity = app.activityInfo;
                                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                shareIntent.setComponent(name);
                                try {
                                    startActivity(shareIntent);
                                } catch (ActivityNotFoundException e) {
                                    SnackBarUtils.showSnackBar(findViewById(android.R.id.content), "FB not found!", Snackbar.LENGTH_SHORT);

                                }

                                break;
                            } else {
                                SnackBarUtils.showSnackBar(findViewById(android.R.id.content), "FB not found!", Snackbar.LENGTH_SHORT);
                            }
                        }


                        forgotpassword.dismiss();
                        break;
                    }
                    case "Google+": {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, "Hey, there is an exciting app to join with the name of Fun Club, Lets connect together through fun club for unlimited entertainment!");
                        intent.setType("text/plain");
                        intent.setPackage("com.google.android.apps.plus");
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            SnackBarUtils.showSnackBar(findViewById(android.R.id.content), "G+ not found!", Snackbar.LENGTH_SHORT);

                        }

                        forgotpassword.dismiss();
                        break;
                    }
                }
            }
        });
    }

    /**
     * Facebook Sign In method
     **/
    private void facebookSignupMethod() {
        facebookLoginButton.setReadPermissions(Arrays.asList("public_profile", "email",
                "user_birthday", "user_friends", "user_photos", "user_location"));

        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                AccessToken.getCurrentAccessToken().getToken();

                loginResult.getAccessToken().getUserId();
                loginResult.getAccessToken().getToken();


//                Intent intent = new Intent(getApplicationContext(),DashBoardActivity.class);
//                startActivity(intent);

                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (object != null) {
                            try {
                                String email, photo, username, password, name, confromPassword, phone, gender, userId;

                                String url = object.getJSONObject("picture")
                                        .getJSONObject("data").getString("url");

                                String loginType = "FB";
                                userId = loginResult.getAccessToken().getUserId();
                                name = object.getString("name");
                                email = object.getString("email");
                                photo = url;
                                gender = object.getString("gender");


                                Intent i = new Intent(getApplicationContext(), DashBoardActivity.class);
                                i.putExtra("name", name);
                                i.putExtra("email", email);
                                i.putExtra("photo", photo);
                                i.putExtra("gender", gender);
                                i.putExtra("type", loginType);

                                SharedPrefSocialIcons.putString(context, "suserID", userId);
                                SharedPrefSocialIcons.putString(context, "sname", name);
                                SharedPrefSocialIcons.putString(context, "semail", email);
                                SharedPrefSocialIcons.putString(context, "sphoto", photo);
                                SharedPrefSocialIcons.putString(context, "sgender", gender);
                                SharedPrefSocialIcons.putString(context, "stype", loginType);

                                token = FirebaseInstanceId.getInstance().getToken();
                                Log.i("token",token);

                                //Log.i("member_id", SharedPrefSocialIcons.getStrings(LoginActivity.this, "member_id"));
//                                requestDashboardStickers(SharedPrefSocialIcons.getStrings(LoginActivity.this, "member_id"));
                                send_Firebase_Token();

                                fb_Login(userId, email, photo, name, gender);

                                startActivity(i);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {

                            Toast.makeText(LoginActivity.this, "No data found", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "picture.type(large),id,age_range,name,locale,link,email,gender,birthday,location,first_name,last_name");


                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                SnackBarUtils.showSnackBar(findViewById(android.R.id.content), "Request cancelled by the user", Snackbar.LENGTH_LONG);

            }

            @Override
            public void onError(FacebookException error) {
                SnackBarUtils.showSnackBar(findViewById(android.R.id.content), error.toString(), Snackbar.LENGTH_LONG);


            }
        });

    }


    private void shareVia() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, there is an exciting app to join with the name of Fun Club, Lets connect together through fun club for unlimited entertainment!");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Via"));
    }

    private void networkLogin(String username, String password) {
        dialogBoxes.showProgress(this, "", R.string.loading);

        if (AppConstant.IS_DEBUG)
         //   Log.d("", "username: " + username + " password: " + password);

        try {
            APIService api = ServiceUtil.restAdapter().create(APIService.class);

            api.login(username, password, new Callback<UserModel>() {
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
                        SharedPrefSocialIcons.putString(context, "member_id", userResponse.getId());
                        sessionManager.setUserObject(userResponse);
                        token = FirebaseInstanceId.getInstance().getToken();


                        send_Firebase_Token();
                      //  bg_Call(userResponse.getId());
                        startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                        finish();
                    } else if (userResponse.getStatus() == 0) {
                        if (userResponse.getMessage().equals("Invalid Username Or Password")) {
                            SnackBarUtils.showSnackBar(findViewById(android.R.id.content),
                                    "Invalid Username Or Password", Snackbar.LENGTH_SHORT);
                            dialogBoxes.hideProgress();
                        }

                        if (userResponse.getMessage().equals("Your account is inactive at the moment, please contact to F.C Admin for further information")) {
                            createCustomDialog();
                            dialogBoxes.hideProgress();
                        }
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "failure: ", error);
                    SnackBarUtils.showSnackBar(findViewById(android.R.id.content), getResources().getString(R.string.erroroccured), Snackbar.LENGTH_SHORT);
                    dialogBoxes.hideProgress();
                }
            });
        } catch (Exception Exp) {
            Exp.printStackTrace();
        }
    }


    private void signUp() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    /****
     *
     * Fb data sending methods are  here
     * */

    private void fb_Login(String fbid, String email, String picture, String fullname, String gender) {

        fb_userLogin(fbid, email, picture, fullname, gender);

    }

    private void fb_userLogin(final String fbid, final String email, final String picture, final String fullname, final String gender) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
//            ProgressDialog loading;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

//                loading = ProgressDialog.show(context,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

//                loading.dismiss();
                jsonResult = s;

                try {
                    JSONObject jsonObject = new JSONObject(jsonResult);
                    String id = jsonObject.getString("id");
                    //     Log.i("printingid ",id);
                    SharedPrefSocialIcons.putString(context, "uniqueidfb", id);
                    SharedPrefSocialIcons.putString(context, "member_id", id);

                 //   bg_Call(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();
                data.put("fbid", params[0]);
                data.put("email", params[1]);
                data.put("picture", params[2]);
                data.put("fullname", params[3]);
                data.put("gender", params[4]);


                String result = ruc.sendPostRequest(WEB_SERVICES_LINKS.API_SERVER_URL_FB, data);
                return result;

            }
        }

        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(fbid, email, picture, fullname, gender);
    }

    /**
     * Gmail Data sending methods are here
     **/

    private void gmail_Login(String fbid, String email, String picture, String fullname) {

        gmail_userLogin(fbid, email, picture, fullname);
    }

    private void gmail_userLogin(final String fbid, final String email, final String picture, final String fullname) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
//            ProgressDialog loading;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

//                loading = ProgressDialog.show(context,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

//                loading.dismiss();
                jsonResult = s;


                try {
                    JSONObject jsonObject = new JSONObject(jsonResult);
                    String id = jsonObject.getString("id");
                    //  Log.i("printingid ",id);
                    SharedPrefSocialIcons.putString(context, "uniqueidgmail", id);
                    SharedPrefSocialIcons.putString(context, "member_id", id);

                    try {

                        Log.i("Gmail member id", SharedPrefSocialIcons.getStrings(context, "member_id"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                //    bg_Call(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();
                data.put("gmailid", params[0]);
                data.put("email", params[1]);
                data.put("picture", params[2]);
                data.put("fullname", params[3]);


                String result = ruc.sendPostRequest(WEB_SERVICES_LINKS.API_SERVER_URL_GMAIL, data);
                return result;

            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(fbid, email, picture, fullname);
    }


    /**
     * Firebase Token Webservice Call here
     **/

    private void send_Firebase_Token() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEB_SERVICES_LINKS.API_SERVER_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("device_id", token);
               // Log.i("tokenId", token);

                return params;
            }
        };

        MySingleton.getmInstance(LoginActivity.this).addtoRequestQueue(stringRequest);
       // Log.i("requesting()", " Dashboard Stickers");
    }

    /**
     * new sign in Methods for the google
     **/

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RC_SIGN_IN:
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handleSignInResult(result);
                    dialogBoxes.hideProgress();
                    break;

                default:
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        } else {
            SnackBarUtils.showSnackBar(findViewById(android.R.id.content), "Try Again!", Snackbar.LENGTH_SHORT);
          /* if(this.getApplicationContext() != null) {
               dialogBoxes.hideProgress();
           }*/
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        dialogBoxes.hideProgress();
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


            Intent i = new Intent(getApplicationContext(), DashBoardActivity.class);
            String loginType = "GMAIL";
            Log.e(TAG, "display name: " + acct.getDisplayName());
            String name = acct.getDisplayName();
            String photo = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            String userId = acct.getId();
            String gender = "Male";

            //    Log.i("gmailuserID",userId);

            SharedPrefSocialIcons.putString(context, "suserID", userId);
            SharedPrefSocialIcons.putString(context, "sname", name);
            SharedPrefSocialIcons.putString(context, "semail", email);
            SharedPrefSocialIcons.putString(context, "sphoto", photo);
            SharedPrefSocialIcons.putString(context, "sgender", gender);
            SharedPrefSocialIcons.putString(context, "stype", loginType);

            token = FirebaseInstanceId.getInstance().getToken();
            send_Firebase_Token();

            //    Log.i("member_id", SharedPrefSocialIcons.getStrings(this, "member_id"));
//            requestDashboardStickers(SharedPrefSocialIcons.getStrings(this, "member_id"));

            gmail_Login(userId, email, photo, name);
            startActivity(i);

        } else {
            // Signed out, show unauthenticated UI.

        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        dialogBoxes.showProgress(this, "", R.string.loading);
    }

    private void bg_Call(String member_id) {
        bg_Call_method(member_id);
    }

    private void bg_Call_method(final String member_id) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context, "Loading Dashboard Content", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                jsonResult = s;
                Log.i("JSONRESPONSE", jsonResult);

                try {
                    if (jsonResult.equals("[]"))
                        SharedPrefSocialIcons.putString(context, "bg_color", "null");
                    JSONObject jsonObject = new JSONObject(jsonResult);
                    String bg_color = jsonObject.getString("bgcolor");
                    if (bg_color != null && !bg_color.isEmpty())
                        SharedPrefSocialIcons.putString(context, "bg_color", bg_color);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("member_id", params[0]);

                String result = ruc.sendPostRequest(WEB_SERVICES_LINKS.API_SERVER_BG_URL, data);

                return result;
            }
        }

        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(member_id);
    }

//    //populate dashboard Stickers arraylist
//    //populate urls arraylist
//    private void requestDashboardStickers(String member_id) {
//        class Room extends AsyncTask<String,Void,String> {
//
//            JsonClass ruc = new JsonClass();
//
//            @Override
//            protected void onPreExecute() {
//
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//                convertingResultDashboardStickers(result);
//
//                //save URLS
//                Gson gson = new Gson();
//                String json = gson.toJson(Constants.dashBoardStickers);
//                Log.i("saveDashboardStickers()", json);
//                InteriorDesignPref.putString(LoginActivity.this, "dashboardStickers", json);
//
//                Log.i("saveDashboardStickers()","dashboardStickers saved");
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//                HashMap<String,String> data = new HashMap<>();
//                Log.i("member_id", params[0]);
//                data.put("member_id",params[0]);
//
//                String result = ruc.sendPostRequest(DASHBOARD_STICKER_URL, data);
//                return result;
//            }
//        }
//
//        Room ulc = new Room();
//        ulc.execute(member_id);
//    }
//
//    public void convertingResultDashboardStickers(String jsonResult) {
//        try {
//            JSONObject obj = new JSONObject(jsonResult);
//            JSONArray array = (JSONArray) obj.get("data");
//
//            for (int i = 0; i < array.length(); i++) {
//
//                JSONObject innerObj = array.getJSONObject(i);
//
//                String id = String.valueOf(innerObj.get("id"));
//                String xaxis =  String.valueOf(innerObj.get("xaxis"));
//                String yaxis =String.valueOf(innerObj.get("yaxis"));
//                String width = String.valueOf(innerObj.get("width"));
//                String height = String.valueOf(innerObj.get("height"));
//                String angle = String.valueOf(innerObj.get("angle"));
//                String image = String.valueOf(innerObj.get("image"));
//
//                Log.i("dashboardStickers()", " added : " + id);
//                Constants.dashBoardStickers.add(new SelectedImage(id, xaxis, yaxis, width, height, angle, image));
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
