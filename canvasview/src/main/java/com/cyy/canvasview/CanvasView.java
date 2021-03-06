package com.cyy.canvasview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Build;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenyuanyang on 2017/5/28.
 *
 * 具有涂鸦功能的View
 */
// TODO: 17/6/1 数据恢复怎么保存 画笔在程序意外退出后没有保存
public class CanvasView extends View implements ImageLoadDelegate.LoadImageCallback{

    private static final boolean DEBUG = true;

    private static final int ERASER_MAX_WIDTH = 30;
    private static final int ROTATE_DURATION = 200;
    private static final int ERASER_WIDTH_FACTOR = 2; //橡皮和笔的比率

    private ImageLoadDelegate.LoadImage mLoadImage;
    private DrawHelper drawHelper;

    private int strokeWidth = 5; //画笔的宽度dp
    private int originEraserWidth = strokeWidth*3;//橡皮的宽度 dp;
    private int eraserWidth = originEraserWidth;

    private Bitmap originBitmap;//最初的bitmap
    Bitmap canvasBitmap;//画布的bitmap
    private Canvas mCanvas;

    private Paint mPenPaint; //笔触的paint
    private Paint mEraserCirclePaint;
    private BitmapShader easerShader;//橡皮擦的shader
    private int circleColor = Color.parseColor("#8e8e8e");


    private Matrix canvasMatrix;//画布缩放的矩阵
    private boolean isPainting; //是否正在画
    private PathMap mPathMap;
    private float rotateAngle;//旋转的角度
    private float rotateScale;//旋转时缩放系数
    private float currentRotateSacle;//旋转时 当前的缩放系数
    private boolean isChanged;//true 对传入的图片做了操作
    private HistoryPath historyPath;//记录历史记录的操作

    private boolean isEraser = false; //是否处于橡皮状态

    private Tracker mTracker;

    private CanvasViewInfo canvasViewInfo = new CanvasViewInfo();

