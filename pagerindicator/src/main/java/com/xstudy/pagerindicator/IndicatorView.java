package com.xstudy.pagerindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

/**
 * Created by study on 17/12/11.
 *
 * 指示View
 */

public class IndicatorView extends View {

    private Drawable drawable;
    private int indicatorWidth;
    private int offset;

    private Rect currentRect = new Rect();

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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation();
            }else {
                invalidate();
            }
        }
    }

    public void pageScrolled(int toOffset , float positionOffset){
        Log.e("111111" , "toOffset = " + toOffset);
        if (currentRect.isEmpty()){
            currentRect.set(drawable.getBounds());
        }
        int iRight = toOffset + indicatorWidth/2;
        int iLeft = toOffset - indicatorWidth/2;

        if (toOffset > currentRect.left){
            //+1
            if (positionOffset>=0.5f){
                int offset = (int) (2 * (iLeft - currentRect.left) * (positionOffset-0.5));
                drawable.setBounds(currentRect.left + offset, currentRect.top , iRight, currentRect.bottom);
            }else {
                int offset = (int) ((iRight - currentRect.right)*positionOffset*2);
                Log.e("111111" , "offset = " + offset);
                drawable.setBounds(currentRect.left , currentRect.top ,currentRect.right + offset , currentRect.bottom);
            }
        }else {
            //-1
            positionOffset = 1 - positionOffset;
            if (positionOffset>=0.5f){
                int offset = (int) (2*(iRight - currentRect.right)*(positionOffset-0.5));
                Log.e("111111" , "offset = " + offset);
                drawable.setBounds(iLeft , currentRect.top ,currentRect.right + offset , currentRect.bottom);
            }else {
                int offset = (int) (2 * (iLeft - currentRect.left) * (positionOffset));
                drawable.setBounds(currentRect.left + offset, currentRect.top , currentRect.right, currentRect.bottom);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            postInvalidateOnAnimation();
        }else {
            invalidate();
        }
    }

    public void pageScrolledEnd(){
        currentRect.setEmpty();
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
        Log.e("2222" , drawable.getBounds()+"");
    }
}
