package com.techminds.funclub.ui.fragment.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.techminds.funclub.R;
import com.techminds.funclub.ui.activity.LoginActivity;
import com.techminds.funclub.ui.activity.base.UserGallery;
import com.techminds.funclub.utils.config.SharedPrefSocialIcons;

/**
 * Created by waseemakram on 6/15/17.
 */

public class ContentFragment extends BaseFragment {

    View view;

    Button galleryButton;
    LinearLayout photoButton;
    LinearLayout videoButton;
    LinearLayout liveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_content,container,false);
//        setViews();
//        setOnclickListeners();

        galleryButton = (Button) view.findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UserGallery.class);
                startActivity(i);
            }
        });

        photoButton = (LinearLayout) view.findViewById(R.id.photoButton);
        photoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Upload Photo", Toast.LENGTH_SHORT).show();
            }
        });

        videoButton = (LinearLayout) view.findViewById(R.id.videoButton);
        videoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Upload Video", Toast.LENGTH_SHORT).show();
            }
        });

        liveButton = (LinearLayout) view.findViewById(R.id.liveButton);
        liveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Go Live!", Toast.LENGTH_SHORT).show();
            }
        });


        return  view;
    }

    @Override
    protected void setViews() {

        galleryButton = (Button) view.findViewById(R.id.galleryButton);
        photoButton = (LinearLayout) view.findViewById(R.id.photoButton);
        videoButton = (LinearLayout) view.findViewById(R.id.videoButton);
        liveButton = (LinearLayout) view.findViewById(R.id.liveButton);

    }

    @Override
    protected void setOnclickListeners() {

    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.galleryButton: {
//                startActivity(new Intent(getActivity(), UserGallery.class));
//                break;
//            }
//            case R.id.photoButton: {
//                Toast.makeText(getActivity(), "Upload Photo", Toast.LENGTH_SHORT).show();
//                break;
//            }
//            case R.id.videoButton: {
//                Toast.makeText(getActivity(), "Upload Video", Toast.LENGTH_SHORT).show();
//                break;
//            }
//            case R.id.liveButton: {
//                Toast.makeText(getActivity(), "Go Live!", Toast.LENGTH_SHORT).show();
//                break;
//            }
//
//
//        }
//    }



    @Override
    protected void changeFragmentNoHistory(String Tag, Fragment fragment) {

    }
}