    //todo 意外退出数据保存
    static class SaveState extends BaseSavedState{
        public SaveState(Parcel source) {
            super(source);
        }
    }

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
        float w = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , strokeWidth , getResources().getDisplayMetrics());
        mPenPaint.setStrokeWidth(w);
        mPenPaint.setColor(Color.RED);

        mEraserCirclePaint = new Paint();
        mEraserCirclePaint.setAntiAlias(true);
        mEraserCirclePaint.setStyle(Paint.Style.STROKE);
        mEraserCirclePaint.setColor(circleColor);

        mPathMap = new PathMap();
        canvasMatrix = new Matrix();

        mTracker = new Tracker(getContext());
        historyPath = new HistoryPath();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setmLoadImage(ImageLoadDelegate.LoadImage mLoadImage , String url) {
        this.mLoadImage = mLoadImage;
        mLoadImage.loadImage(this.getContext() , url , this);
    }

    /**
     * 设置原始的bitmap
     * @param oBitmap 原始的bitmap
     */
    public void setOriginBitmap(Bitmap oBitmap){
        //清除内存
        if (originBitmap!=null && !originBitmap.isRecycled()){
            originBitmap.recycle();
            originBitmap = null;
        }
        try {
            originBitmap = oBitmap.copy(Bitmap.Config.RGB_565 , true); //原来的图片副本给这个类用
        }catch (OutOfMemoryError e){
            Toast.makeText(getContext() , "请重新加载图片",Toast.LENGTH_SHORT).show();
            return;
        }

        initCanvasShader();
        invalidate();
        requestLayout();
    }

    @Override
    public void imageCallback(Bitmap bitmap) {
        if (bitmap == null){
            throw new NullPointerException("图片下载的bitmap为空");
        }
        setOriginBitmap(bitmap);
    }

    private void initCanvasShader(){
        initCanvas();
        easerShader = new BitmapShader(canvasBitmap.copy(Bitmap.Config.RGB_565 , true) , Shader.TileMode.REPEAT , Shader.TileMode.REPEAT);

        //笔触和橡皮的宽度 根据图片的宽度来自适应涂鸦的宽度
        strokeWidth = canvasBitmap.getWidth()/150;
        eraserWidth = strokeWidth*ERASER_WIDTH_FACTOR;
        mPenPaint.setStrokeWidth(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , strokeWidth , getResources().getDisplayMetrics()));
    }

    private void initCanvas(){
        if (canvasBitmap!=null && !canvasBitmap.isRecycled()){
            canvasBitmap.recycle();
            canvasBitmap = null;
        }
        try {
            canvasBitmap = originBitmap.copy(Bitmap.Config.RGB_565 , true);
        }catch (OutOfMemoryError e){
            Toast.makeText(getContext() , "请重新加载图片" , Toast.LENGTH_SHORT).show();
            return;
        }

        canvasViewInfo.width = canvasBitmap.getWidth();
        canvasViewInfo.height = canvasBitmap.getHeight();

        mCanvas = new Canvas(canvasBitmap);
        easerShader = new BitmapShader(canvasBitmap.copy(Bitmap.Config.RGB_565 , true) , Shader.TileMode.REPEAT , Shader.TileMode.REPEAT);

        //笔触和橡皮的宽度 根据图片的宽度来自适应涂鸦的宽度
        strokeWidth = canvasBitmap.getWidth()/150;
        originEraserWidth = strokeWidth*ERASER_WIDTH_FACTOR;
        mPenPaint.setStrokeWidth(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , strokeWidth , getResources().getDisplayMetrics()));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //计算缩放系数和平移
        //主要将图片放倒View中间的位置 图片不能有裁剪
        if (originBitmap!=null) {
            canvasMatrix.reset();
            int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

            int originBitmapWidth = originBitmap.getWidth();
            int originBitmapHeight = originBitmap.getHeight();

            float viewScale = widthSpec*1.0f/heightSpec;
            float bitmapViewScale = originBitmapWidth*1.0f/originBitmapHeight;
            float max = Math.max(viewScale, bitmapViewScale);
            float scale;

            if (max == viewScale) {
                //缩放平移到中间
                scale = heightSpec * 1.0f / originBitmapHeight;
                canvasMatrix.postTranslate((widthSpec-originBitmapWidth*scale)/2/scale , 0);
                //计算旋转缩放系数
                rotateScale = calculateRotate(originBitmapWidth, originBitmapHeight ,
                        getMeasuredWidth() , getMeasuredHeight() , true);
                canvasViewInfo.isScaleDependOnWidth = false;
            } else {
                //缩放平移到中间
                scale = widthSpec * 1.0f / originBitmapWidth;
                canvasMatrix.postTranslate(0 , (heightSpec-originBitmapHeight*scale)/2/scale);
                //计算旋转缩放系数
                rotateScale = calculateRotate(originBitmapWidth, originBitmapHeight ,
                        getMeasuredWidth() , getMeasuredHeight() , false);
                canvasViewInfo.isScaleDependOnWidth = true;
            }

            canvasMatrix.postScale(scale , scale);
            mPathMap.resetPathMapMatrix(canvasMatrix);

            canvasViewInfo.rotateScale = rotateScale;
            canvasViewInfo.scale = scale;
        }

