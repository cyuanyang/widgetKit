package com.cyy.canvasview.layers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.cyy.canvasview.Layer;
import com.cyy.canvasview.LayerCanvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by study on 17/12/13.
 *
 * 图章图层
 */

public class StampLayer extends Layer{

    private final static String  STAMP_LAYER= "StampLayer";


    private List<Stamp> stamps = new ArrayList<>();
    private Stamp addingStamp;

    public StampLayer(Context context){
        super(context);

        setBackgroundColor(Color.RED);
    }

    @Override
    protected void reset(){
        super.reset();
        for (Stamp s : stamps) {
            s.release();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0 ; i < getChildCount() ; i++){
            View child = getChildAt(i);
            if (child == addingStamp.imageView){
                if (addingStamp.isCenter){
                    int l = getMeasuredWidth()/2 - child.getMeasuredWidth()/2;
                    int t = getMeasuredHeight()/2 - child.getMeasuredHeight()/2;
                    int r = l + child.getMeasuredWidth();
                    int b = t + child.getMeasuredHeight();
                    child.layout(l,t,r,b);
                    addingStamp.setRect(l , t , r , b);
                }
            }else {
                Rect rect = (Rect) child.getTag();
                child.layout(rect.left , rect.top , rect.right , rect.bottom);
            }
        }
    }

    /**
     * 添加图章
     * @param bitmap 图章
     */
    public void addStamp(Bitmap bitmap , boolean isCenter){
        if (isBegin()){
            Stamp stamp = new Stamp(getContext() , bitmap);
            stamp.isCenter = isCenter;
            addView(stamp.imageView);
            stamps.add(stamp);

            addingStamp = stamp;
        }else {
            throw new IllegalStateException("请先调用开始方法");
        }

    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public String getIdentify() {
        return STAMP_LAYER;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

    private int preX , preY;
    private Stamp moveStamp;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                preX = (int) event.getX();
                preY = (int) event.getY();

                moveStamp = findMoveStamp(preX , preY);
                break;
            case MotionEvent.ACTION_MOVE:

                if (moveStamp != null){
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    int offsetX = x - preX;
                    int offsetY = y - preY;

                    moveStamp(offsetX , offsetY);

                    preX = x;
                    preY = y;
                }


                break;
            case MotionEvent.ACTION_UP:

                if (moveStamp != null){
                    updateStampLocation();
                    moveStamp = null;
                }

                break;
        }
        return true;
    }

     private Stamp findMoveStamp(int x , int y){
         for (int i = stamps.size() -1 ; i>=0 ; i--) {
             Stamp stamp = stamps.get(i);
             ImageView imageView = stamp.imageView;
             if (x>imageView.getLeft() && x <imageView.getRight()
                     && y>imageView.getTop() && y<imageView.getBottom()){
                 return stamp;
             }
         }
         return null;
     }

     private void moveStamp(int offsetX , int offsetY){
        moveStamp.imageView.offsetTopAndBottom(offsetY);
        moveStamp.imageView.offsetLeftAndRight(offsetX);
     }

     private void updateStampLocation(){
        moveStamp.updateRect();
     }

    private class Stamp{
        ImageView imageView;
        Bitmap bitmap;
        boolean isCenter;
        Rect rect;

        public Stamp(Context context ,Bitmap bitmap){
            this.bitmap = bitmap;
            imageView = new ImageView(context);
            imageView.setImageBitmap(bitmap);
            rect = new Rect();
            imageView.setTag(rect);
        }

        void release(){
            bitmap.recycle();
        }

        public void setRect(int l , int t , int r , int b) {
            this.rect.left = l;
            rect.top = t;
            rect.right = r;
            rect.bottom = b;
        }

        void updateRect(){
            rect.left = imageView.getLeft();
            rect.top = imageView.getTop();
            rect.right = imageView.getRight();
            rect.bottom = imageView.getBottom();
        }
    }
}
