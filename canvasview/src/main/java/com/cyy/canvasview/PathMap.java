package com.cyy.canvasview;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

/**
 * Created by chenyuanyang on 2017/5/28.
 *
 * 路径的映射类
 * 处理触摸点到图片坐标系的映射
 */

public class PathMap {

    private float mScale;//缩放系数

    private int translationX , translationY;

    private int downX , downY; //计算系数之后的按下X Y

    private Path paintingPath;//正在画的路径
    private Path tempPath; //获取每一段的path 目前用于橡皮擦出 也可以做出画笔的粗细

    public PathMap(){
        paintingPath = new Path();
        tempPath = new Path();
    }

    public void resetPathMapMatrix(Matrix matrix){
        float[] values = new float[9];
        matrix.getValues(values);
        this.mScale = values[Matrix.MSCALE_X]; //缩放 x y 是一样的
        this.translationX = (int) values[Matrix.MTRANS_X];
        this.translationY = (int) values[Matrix.MTRANS_Y];
        
    }

    public void reset(){
        paintingPath.reset();
        tempPath.reset();
        downX = 0;
        downY = 0;
    }

    public PathMap moveTo(int x , int y){
        Log.e("TAG" , "move to =" + x + " ," + y);
        paintingPath.moveTo(x , y);
        tempPath.moveTo(x , y);
        return this;
    }

    public PathMap mapPath(float eventX , float eventY){
        int x = (int) ((downX+toX(eventX))/2);
        int y = (int) ((downY+toY(eventY))/2);
        Log.e("TAG" , "quadTo = downX =" + downX + " downY =" + downY + " x="+x +" y="+y);
        paintingPath.quadTo(downX , downY , x , y );
        tempPath.reset();
        tempPath.moveTo(downX , downY);
        tempPath.quadTo(downX , downY , x , y);
        return this;
    }

    public PathMap setDownXY(float eventX , float eventY){
        this.downX = (int) toX(eventX);
        this.downY = (int) (toY(eventY));
        Log.e("TAG" , "setDownXY = downX =" + downX + " downY =" + downY );
        return this;
    }

    //屏幕坐标系转换画布坐标系
    public float toX(float eventX){
        return (eventX-translationX)/mScale;
    }
    public float toY(float eventY){
        return (eventY-translationY)/mScale;
    }

    public int downX() {
        return downX;
    }

    public int downY() {
        return downY;
    }

    public Path getPaintingPath() {
        return paintingPath;
    }

    public Path getTempPath(){
        return tempPath;
    }
}
