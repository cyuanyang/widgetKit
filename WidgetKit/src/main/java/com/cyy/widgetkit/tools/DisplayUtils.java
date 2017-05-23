package com.cyy.widgetkit.tools;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.xstudy.assistteacher.App;

/**
 * Created by cyy
 * 屏幕工具
 */
public class DisplayUtils {

    /**
     * dp 转换成 px
     *
     * @param dpValue
     * @return
     */
    public static int dip2px(int dpValue) {
        float scale = 0;
        try {
            scale = App.getApp().getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            return dpValue;
        }
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * dp 转 px
     * @param dpValue
     * @return
     */
    public static int dip2px(double dpValue) {
        float scale = 0;
        try {
            scale = App.getApp().getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            return (int) dpValue;
        }
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px 转换成 dp
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(int pxValue) {
        final float scale = App.getApp().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(int pxValue) {
        final float scale = App.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px( int spValue) {
        final float scale = App.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * px 转 dp
     * @param pxValue
     * @return
     */
    public static int px2dip(double pxValue) {
        float m = App.getApp().getResources().getDisplayMetrics().density;
        return (int) (pxValue / m + 0.5f);
    }

    /**
     * 屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Activity context) {
        if (context == null) {
            return 1;
        } else {
            DisplayMetrics metric = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metric);
            return metric.heightPixels;
        }
    }

    /**
     * 屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Activity context) {
        if (context == null) {
            return 1;
        } else {
            DisplayMetrics metric = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metric);
            return metric.widthPixels;
        }
    }


    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Activity context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 计算两种颜色之间的某个颜色值，这个值跟系数有关系
     * @param fraction 系数，0返回开始颜色 1返回救赎颜色 取值范围在[0 , 1]
     * @param startColor 开始颜色
     * @param endColor 结束颜色
     * @return 根据系数返回中间的某一个颜色
     */
    public static int evaluate(float fraction, long startColor, long endColor) {

        fraction = fraction < 0.0f ? 0.0f : fraction;
        fraction = fraction > 1.0f ? 1.0f : fraction;

        long startInt = startColor;
        long startA = (startInt >> 24) & 0xff;
        long startR = (startInt >> 16) & 0xff;
        long startG = (startInt >> 8) & 0xff;
        long startB = startInt & 0xff;

        long endInt = endColor;
        long endA = (endInt >> 24) & 0xff;
        long endR = (endInt >> 16) & 0xff;
        long endG = (endInt >> 8) & 0xff;
        long endB = endInt & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }

}
