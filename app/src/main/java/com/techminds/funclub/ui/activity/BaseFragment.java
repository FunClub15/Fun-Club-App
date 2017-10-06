package com.techminds.funclub.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.techminds.funclub.utils.TouchEffect;

import com.techminds.funclub.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BaseFragment extends Fragment implements OnClickListener {

	TouchEffect TOUCH = new TouchEffect();

	public DisplayImageOptions options, optionProfile;
	public ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_splash,
				container, false);

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.displayer(new SimpleBitmapDisplayer()).build();

		optionProfile = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.displayer(new SimpleBitmapDisplayer()).build();

		return rootView;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			getActivity().finish();
			return true;
		}
		return true;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public View setTouchNClick(int id) {

		View v = getActivity().findViewById(id);
		v.setOnClickListener(this);
		v.setOnTouchListener(TOUCH);
		return v;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public View setClick(int id, View rootView) {

		View v = rootView.findViewById(id);
		v.setOnClickListener(this);
		return v;
	}

	public void setViewEnable(int id, boolean flag) {
		View v = getActivity().findViewById(id);
		v.setEnabled(flag);
	}

	@SuppressLint("NewApi")
	public void setViewVisibility(int id, int flag, View view) {
		View v = view.findViewById(id);
		v.setVisibility(flag);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setTextViewText(int id, String text, View v) {
		((TextView) v.findViewById(id)).setText(text);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setEditText(int id, String text) {
		((EditText) getActivity().findViewById(id)).setText(text);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public String getEditTextText(int id) {
		return ((EditText) getActivity().findViewById(id)).getText().toString()
				.trim();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public String getTextViewText(int id) {
		return ((TextView) getActivity().findViewById(id)).getText().toString()
				.trim();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public String getButtonText(int id) {
		return ((Button) getActivity().findViewById(id)).getText().toString();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setButtonText(int id, String text) {
		((Button) getActivity().findViewById(id)).setText(text);
	}

	public void replaceButtoImageWith(int replaceId, int drawable) {
		((Button) getActivity().findViewById(replaceId))
				.setBackgroundResource(drawable);
	}

	public void setButtonSelected(int id, boolean flag) {
		((Button) getActivity().findViewById(id)).setSelected(flag);
	}

	public boolean isButtonSelected(int id) {
		return ((Button) getActivity().findViewById(id)).isSelected();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
