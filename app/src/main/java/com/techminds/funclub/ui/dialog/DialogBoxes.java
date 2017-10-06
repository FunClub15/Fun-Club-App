package com.techminds.funclub.ui.dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.techminds.funclub.CustomMethods.JsonClass;
import com.techminds.funclub.R;
import com.techminds.funclub.model.bean.ForgotPasswordModel;
import com.techminds.funclub.ui.activity.LoginActivity;
import com.techminds.funclub.ui.activity.SignUpActivity;
import com.techminds.funclub.ui.fragment.base.UpdateFragment;
import com.techminds.funclub.utils.ServiceUtil;
import com.techminds.funclub.utils.SnackBarUtils;
import com.techminds.funclub.utils.config.APIService;
import com.techminds.funclub.utils.config.SharedPrefSocialIcons;
import com.techminds.funclub.webServices.WEB_SERVICES_LINKS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DialogBoxes {

    private final String TAG = this.getClass().getSimpleName();

    public static Dialog discountDialog, checkoutDialog;
    ProgressDialog loading;
    static Dialog progressDialog, PickAProPic;
    static Dialog choose_pic;
    ProgressBar progressBar;
    TextView tv_title, tv_message;
    Intent pickPhoto, takePicture;
    ImageView im;
    AlertDialog alertDialog;


    public static void dialogForgotPassword(final Activity activity) {

        final Dialog forgotpassword = new Dialog(activity);
        forgotpassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotpassword.setContentView(R.layout.dilog_forgot_password);
        forgotpassword.setCancelable(true);
        forgotpassword.show();

        final EditText email = (EditText) forgotpassword.findViewById(R.id.email);
        Button Cancel = (Button) forgotpassword.findViewById(R.id.btnCancel);
        Button Send = (Button) forgotpassword.findViewById(R.id.btnSend);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword.dismiss();
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.getText().toString();
                if (emailString.trim().isEmpty()) {
                    email.setError("Email is missing");
                } else if (!emailString.contains(".") && !emailString.contains("@")) {
                    email.setError("Invalid Email");
                }
                // SnackBarUtils.showSnackBar(activity.findViewById(android.R.id.content), "Invalid Email", Snackbar.LENGTH_SHORT);
                else {
                    networkForgotPassword(activity, emailString, forgotpassword);
                }
            }
        });
    }


    public static void dialogResendActivationLink(final Activity activity) {

        final Dialog forgotpassword = new Dialog(activity);
        forgotpassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotpassword.setContentView(R.layout.dialog_resend_activation_link);
        forgotpassword.setCancelable(true);
        forgotpassword.show();


        Log.i("Clciked()","done");
        final EditText email = (EditText) forgotpassword.findViewById(R.id.email);
        Button Cancel = (Button) forgotpassword.findViewById(R.id.btnCancel);
        Button Send = (Button) forgotpassword.findViewById(R.id.btnSend);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword.dismiss();
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Clciked()1","done");
                String emailString = email.getText().toString();
                if (emailString.trim().isEmpty()) {
                    email.setError("Email is missing");
                    Log.i("Clciked()2","done");
                } else if (!emailString.contains(".") && !emailString.contains("@")) {
                    email.setError("Invalid Email");
                    Log.i("Clciked()3","done");
                }
                // SnackBarUtils.showSnackBar(activity.findViewById(android.R.id.content), "Invalid Email", Snackbar.LENGTH_SHORT);
                else {

                    Log.i("Clciked()4","done");
                    // here i have to call the resent activation link method
                    resend_Activation_link(activity,emailString,forgotpassword);
                }
            }
        });
    }


    public static void dialogThankyou(final Activity activity) {

        final Dialog forgotpassword = new Dialog(activity);
        forgotpassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotpassword.setContentView(R.layout.dilog_thank_you);
        forgotpassword.setCancelable(true);
        forgotpassword.show();

        Button Send = (Button) forgotpassword.findViewById(R.id.btnOk);

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword.dismiss();
                Intent i = new Intent(activity, LoginActivity.class);
                activity.startActivity(i);
                activity.finish();
            }
        });
    }

    public static void chooseFrom(final Activity activity) {

        choose_pic = new Dialog(activity);
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
                activity.startActivityForResult(intent, 0);
                choose_pic.dismiss();
            }
        });


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                activity.startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
                choose_pic.dismiss();
            }
        });
    }

    public void showProgress(Activity activity, String title, int message) {

        progressDialog = new Dialog(activity);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_progressbar);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();

        progressBar = (ProgressBar) progressDialog.findViewById(R.id.progresbar);
        tv_title = (TextView) progressDialog.findViewById(R.id.title);
        tv_message = (TextView) progressDialog.findViewById(R.id.message);

        tv_title.setText(title);
        tv_message.setText(message);

        for (int progress = 0; progress < 100; progress += 10) {

            progressBar.setProgress(progress);

        }

    }

    public void hideProgress() {
        progressDialog.dismiss();

    }

    /*
    * Resend Activation Link Method
    * */
    static String jsonResult;

    private static void resend_Activation_link(Activity activity ,String email,Dialog forgotpassword){
        resend_Activation_link_method(activity,email,forgotpassword);
    }

    private static void resend_Activation_link_method(final Activity activity,final String email,final Dialog forgotpassword) {
        class UserLoginClass extends AsyncTask<String,Void,String> {

            final Dialog FP = forgotpassword;
            DialogBoxes dialogBoxes;

            JsonClass ruc = new JsonClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                dialogBoxes = new DialogBoxes();
                dialogBoxes.showProgress(activity, "", R.string.loading);
               // loading = ProgressDialog.show(activity, "Please wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                jsonResult = s;

                try {

                    JSONObject jsonObject = new JSONObject(jsonResult);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if(status.equals("1")){
                        Toast.makeText(activity,"Resent Activation Sent",Toast.LENGTH_SHORT).show();
                        Log.i("Response()",message);
                      //  DialogBoxes.dialogThankyou(context);
                    }else if(status.equals("0")){
                        Log.i("Response()",message);
                        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("Response()",jsonResult);

                        //mask477@gmail.com

                dialogBoxes.hideProgress();
               // loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("email", params[0]);

                String result = ruc.sendPostRequest(WEB_SERVICES_LINKS.API_RESENT_ACTIVATION,data);

                return result;
            }
        }

        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(email);
    }


    public static void networkForgotPassword(final Activity activity, String email, Dialog forgotpassword) {
        final Dialog FP = forgotpassword;
        final DialogBoxes dialogBoxes;
        dialogBoxes = new DialogBoxes();
        dialogBoxes.showProgress(activity, "", R.string.loading);
        try {

            APIService api = ServiceUtil.restAdapter().create(APIService.class);

            api.forgotPassword(email, new Callback<ForgotPasswordModel>() {
                @Override
                public void success(ForgotPasswordModel userResponse, Response response) {
                    dialogBoxes.hideProgress();
                    int status = userResponse.getStatus();
                    if (status == 1) {
                        Toast.makeText(activity.getApplicationContext(), "Password Sent To Email", Toast.LENGTH_LONG).show();
                        FP.dismiss();
                    } else {
                        Toast.makeText(activity.getApplicationContext(), R.string.error_email, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(activity.getApplicationContext(), R.string.error, Toast.LENGTH_LONG).show();
                    dialogBoxes.hideProgress();
                }
            });

        } catch (Exception Exp) {
            Exp.printStackTrace();
        }
    }
}
/*
    public static void  showDiscountPopup(final Activity activity){

        discountDialog = new Dialog(activity);
        discountDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        discountDialog.setContentView(R.layout.dialog_discountoffer);
        discountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        discountDialog.setCancelable(true);
        discountDialog.show();

        Button cancel = (Button) discountDialog.findViewById(R.id.cancel);
        Button ok = (Button) discountDialog.findViewById(R.id.ok);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discountDialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discountDialog.dismiss();
            }
        });

        //  text_dummy.setText(messgae);
    }


    public static void  showCheckoutPopup(final Activity activity){

        checkoutDialog = new Dialog(activity);
        checkoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        checkoutDialog.setContentView(R.layout.dialog_checkout);
        checkoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkoutDialog.setCancelable(true);
        checkoutDialog.show();

        Button cancel = (Button) checkoutDialog.findViewById(R.id.cancel);
        Button ok = (Button) checkoutDialog.findViewById(R.id.ok);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkoutDialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutDialog.dismiss();


            }
        });

        //  text_dummy.setText(messgae);
    }*/


