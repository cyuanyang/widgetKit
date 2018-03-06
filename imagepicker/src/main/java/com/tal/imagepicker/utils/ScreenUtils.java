package com.tal.imagepicker.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by cyy on 2016/7/6.
 *
 */
public class ScreenUtils {

    public static Size getSceenSize(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return new Size(dm.widthPixels , dm.heightPixels);
    }

    public static class Size{
        public int width;
        public int height;

        public Size(int width , int height){
            this.width = width ;
            this.height = height;
        }
    }
}
