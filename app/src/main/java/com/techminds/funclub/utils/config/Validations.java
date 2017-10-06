package com.techminds.funclub.utils.config;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class Validations {
    Context context;
    public static Drawable drawableValid, drawableInValid;
    public static String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

    public boolean isValidText(EditText edittext, String fName) {
        String validate = edittext.getText().toString();
        validate = validate.replaceAll("\\s+", " ").trim();
        if (validate.isEmpty()) {
            edittext.setError(fName + " is Empty");
            return false;
        }
        return true;
    }


    public boolean isValidPassword(EditText editText) {
        String pass = editText.getText().toString().trim();
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        editText.setError("Should be greater than 6!");
        return false;

    }

    public boolean isPasswordEquals(EditText pass, EditText conform) {
        String password = pass.getText().toString().trim();
        String conformPass = conform.getText().toString().trim();

        if (password.equals(conformPass)) {
            return true;
        }
        conform.setError("Password not match!");
        return false;

    }

    public boolean isValidNumber(EditText editText) {
        String number = editText.getText().toString().trim();
        if (number != null && number.length() >= 10) {
            return true;
        }
        editText.setError("Invalid phone number!");
        return false;
    }

    //Verifying Email Pattern
    public boolean isValidEmail(EditText email) {

        if (email.getText().toString().trim().matches(EMAIL_PATTERN) || email.getText().toString().trim().matches(expression)) {

            return true;
        } else {
            email.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableInValid, null);
            email.setError("Invalid Email Address!");
            return false;
        }
    }
    //Verifying Email Pattern End

    public boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void clearCache(Activity activity) {
        CookieSyncManager.createInstance(activity);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }


    public void fullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void checkEditText(final EditText edit) {
        edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                if (count != 0) {
                    edit.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableValid, null);
                } else {
                    edit.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });
    }


    public void checkEditTextEmail(final EditText edit) {
        edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                edit.setError(null);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                if (count != 0) {

                    if (isValidEmail(edit)) {
                        edit.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableValid, null);
                    } else {
                        edit.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableInValid, null);
                    }

                } else {
                    edit.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                edit.setError(null);
            }

        });
    }


    public void showToast(String msg, Activity activity) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    //Validation on EditText Start

    //Hide edit text start
    public static boolean hide(EditText field, InputMethodManager imm) {

        imm.hideSoftInputFromWindow(field.getApplicationWindowToken(), 0);

        return false;

    }
    //Hide Edit text end

    //Encode Bitmap into String start
    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);

        return imageEncoded;
    }
    //Encode Bitmap into String end


    //Decode Imgae from String to Bitmap Start
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    //Decode Imgae from String to Bitmap End


    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    public void transitionAlpha(ImageView image, int duration) {
        ObjectAnimator.ofFloat(image, "alpha", 1, 0, 1).setDuration(duration).start();
    }


    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("", "UTF-8 should always be supported", e);
            return "";
        }
    }


    // Show Alert Box Start
    public void resultAlert(String title, String msg, final Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);

        builder.setMessage(msg);

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // activity.finish();
            }
        });

        builder.show();
    }

}