//        //计算缩放系数和平移
//        if (originBitmap!=null) {
//            canvasMatrix.reset();
//            int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
//            int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
//
//            int originBitmapWidth = originBitmap.getWidth();
//            int originBitmapHeight = originBitmap.getHeight();
//
//            int max = Math.max(originBitmapWidth, originBitmapHeight);
//            float scale;
//
//            if (max == originBitmapHeight) {
//                //缩放平移到中间
//                scale = heightSpec * 1.0f / max;
//                canvasMatrix.postTranslate((widthSpec-originBitmapWidth*scale)/2/scale , 0);
//                //计算旋转缩放系数
//                rotateScale = calculateRotate(originBitmapWidth, originBitmapHeight ,
//                        getMeasuredWidth() , getMeasuredHeight() , true);
//                canvasViewInfo.isScaleDependOnWidth = false;
//            } else {
//                //缩放平移到中间
//                scale = widthSpec * 1.0f / max;
//                canvasMatrix.postTranslate(0 , (heightSpec-originBitmapHeight*scale)/2/scale);
//                //计算旋转缩放系数
//                rotateScale = calculateRotate(originBitmapWidth, originBitmapHeight ,
//                        getMeasuredWidth() , getMeasuredHeight() , false);
//                canvasViewInfo.isScaleDependOnWidth = true;
//            }
//
//            canvasMatrix.postScale(scale , scale);
//            mPathMap.resetPathMapMatrix(canvasMatrix);
//
//            canvasViewInfo.rotateScale = rotateScale;
//            canvasViewInfo.scale = scale;
//        }
    }

    /**
     * 计算旋转缩放系数
     */
    private float calculateRotate(int bitmapWidth , int bitmapHeight , int viewWidth , int viewHeight , boolean isVertical){
        if (isVertical){
            float viewScale = viewWidth*1f/viewHeight;
            float bitmapScale = bitmapHeight*1f/bitmapWidth;

            return Math.min(viewScale , bitmapScale);
        }else {
            float viewScale = viewHeight*1f/viewWidth;
            float bitmapScale = bitmapHeight*1f/bitmapWidth;
            return Math.max(viewScale , bitmapScale);
        }
    }

    /**
     * 设置笔的宽度 橡皮的宽度为笔的三倍
     * @param penWidth 单位为dp
     */
    public void setPenWidth(int penWidth){
        strokeWidth = penWidth;
        originEraserWidth = strokeWidth*ERASER_WIDTH_FACTOR;
        mPenPaint.setStrokeWidth(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , strokeWidth , getResources().getDisplayMetrics()));
    }

    /**
     * 橡皮
     */
    public void eraser(){
        mPenPaint.setStrokeWidth(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , eraserWidth , getResources().getDisplayMetrics()));
        mPenPaint.setShader(easerShader);
        isEraser = true;
    }

    private void setEraserWidth(int width){
        mPenPaint.setStrokeWidth(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , width , getResources().getDisplayMetrics()));
    }

    /**
     * 画笔
     */
    public void pen(){
        pen(0);
    }

    public void pen(int color){
        if (color!=0){
            mPenPaint.setColor(color);
        }
        mPenPaint.setStrokeWidth(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , strokeWidth , getResources().getDisplayMetrics()));
        mPenPaint.setShader(null);
        isEraser = false;
    }

    /**
     * 旋转View 非画布
     * @param clockwise true 顺时针 false 逆时针
     */
    public void rotate(boolean clockwise){
        float toDegree ;
        float currentDegree = rotateAngle;
        if (clockwise){
            toDegree = currentDegree+90f;
        }else {
            toDegree = currentDegree-90f;
        }
        rotateAngle = toDegree%360==0?0:toDegree;
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator scaleAnimX = null;
        ObjectAnimator scaleAnimY = null;
        if (Math.abs(toDegree)/90%2 == 1){
            //横着 将原来的高度缩放到宽度到校
            scaleAnimX = ObjectAnimator.ofFloat(this, "scaleX", 1f, rotateScale);
            scaleAnimX.setDuration(ROTATE_DURATION);
            scaleAnimY = ObjectAnimator.ofFloat(this, "scaleY", 1f, rotateScale);
            scaleAnimY.setDuration(ROTATE_DURATION);
            currentRotateSacle = rotateScale;
        }else {
            if (currentRotateSacle!=1){
                scaleAnimX = ObjectAnimator.ofFloat(this, "scaleX", rotateScale, 1f);
                scaleAnimX.setDuration(ROTATE_DURATION);
                scaleAnimY = ObjectAnimator.ofFloat(this, "scaleY", rotateScale, 1f);
                scaleAnimY.setDuration(ROTATE_DURATION);
                currentRotateSacle = 1f;
            }
        }
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "rotation", currentDegree, toDegree);
        anim.setDuration(ROTATE_DURATION);
        if (scaleAnimX!=null){
            set.playTogether(anim , scaleAnimX , scaleAnimY);
        }else {
            set.playTogether(anim);
        }
        set.start();

        canvasViewInfo.rotateAngle = rotateAngle;
    }

    /**
     * 画橡皮
     * @param canvas
     */
    private void drawEraser(Canvas canvas){
        float w = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , 2 , getResources().getDisplayMetrics());
        mEraserCirclePaint.setStrokeWidth(w);
        float radius = mPenPaint.getStrokeWidth();
        canvas.drawCircle(mPathMap.downX() , mPathMap.downY() , radius , mEraserCirclePaint);
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
            canvas.clipRect(0,0,canvasBitmap.getWidth() , canvasBitmap.getHeight());
            canvas.drawBitmap(canvasBitmap , 0 , 0 , null);
        }
        if (isPainting){

            if (isEraser){
                drawEraser(canvas);
            }else {
                canvas.drawPath(mPathMap.getPaintingPath() , mPenPaint);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("CanvasView" , " Canvas View onDetachedFromWindow ");
        if (originBitmap!=null&&!originBitmap.isRecycled()){
            originBitmap.recycle();
            originBitmap = null;
        }
        if (canvasBitmap!=null && !canvasBitmap.isRecycled()){
            canvasBitmap.recycle();
            canvasBitmap = null;
        }

    }

    /**
     * 合并图层
     *
     * 将指定图层于 mCanvas 进行合并
     */
    public void mergeLayer(Bitmap bitmap){
        if (bitmap != null && !bitmap.isRecycled()) {

            //矩阵求逆
            Matrix temMatrix = new Matrix();
            canvasMatrix.invert(temMatrix);

            //得到缩放比
            float[] values = new float[9];
            temMatrix.getValues(values);
            float scale = values[Matrix.MSCALE_X];

            //还原图片
            Matrix matrix = new Matrix();
            matrix.postScale(scale , scale);
            mCanvas.drawBitmap(bitmap, matrix, null);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isEraser){
            mTracker.initVelocityTrackerIfNotExists(event);
        }

        if (originBitmap==null){
            return true;
        }
        int action = event.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                isPainting = true;
                mPathMap.reset();
                mPathMap.setDownXY(event.getX() , event.getY())
                        .moveTo(mPathMap.downX() , mPathMap.downY());
                break;

            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                mPathMap.mapPath(x , y)
                        .setDownXY(x , y);
                if (isEraser){
                    //获取速率
                    int vel = mTracker.velocity();
                    eraserWidth = originEraserWidth + vel/100;
                    eraserWidth = eraserWidth > ERASER_MAX_WIDTH ? ERASER_MAX_WIDTH : eraserWidth;
                    setEraserWidth(eraserWidth);
                    mCanvas.drawPath(mPathMap.getTempPath() , mPenPaint);
                }
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isPainting = false;
                //将路径花到画布上
                Path path = mPathMap.getPaintingPath();
                historyPath.addPath(path);
                mCanvas.drawPath(path , mPenPaint);
                isChanged = true;
                invalidate();
                if (isEraser){
                    mTracker.recycleVelocityTracker();
                }
                eraserWidth = originEraserWidth;
                break;
        }
        return true;
    }

    public boolean isHasPre(){
        return !historyPath.isEmpty();
    }

    /**
     * 上一步
     */
    public void pre(){
        if (isHasPre()){
            initCanvas();
            mCanvas.drawPath(historyPath.getPrePath() , mPenPaint);
            invalidate();
        }
    }

    public Bitmap getResultBitmap(){
        return canvasBitmap;
    }

    /*
     * 保存所绘图形
     * 返回绘图文件的存储路径
     *
     * */
    public File saveBitmap(File path , String fileName) {
        if (TextUtils.isEmpty(fileName)){
            //获得系统当前时间，并以该时间作为文件名
            fileName = System.currentTimeMillis()+"";
        }

        if (path == null){
            path = getContext().getExternalFilesDir("files");
        }

        File file = new File(path, fileName+".jpg");
        if (!path.exists()) {
            path.mkdirs();
        } else {
            if (file.exists()) {
                file.delete();
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            canvasBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            //保存绘图文件路径
            isChanged = false;
            return file;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对传入的是否进行过绘制
     * @return true 绘制过 false 没有对图片进行任何操作
     */
    public boolean isChanged() {
        return isChanged;
    }

    public CanvasViewInfo getCanvasViewInfo(){
        return canvasViewInfo;
    }


    static class CanvasViewInfo{
        int width;
        int height;
        float rotateAngle = 0f; //画布旋转的角度
        float rotateScale = 0f;
        float scale = 0.0f; //缩放系数
        boolean isScaleDependOnWidth = false;  //true 按照宽度最大缩放  false 反之
    }
}
