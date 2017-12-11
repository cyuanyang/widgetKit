package com.xstudy.pagerindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.view.View;

/**
 * Created by study on 17/12/11.
 *
 * 指示View
 */

public class IndicatorView extends View {

    private Drawable drawable;
    private int width;

    public IndicatorView(Context context , @DrawableRes int d , int width) {
        super(context);
        this.drawable = ActivityCompat.getDrawable(context , d);
        this.width = width;
    }

    public void update(int offset ,int height){
        drawable.setBounds(offset - width/2 , 0 ,offset + width/2  , height);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawable.draw(canvas);
    }
}
