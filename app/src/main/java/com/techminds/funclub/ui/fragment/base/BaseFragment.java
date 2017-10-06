package com.techminds.funclub.ui.fragment.base;

import android.support.v4.app.Fragment;

/**
 * Created by waseemakram on 5/24/17.
 */

public abstract class BaseFragment extends Fragment {

    protected abstract void setViews();

    protected abstract void setOnclickListeners();

    protected abstract void changeFragmentNoHistory(String Tag, Fragment fragment);


}