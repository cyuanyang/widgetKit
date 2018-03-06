package com.tal.imagepicker.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by cyy on 2016/7/4.
 *
 */
public class ToastHelper {

    public static void show(final String msg , final Activity a){
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(a , msg , Toast.LENGTH_SHORT).show();
            }
        });
    }
}
