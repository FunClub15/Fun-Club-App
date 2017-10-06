package com.techminds.funclub.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Manay.Khan on 12/13/2016.
 */
public class SnackBarUtils {

    public static void showSnackBar(View rootView, String text, int duration){
        Snackbar snack = Snackbar.make(rootView,text, duration);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }
}
