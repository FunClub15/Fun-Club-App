package com.techminds.funclub.ui.activity.base;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by waseemakram on 5/22/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public abstract void onClick(View v);

    protected abstract void setOnclickListener();

    protected abstract void setViews();

    protected abstract void addFragmentNoHistory(Fragment fragment);

    protected abstract void addFragment(String Tag,Fragment fragment);

    protected abstract void replaceFragmentNoHistory(Fragment fragment);

    protected abstract void replaceFragment(String Tag,Fragment fragment);


}
