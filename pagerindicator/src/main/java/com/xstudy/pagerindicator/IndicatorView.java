package com.xstudy.pagerindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by study on 17/12/11.
 *
 * 指示View
 */

public class IndicatorView extends View {

    private Drawable drawable;
    private int indicatorWidth;
    private int offset;

    private boolean drawableNeedUpdate = false;
    private boolean isLayout;

    public IndicatorView(Context context , @DrawableRes int d , int indicatorWidth) {
        super(context);
        this.indicatorWidth = indicatorWidth;
        this.drawable = ActivityCompat.getDrawable(context , d);
    }

    public void update(int offset){
        this.offset = offset;
        if (!isLayout){
            drawableNeedUpdate = true;
        }else {
            drawable.setBounds(offset - indicatorWidth/2 , 0 ,
                    offset + indicatorWidth/2 , getHeight());
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!isLayout){
            isLayout = true;
            drawable.setBounds(offset - indicatorWidth/2 , 0 ,
                    offset + indicatorWidth/2 , getHeight());
            drawableNeedUpdate = false;
        }
    }

    @Override
    public boolean isInLayout() {
        return super.isInLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawable.draw(canvas);
    }
}
