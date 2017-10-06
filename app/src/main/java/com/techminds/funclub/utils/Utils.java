package com.techminds.funclub.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.techminds.funclub.R;


/**
 * @author admin
 *
 */
/**
 * @author admin
 *
 */

/**
 * @author admin
 *
 */
@SuppressLint("NewApi")
public class Utils {

	public static String phNum = "";
	private static int startdeg = 360;
	public static String frientUserId = "";
	static int digits = 10;
	static int plus_sign_pos = 0;

	public static AlertDialog showDialog(Context ctx, String title, String msg,
										 String btn1, String btn2,
										 OnClickListener listener1,
										 OnClickListener listener2) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setMessage(msg).setCancelable(true)
				.setPositiveButton(btn1, listener1);
		if (btn2 != null)
			builder.setNegativeButton(btn2, listener2);

		AlertDialog alert = builder.create();
		alert.show();
		return alert;

	}

	public static String getValidMobileNo(String mobileNo) {

		if (mobileNo.length() > 10) {
			if (hasCountryCode(mobileNo)) {
				// +52 for MEX +526441122345, 13-10 = 3, so we need to remove 3 characters
				int country_digits = mobileNo.length() - digits;
				mobileNo = mobileNo.substring(country_digits);
			}

			if (String.valueOf(mobileNo.charAt(0)).equals("0")) {
				mobileNo = mobileNo.substring(1);
			}

		}
		return mobileNo;
	}


	private static boolean hasCountryCode(String number) {
		return number.charAt(plus_sign_pos) == '+';
	}

	public static float convertDpToPixels(Context context, float dp) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}


	public static String getLatestDateTime() {

		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String date = String.valueOf(c.get(Calendar.DATE));
		String time = String.valueOf(c.get(Calendar.HOUR) + ":"
				+ c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));

		return year + "-" + month + "-" + date + " " + time;
	}

	// get diffrence between to date in min
	public static long getDateDifference(String startDate, String endDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd h:mm:ss", Locale.ENGLISH);
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse(startDate);
			date2 = sdf.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long diff = date2.getTime() - date1.getTime();
		return diff / (1000);
	}

	public static AlertDialog showDialog(Context ctx, String title, String msg,
										 String btn1, String btn2, OnClickListener listener) {

		return showDialog(ctx, title, msg, btn1, btn2, listener,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						dialog.dismiss();
					}
				});

	}

	public static AlertDialog showDialog(Context ctx, String title, String msg,
										 OnClickListener listener) {

		return showDialog(ctx, title, msg, "Ok!", null, listener, null);
	}

	public static AlertDialog showDialog(Context ctx, String title, String msg) {


		return showDialog(ctx, title, msg,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						dialog.dismiss();
					}
				});

	}

	public static AlertDialog showDialogf(final Activity ctx, String title, String msg) {

		return showDialog(ctx, title, msg,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						dialog.dismiss();
						ctx.finish();
					}
				});

	}

	public static void showNoNetworkDialog(Context ctx) {

		showDialog(ctx, "No Network Connection",
				"Internet is not available. Please check your network connection.")
				.show();
	}

	public static String splitStringByGivenChar(String str, String splitChar) {

		String[] parts = str.split(splitChar);
		String part1 = parts[0];

		return part1;

	}

	public static void showFailDialog(final Activity ctx) {

		showDialog(ctx, "Error", "Failed to check the subscription.",
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ctx.finish();
					}
				}).show();
	}

	public static void showServerErrorDialog(final Activity ctx) {

		showDialog(ctx, "Error", "Server occured problem ... Please try again",
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ctx.finish();
					}
				}).show();
	}

	public static final boolean isOnline(Context context) {

		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMgr.getActiveNetworkInfo() != null

				&& conMgr.getActiveNetworkInfo().isAvailable()

				&& conMgr.getActiveNetworkInfo().isConnected())
			return true;
		return false;
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			Log.d("file", dir.list().length + dir.getAbsolutePath());
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		return dir.delete();
	}

	public static boolean isValidEmail(String email) {

		String emailExp = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,10}$";
		Pattern pattern = Pattern.compile(emailExp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isNumeric(String number) {

		String numExp = "^[-+]?[0-9]*\\.?[0-9]+$";
		Pattern pattern = Pattern.compile(numExp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();
	}

	public static String getDoubleFromString(String number) {

		String numExp = "^[-+]?[0-9]*\\.?[0-9]+$";
		Pattern pattern = Pattern.compile(numExp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(number);
		boolean match = matcher.matches();
		if (match)
			return number;
		else
			return "0.0";
	}

	public static String getIntFromString(String number) {

		String numExp = ".*[^0-9].*";
		Pattern pattern = Pattern.compile(numExp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(number);
		boolean match = matcher.matches();
		if (match)
			return number;
		else
			return "0";
	}

	public static final void hideKeyboard(Activity ctx) {

		if (ctx.getCurrentFocus() != null) {
			InputMethodManager imm = (InputMethodManager) ctx
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(),
					0);
		}
	}

	public static final void showKeyboard(Activity ctx) {

		if (ctx.getCurrentFocus() != null) {
			InputMethodManager imm = (InputMethodManager) ctx
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}
	}

	public static final void dialNumber(final Activity act, String number) {

		Intent call = new Intent(Intent.ACTION_DIAL);
		call.setData(Uri.parse("tel:" + number));
		act.startActivity(call);
	}

	public static final void makeCall(final Activity act, String number) {

		String num = number.replace(" ", "").replace("-", "");
		String digits = "0123456789";
		phNum = "";
		for (int i = 0; i < num.length(); i++) {
			if (digits.contains(num.charAt(i) + "")) {
				phNum += num.charAt(i);
			}
		}

		Log.d("phone", number + "=" + phNum);

		Utils.showDialog(act, "Call", "Call " + phNum, "Ok", "Cancel",
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent call = new Intent(Intent.ACTION_CALL);
						call.setData(Uri.parse("tel:" + phNum));
						act.startActivity(call);
					}
				}).show();
	}

	public static final void sendMessage(final Activity act, String phone) {

		String num = phone.replace(" ", "").replace("-", "");
		String digits = "0123456789";
		phNum = "";
		for (int i = 0; i < num.length(); i++) {
			if (digits.contains(num.charAt(i) + "")) {
				phNum += num.charAt(i);
			} else
				break;
		}

		Intent msg = new Intent(Intent.ACTION_SENDTO);
		msg.setData(Uri.parse("smsto:" + phone.trim()));
		act.startActivity(msg);

	}

	public static final void sendEmail(final Activity act, String email,
                                       String subject) {

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);

		act.startActivity(Intent.createChooser(intent, "Send Email"));
	}

	public static void copyFile(File src, File dst) {

		try {
			if (!dst.exists())
				dst.createNewFile();
			FileInputStream in = new FileInputStream(src);
			FileOutputStream out = new FileOutputStream(dst);

			int size = (int) src.length();
			byte[] buf = new byte[size];
			in.read(buf);
			out.write(buf);
			out.flush();
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createNoMediaFile(File dir) {

		try {
			File f = new File(dir, ".nomedia");
			if (!f.exists())
				f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	public static Typeface getTypeFace(Context ctx) {
		return Typeface.createFromAsset(ctx.getAssets(), "GOTHIC.TTF");
	}

	public static Typeface getBoldTypeFace(Context ctx) {
		return Typeface.createFromAsset(ctx.getAssets(), "GOTHICB.TTF");
	}

	public static String getRealPathFromURI(Uri contentUri, Activity act) {
		String[] proj = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
        Cursor cursor = act.managedQuery(contentUri, proj, null, null, null);

		if (cursor == null)
			return null;

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	public static String getRealPathForKitkatFromURI(Uri contentUri, Activity act) {
		String wholeID = DocumentsContract.getDocumentId(contentUri);

    	// Split at colon, use second item in the array
    	String id = wholeID.split(":")[1];

    	String[] column = { MediaStore.Images.Media.DATA };

    	// where id is equal to             
    	String sel = MediaStore.Images.Media._ID + "=?";

    	Cursor cursor = act.getContentResolver().
    	                          query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    	                          column, sel, new String[]{ id }, null);
    
    	int columnIndex = cursor.getColumnIndex(column[0]);
    	String path = "";
    	if (cursor.moveToFirst()) {
    		
    		Log.d("cursor gallery", cursor.getString(columnIndex) + "");
    		path = cursor.getString(columnIndex);
    	} 
    	return path;
	}

	public static Bitmap getOrientationFixedImage(File f) {

		try {
			ExifInterface exif = new ExifInterface(f.getPath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			int angle = 0;

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				angle = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				angle = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				angle = 270;
			}

			Matrix mat = new Matrix();
			mat.postRotate(angle);

			Bitmap bmp = Utils.getCompressedBm(f);
			Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), mat, true);
			return correctBmp;
		} catch (IOException e) {
			Log.w("TAG", "-- Error in setting image");
		} catch (OutOfMemoryError oom) {
			Log.w("TAG", "-- OOM Error in setting image");
		}
		return Utils.getCompressedBm(f);
	}
	
	public static final Bitmap getCompressedBm(byte b[], int w, int h) {

		try {
			final Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(b, 0, b.length, options);
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > h || width > w) {
				final int heightRatio = Math.round((float) height / (float) h);
				final int widthRatio = Math.round((float) width / (float) w);

				inSampleSize = heightRatio < widthRatio ? heightRatio
						: widthRatio;

			}
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeByteArray(b, 0, b.length, options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static final Bitmap getCompressedBm(File file, int w, int h) {

		try {
			final Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getAbsolutePath(), options);
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > h || width > w) {
				final int heightRatio = Math.round((float) height / (float) h);
				final int widthRatio = Math.round((float) width / (float) w);

				inSampleSize = heightRatio < widthRatio ? heightRatio
						: widthRatio;

			}
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static final Bitmap getCompressedBm(File file)
	{

		try
		{
			final Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getAbsolutePath(), options);
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			/*if (height > h || width > w)
			{
				final int heightRatio = Math.round((float) height / (float) h);
		        final int widthRatio = Math.round((float) width / (float) w);

		        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			}*/
			long maxMB = 1024*1024*2;
			
			if (file.length()>maxMB)
			{
				final int heightRatio = Math.round((float) file.length() / maxMB);

		        inSampleSize = heightRatio ;

			}
			//Log.d("Sample Size======================", inSampleSize + "");
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getOrientationFixedImage(File f, int w, int h) {

		try {
			ExifInterface exif = new ExifInterface(f.getPath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			int angle = 0;

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				angle = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				angle = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				angle = 270;
			}

			Matrix mat = new Matrix();
			mat.postRotate(angle);

			Bitmap bmp = Utils.getCompressedBm(f, w, h);
			Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), mat, true);
			return correctBmp;
		} catch (IOException e) {
			Log.w("TAG", "-- Error in setting image");
		} catch (OutOfMemoryError oom) {
			Log.w("TAG", "-- OOM Error in setting image");
		}
		return Utils.getCompressedBm(f, w, h);
	}
	
	

	public static Bitmap downLoadImage(String urlStr) {

		if (!URLUtil.isValidUrl(urlStr))
			return null;
		InputStream is = null;
		Bitmap bmp = null;
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.connect();
			is = conn.getInputStream();

			Options opt = new Options();
			opt.inScaled = true;
			opt.inDither = true;

			opt.inTempStorage = new byte[16000];

			bmp = BitmapFactory.decodeStream(is, null, opt);

			if (is != null)
				is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bmp;
	}

	/*
	 * public static String getStringFromPref(Context context, String key){
	 * return context.getSharedPreferences(Constant.PREF_FILE,
	 * Context.MODE_PRIVATE).getString(key, ""); }
	 * 
	 * public static boolean getBooleanFromPref(Context context, String key,
	 * boolean defaultVal){ return
	 * context.getSharedPreferences(Constant.PREF_FILE,
	 * Context.MODE_PRIVATE).getBoolean(key, defaultVal); }
	 */
	/** Create a file Uri for saving an image or video */
	public static Uri getOutputMediaFileUri() {
		return Uri.fromFile(getOutputMediaFile());
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile() {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"Jaunty Twig");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("Jaunty Twig", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "JapIMG_" + timeStamp + ".jpg");

		return mediaFile;
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static int resolveBitmapOrientation(File bitmapFile)
			throws IOException {
		ExifInterface exif = null;
		exif = new ExifInterface(bitmapFile.getAbsolutePath());

		return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_NORMAL);
	}

	public static Bitmap applyOrientation(Bitmap bitmap, int orientation) {
		int rotate = 0;
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_270:
			rotate = 270;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			rotate = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			rotate = 0;
			break;
		default:
			return bitmap;
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix mtx = new Matrix();
		mtx.postRotate(rotate);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

	public static void writeBitmapToFile(String tempphoto, Bitmap bmp) {
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(tempphoto);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String convertStringToFormat(String inputDate) {
		Date date;
		SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy hh:mmaa");
		try {
			date = format.parse(inputDate);
			format = new SimpleDateFormat("dd MMMMM yyyy");
			return format.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	

	public static boolean validateEditText(EditText et, int requestCode,
                                           String msg) {
		if (et.getText().toString().trim().equals("")) {
			et.setError("Required Field");
			return false;
		} else {

			if (requestCode == 103) {
				if (!Utils.isValidEmail(et.getText().toString().trim())) {
					et.setError(msg);
					return false;
				} else
					return true;
			}

			else if (requestCode == 101) {
				if (et.getText().toString().trim().equals("")) {
					et.setError(msg);
					return false;
				} else

					return true;
			} else if (requestCode == 102) {
				if (!Utils.isNumeric(et.getText().toString().trim())) {
					et.setError(msg);
					return false;
				} else
					return true;
			}
		}
		return true;
	}

	public static boolean isEditTextEmpty(EditText et) {
		if (et.getText().toString().trim().equals("")) {
			return true;
		}
		return false;
	}

	public static boolean validatePassword(EditText password,
			EditText confirmPassword) {
		boolean flag = true;
		if (password.getText().toString().trim().equals("")) {
			password.setError("Required Field");
			flag = false;
		}
		if (confirmPassword.getText().toString().trim().equals("")) {
			confirmPassword.setError("Required Field");
			flag = false;
		}
		if (!password.getText().toString()
				.equals(confirmPassword.getText().toString())) {
			confirmPassword.setError("Password not match");
			flag = false;
		}
		return flag;
	}

	public static boolean isPasswordMatch(EditText password,
			EditText confirmPassword) {
		if (!password.getText().toString()
				.equals(confirmPassword.getText().toString())) {
			return false;
		}
		return true;
	}
	public static boolean isEmailMatch(EditText email,
			EditText confirmEmail) {
		if (!email.getText().toString()
				.equals(confirmEmail.getText().toString())) {
			return false;
		}
		return true;
	}

	

	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 * 
	 * @param dp
	 *            A value in dp (density independent pixels) unit. Which we need
	 *            to convert into pixels
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on
	 *         device density
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * This method converts device specific pixels to density independent
	 * pixels.
	 * 
	 * @param px
	 *            A value in px (pixels) unit. Which we need to convert into db
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	public static String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy h:mm:ss a");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static Calendar getDateFromString(String strDate) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy h:mm:ss a");
		Calendar c = Calendar.getInstance();
		try {
			Date d = dateFormat.parse(strDate);
			c.setTime(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return c;
		}
		return c;
	}

	public static String getTime(int hr, int min) {
		Time tme = new Time(hr, min, 0);// seconds by default set to zero
		Format formatter;
		formatter = new SimpleDateFormat("h:mm:ss a");
		return formatter.format(tme);
	}

	public static String checkDigit(int number) {
		return number <= 9 ? "0" + number : String.valueOf(number);
	}

	public static void vibrateMe(Context context) {
		Vibrator v = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 500 milliseconds
		v.vibrate(100);
	}

	public static double roundTo2Decimal(double number) {
		return Math.round(number * 100.0) / 100.0;
	}


	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String device_id = tm.getDeviceId();
		return device_id;
	}

	public static int getDPValues(Context context, int val) {
		Resources r = context.getResources();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				val, r.getDisplayMetrics());
		return px;
	}

	public static void openWebUrl(Context context, String url) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		context.startActivity(i);
	}

	/**
	 * Author Mayur Tailor This method use to write genric object into file
	 * 
	 * @param e
	 * @param fileName
	 * @param context
	 */
	public <E> void writeObjectIntoFile(E e, String fileName, Context context) {
		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);

			os.writeObject(e);
			os.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * Author Mayur Tailor This method use to read genric object from file
	 * 
	 * @param fileName
	 * @param context
	 * @return
	 */
	public <E> E readObjectIntoFile(String fileName, Context context) {
		try {
			FileInputStream fis = context.openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			E e = (E) is.readObject();
			is.close();

			return e;
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return null;
	}

	public static String convertDateFormate(String datestr) {

		String inputPattern = "yyyy-MM-dd HH:mm:ss";
		String outputPattern = "MMM dd-yyyy";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date date = null;
		String str = null;

		try {
			date = inputFormat.parse(datestr);
			str = outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;

	}

	public static String convertDateToGMT(String datestr) {
		try {
			DateFormat fromFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			Date d = fromFormat.parse(datestr);

			TimeZone gmtTime = TimeZone.getTimeZone("GMT");
			fromFormat.setTimeZone(gmtTime);
			System.out.println("DateTime in GMT : " + fromFormat.format(d));
			return fromFormat.format(d);
			// return d.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String convertGMTToDate(String date) {
		try {
			DateFormat fromFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			final DateFormat formatter = new SimpleDateFormat(
					"dd/MM/yyyy hh:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date d = formatter.parse(date);

			return fromFormat.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void  LogMessage(String tag, String message)
	{
		Log.d(tag, message);
	}
	
	public static int checkPasswordStrength(String password) {
        int strengthPercentage=0;
        String[] partialRegexChecks = { ".*[a-z]+.*", // lower
        ".*[A-Z]+.*", // upper
        ".*[\\d]+.*", // digits
        ".*[@#$%]+.*" // symbols
        };


            if (password.matches(partialRegexChecks[0])) {
            	strengthPercentage+=25;
            }
            if (password.matches(partialRegexChecks[1])) {
            	strengthPercentage+=25;
            }
            if (password.matches(partialRegexChecks[2])) {
            	strengthPercentage+=25;
            }
            if (password.matches(partialRegexChecks[3])) {
            	strengthPercentage+=25;
            }
            Utils.LogMessage("strengthPercentage", strengthPercentage + "");

            return strengthPercentage;
            
            
	}
	
	
	public static boolean checkBirthDateValidation(int day,int month, int year)
	{
		Calendar cal = Calendar.getInstance();

        cal.set(year, month, day);
        
        Date bthDate = cal.getTime();
        
        Utils.LogMessage("bthDate", bthDate + "");
        Utils.LogMessage("Currewnt", new Date() + "");

        int diff1 =new Date().compareTo(bthDate);
        
        Utils.LogMessage("diff1", diff1 + "");

        if(diff1>0)
            return true;
         else
        	 return false;
	}
	
	public static void changeDescBack(View v, int color)
	{
		GradientDrawable sd = (GradientDrawable) v.getBackground().mutate();
		sd.setDither(true);
		sd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		
		sd.setColor(color);
		
		sd.invalidateSelf();
	}
	
	
	
	
	public static String JSONTokener(String in) {
		// consume an optional byte order mark (BOM) if it exists
		if (in != null && in.startsWith("\ufeff")) {
			in = in.substring(1);
		}
		return in;
	}
	
	
	public static String changeToString(String value)
	{
		if(value!=null)
			return value;
		else
			return "";
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		  int w = bitmap.getWidth();                                          
		  int h = bitmap.getHeight();                                         

		  int radius = Math.min(h / 2, w / 2);
		  Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Config.ARGB_8888);

		  Paint p = new Paint();
		  p.setAntiAlias(true);                                               

		  Canvas c = new Canvas(output);
		  c.drawARGB(0, 0, 0, 0);                                             
		  p.setStyle(Style.FILL);

		  c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);                  

		  p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		  c.drawBitmap(bitmap, 4, 4, p);                                      
		  p.setXfermode(null);                                                
		  p.setStyle(Style.STROKE);
		  p.setColor(Color.WHITE);
		  p.setStrokeWidth(3);                                                
		  c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);                  

		  return output;   
		 }
	
	public static boolean isGPSEnabled(Activity activity)
	{
		final LocationManager manager = (LocationManager) activity.getSystemService( Context.LOCATION_SERVICE );

	    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
	        return false;
	    else 
	    	return true;
	    
	}
	
	
	
	public static boolean isGPSEnabled(Context activity) {
		final LocationManager manager = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return false;
		else
			return true;

	}


	
	public static void appendLog(String text) {
		File logFile = new File("sdcard/bzzzlog.txt");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.append(text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void deleteLogs() {
		File logFile = new File("sdcard/bzzzlog.txt");
		if (logFile.exists()) {
			logFile.delete();
		}

	}

	public static String getIP(Context context){
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						String ip = Formatter.formatIpAddress(inetAddress.hashCode());
						Log.i("IP", "***** IP=" + ip);
						return ip;
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("IP", ex.toString());
		}

		return "";
	}
	
	
	 public static String getDateFromTimeStamp(String timestamp) {

		  Calendar calendar = Calendar
		    .getInstance(TimeZone.getTimeZone("GMT"));
		  

		  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
		  sdf.setTimeZone(calendar.getTimeZone());
		  try {
		   calendar.setTime(sdf.parse(timestamp));

		   
		  } catch (ParseException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }

		   sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
		  calendar.setTimeZone(TimeZone.getDefault());
		  sdf.setTimeZone(calendar.getTimeZone());
		  
		  return sdf.format(calendar.getTime());

		 }
	 
	 public static String remainingTimeAfterCompareDates(String getDateFromServer)
	 {
		 Calendar c = Calendar.getInstance();
	    	
		 SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
		 
		  Date d1 = null;
		  Date d2 = null;
		  
		  String result = "";
		  
		  try {
			  
			  String currentDate = df.format(c.getTime());
			  
				
				
		//		Utils.LogMessage("==========start=======================", "========start=====");
				
			//	Utils.LogMessage("current date===============", currentDate+"");
			//	Utils.LogMessage("get review date===============", getDateFromServer+"");
				
				
				String newDate = Utils.getDateFromTimeStamp(getDateFromServer);
				
				d1 = df.parse(currentDate);
				d2 = df.parse(newDate);
				
				 long diff = d1.getTime() - d2.getTime();
				 
				 long diffSeconds = diff / 1000 % 60;
				 long diffMinutes = diff / (60 * 1000) % 60;
				 long diffHours = diff / (60 * 60 * 1000) % 24;
				 long diffDays = diff / (24 * 60 * 60 * 1000);
				 long diffMonths = diffDays/30;
				 long diffWeeks = diffDays/7;
				 long diffYears = diffDays/365;
		 
		//		 Utils.LogMessage("===============diffDays" , " ============"+diffDays);
		//		 Utils.LogMessage("===============diffHours" , "============"+diffHours);
		//		 Utils.LogMessage("===============diffMinutes" ,"============"+diffMinutes);
		//		 Utils.LogMessage("===============diffSeconds" ," ============"+diffSeconds);
				 
				
				 if(diffYears>0)
				 {
					 if(diffYears<=1)
						 result = diffYears+"yr ago";
					 else
						 result = diffYears+"yr ago";
				 }
				 else if(diffMonths>0)
				 {
					 if(diffMonths<=1)
						 result = diffMonths+"mth ago";
					 else
						 result = diffMonths+"mth ago";
				 }
				 else if(diffWeeks>0)
				 {
					 if(diffWeeks<=1)
						 result = diffWeeks+"w ago";
					 else
						 result = diffWeeks+"w ago";
				 }
				 else if(diffDays>0)
				 {
					 if(diffDays<=1)
						 result = diffDays+"d ago";
					 else
						 result = diffDays+"d ago";
				 }
				 else if(diffHours>0)
				 {
					 if(diffHours<=1)
						 result = diffHours+"hr ago";
					 else
						 result = diffHours+"hr ago";
				 }
				 else if(diffMinutes>0)
				 { 
					 if(diffMinutes<=1)
						 result = diffMinutes+"m ago";
					 else
						 result = diffMinutes+"m ago";
				 }
				 else if(diffSeconds>0)
				 {
					 if(diffSeconds<=1)
						 result = diffSeconds+"s ago";
					 else
						 result = diffSeconds+"s ago";
				 }
				
			
				
			//	Utils.LogMessage("==========end=======================", "========end=====");
				
		  }
		  catch (Exception e) {
				e.printStackTrace();
			}
		  
		  
		  return result;
	 }
	 
	 
	 public static void makeUserImage(ImageView mImageView, Bitmap loadedImage, Context ctx)
	{
			Bitmap original =loadedImage;
			Bitmap mask = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher);
			Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888);
			Canvas mCanvas = new Canvas(result);
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			mCanvas.drawBitmap(Bitmap.createScaledBitmap(original, mask.getWidth(), mask.getHeight(), true), 0, 0, null);
			mCanvas.drawBitmap(mask, 0, 0, paint);
			paint.setXfermode(null);
			mImageView.setImageBitmap(result);
			//mImageView.setBackgroundResource(R.drawable.company_image_avtar);
	}
	 
	 
	 /**
	  * Get a file path from a Uri. This will get the the path for Storage Access
	  * Framework Documents, as well as the _data field for the MediaStore and
	  * other file-based ContentProviders.
	  *
	  * @param context The context.
	  * @param uri The Uri to query.
	  * @author paulburke
	  */
	 public static String getPath(final Context context, final Uri uri) {

	     final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	     // DocumentProvider
	     if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	         // ExternalStorageProvider
	         if (isExternalStorageDocument(uri)) {
	             final String docId = DocumentsContract.getDocumentId(uri);
	             final String[] split = docId.split(":");
	             final String type = split[0];

	             if ("primary".equalsIgnoreCase(type)) {
	                 return Environment.getExternalStorageDirectory() + "/" + split[1];
	             }

	             // TODO handle non-primary volumes
	         }
	         // DownloadsProvider
	         else if (isDownloadsDocument(uri)) {

	             final String id = DocumentsContract.getDocumentId(uri);
	             final Uri contentUri = ContentUris.withAppendedId(
						 Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	             return getDataColumn(context, contentUri, null, null);
	         }
	         // MediaProvider
	         else if (isMediaDocument(uri)) {
	             final String docId = DocumentsContract.getDocumentId(uri);
	             final String[] split = docId.split(":");
	             final String type = split[0];

	             Uri contentUri = null;
	             if ("image".equals(type)) {
	                 contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	             } else if ("video".equals(type)) {
	                 contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	             } else if ("audio".equals(type)) {
	                 contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	             }

	             final String selection = "_id=?";
	             final String[] selectionArgs = new String[] {
	                     split[1]
	             };

	             return getDataColumn(context, contentUri, selection, selectionArgs);
	         }
	     }
	     // MediaStore (and general)
	     else if ("content".equalsIgnoreCase(uri.getScheme())) {
	         return getDataColumn(context, uri, null, null);
	     }
	     // File
	     else if ("file".equalsIgnoreCase(uri.getScheme())) {
	         return uri.getPath();
	     }

	     return null;
	 }

	 /**
	  * Get the value of the data column for this Uri. This is useful for
	  * MediaStore Uris, and other file-based ContentProviders.
	  *
	  * @param context The context.
	  * @param uri The Uri to query.
	  * @param selection (Optional) Filter used in the query.
	  * @param selectionArgs (Optional) Selection arguments used in the query.
	  * @return The value of the _data column, which is typically a file path.
	  */
	 public static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

	     Cursor cursor = null;
	     final String column = "_data";
	     final String[] projection = {
	             column
	     };

	     try {
	         cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                 null);
	         if (cursor != null && cursor.moveToFirst()) {
	             final int column_index = cursor.getColumnIndexOrThrow(column);
	             return cursor.getString(column_index);
	         }
	     } finally {
	         if (cursor != null)
	             cursor.close();
	     }
	     return null;
	 }


	 /**
	  * @param uri The Uri to check.
	  * @return Whether the Uri authority is ExternalStorageProvider.
	  */
	 public static boolean isExternalStorageDocument(Uri uri) {
	     return "com.android.externalstorage.documents".equals(uri.getAuthority());
	 }

	 /**
	  * @param uri The Uri to check.
	  * @return Whether the Uri authority is DownloadsProvider.
	  */
	 public static boolean isDownloadsDocument(Uri uri) {
	     return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	 }

	 /**
	  * @param uri The Uri to check.
	  * @return Whether the Uri authority is MediaProvider.
	  */
	 public static boolean isMediaDocument(Uri uri) {
	     return "com.android.providers.media.documents".equals(uri.getAuthority());
	 }
	 
	
	 
}
