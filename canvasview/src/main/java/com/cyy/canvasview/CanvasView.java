package com.cyy.canvasview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chenyuanyang on 2017/5/28.
 *
 * 具有涂鸦功能的View
 *
 *
 */

public class CanvasView extends View implements ImageLoadDelegate.LoadImageCallback{

    private ImageLoadDelegate.LoadImage mLoadImage;
    private DrawHelper drawHelper;

    private Bitmap originBitmap;//最初的bitmap
    private Bitmap canvasBitmap;//画布的bitmap
    private Canvas mCanvas;

    private Paint mPenPaint; //笔触的paint
    private BitmapShader easerShader;//橡皮擦的shader

    private Matrix canvasMatrix;//画布缩放的矩阵

    private boolean isPainting; //是否正在画

    private PathMap mPathMap;

    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        drawHelper = new DrawHelper();

        mPenPaint = new Paint();
        mPenPaint.setAntiAlias(true);
        mPenPaint.setStyle(Paint.Style.STROKE);
        mPenPaint.setStrokeCap(Paint.Cap.ROUND);
        mPenPaint.setStrokeWidth(20);
        mPenPaint.setColor(Color.RED);

        mPathMap = new PathMap();
        canvasMatrix = new Matrix();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setmLoadImage(ImageLoadDelegate.LoadImage mLoadImage , String url) {
        this.mLoadImage = mLoadImage;
        mLoadImage.loadImage(this.getContext() , url , this);
    }

    @Override
    public void imageCallback(Bitmap bitmap) {
        if (bitmap == null){
            throw new NullPointerException("图片下载的bitmap为空");
        }
        originBitmap = bitmap;
        initCanvas();
        invalidate();
        requestLayout();
    }

    private void initCanvas(){
        canvasBitmap = originBitmap.copy(Bitmap.Config.RGB_565 , true);
        mCanvas = new Canvas(canvasBitmap);
        easerShader = new BitmapShader(canvasBitmap.copy(Bitmap.Config.RGB_565 , true) , Shader.TileMode.REPEAT , Shader.TileMode.REPEAT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //计算缩放系数和平移
        if (originBitmap!=null) {
            canvasMatrix.reset();
            int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

            int originBitmapWidth = originBitmap.getWidth();
            int originBitmapHeight = originBitmap.getHeight();

            int max = Math.max(originBitmapWidth, originBitmapHeight);
            float scale;

            if (max == originBitmapHeight) {
                scale = heightSpec * 1.0f / max;
                canvasMatrix.postTranslate((widthSpec-originBitmapWidth*scale)/2/scale , 0);
            } else {
                scale = widthSpec * 1.0f / max;
            }

            canvasMatrix.postScale(scale , scale);
//            canvasMatrix.postRotate(90 , widthSpec/2 , heightSpec/2);
            mPathMap.resetPathMapMatrix(canvasMatrix);
        }
    }

    public void easer(){
        mPenPaint.setShader(easerShader);
    }

    public void pen(){
        mPenPaint.setShader(null);
    }

    float r = 0;
    public void rorate(){
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "rotation", r, r+90f);
        anim.setDuration(100);
        anim.start();
        r = r+90f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int count = canvas.save();
        if (canvasBitmap!=null){

            float[] values = new float[9];
            canvasMatrix.getValues(values);
            Log.i("scale" , "scale="+values[Matrix.MSCALE_X]);
            canvas.concat(canvasMatrix);
            //canvasBitmap以外区域不让画 让画的话需要在优化
//            canvas.clipRect(0,0,canvasBitmap.getWidth() , canvasBitmap.getHeight());
            canvas.drawBitmap(canvasBitmap , 0 , 0 , null);
        }
        if (isPainting){
            canvas.drawPath(mPathMap.getPaintingPath() , mPenPaint);
        }
        canvas.restoreToCount(count);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                isPainting = true;
                mPathMap.reset();
                mPathMap.setDownXY(event.getX() , event.getY())
                        .moveTo(mPathMap.downX() , mPathMap.downY());
                break;

            case MotionEvent.ACTION_MOVE:
                mPathMap.mapPath(event.getX() , event.getY())
                .setDownXY(event.getX() , event.getY());
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isPainting = false;
                //将路径花到画布上
                mCanvas.drawPath(mPathMap.getPaintingPath() , mPenPaint);
                break;
        }
        return true;
    }
}
