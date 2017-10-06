package com.techminds.funclub.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.techminds.funclub.utils.ExceptionHandler;
import com.techminds.funclub.utils.TouchEffect;

import com.techminds.funclub.R;

public class BaseActivity extends AppCompatActivity implements OnClickListener {

	TouchEffect TOUCH = new TouchEffect();
	
	public DisplayImageOptions options,optionProfile;
	public ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.activity_splash);
		
		 options = new DisplayImageOptions.Builder()/*
	  		.showImageOnLoading(R.drawable.profile_img)
	  		.showImageForEmptyUri(R.drawable.profile_img)
	  		.showImageOnFail(R.drawable.profile_img)*/
	  		.cacheInMemory(true)
	  		.cacheOnDisk(true)
	  		.considerExifParams(true)
	  		.displayer(new SimpleBitmapDisplayer())
	  		.build();
				  
		  optionProfile = new DisplayImageOptions.Builder()		
	  		.cacheInMemory(true)  		
	  		.cacheOnDisk(true)
	  		.considerExifParams(true)
	  		.displayer(new RoundedBitmapDisplayer(20))
	  		.build();

	}

	public View setTouchNClick(int id) {

		View v = findViewById(id);
		v.setOnClickListener(this);
		v.setOnTouchListener(TOUCH);
		return v;
	}

	public View setClick(int id) {

		View v = findViewById(id);
		v.setOnClickListener(this);
		return v;
	}

	public void setViewEnable(int id, boolean flag) {
		View v = findViewById(id);
		v.setEnabled(flag);
	}

	public void setViewVisibility(int id, int flag) {
		View v = findViewById(id);
		v.setVisibility(flag);
	}

	public void setTextViewText(int id, String text) {
		((TextView) findViewById(id)).setText(text);
	}

	public void setEditText(int id, String text) {
		((EditText) findViewById(id)).setText(text);
	}

	public String getEditTextText(int id) {
		return ((EditText) findViewById(id)).getText().toString().trim();
	}

	public String getTextViewText(int id) {
		return ((TextView) findViewById(id)).getText().toString().trim();
	}

	public String getButtonText(int id) {
		return ((Button) findViewById(id)).getText().toString();
	}

	public void setButtonText(int id, String text) {
		((Button) findViewById(id)).setText(text);
	}

	public void replaceButtoImageWith(int replaceId, int drawable) {
		((Button) findViewById(replaceId)).setBackgroundResource(drawable);
	}

	public void setButtonSelected(int id, boolean flag) {
		((Button) findViewById(id)).setSelected(flag);
	}

	public boolean isButtonSelected(int id) {
		return ((Button) findViewById(id)).isSelected();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
