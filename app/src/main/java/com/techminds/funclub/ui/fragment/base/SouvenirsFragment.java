package com.techminds.funclub.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techminds.funclub.R;

/**
 * Created by waseemakram on 6/15/17.
 */

public class SouvenirsFragment extends BaseFragment {
View view;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_souvenirs,container,false);
        return  view;
    }
    @Override
    protected void setViews() {

    }

    @Override
    protected void setOnclickListeners() {

    }

    @Override
    protected void changeFragmentNoHistory(String Tag, Fragment fragment) {

    }
}
