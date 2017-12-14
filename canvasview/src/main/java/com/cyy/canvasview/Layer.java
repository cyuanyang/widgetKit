package com.cyy.canvasview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.widget.FrameLayout;

/**
 * Created by study on 17/12/13.
 * 图层
 *
 * begin 会根据画布的信息穿件一个图层
 *
 * 这里面才可以做图层操作
 *
 * end 关闭图层，释放图层占用资源
 *
 */

public abstract class Layer extends FrameLayout {

    private String identify;
    private boolean isBegin;
    private CanvasView.CanvasViewInfo canvasViewInfo;
    private CanvasView canvasView;

    public Layer(Context context){
        super(context);

        identify = getIdentify();
        setDrawingCacheEnabled(true);
    }

    //开启一个图层
    public void begin(LayerCanvas layerCanvas){
        if (!isBegin){
            //根据画板的信息来创建新图层
            canvasViewInfo = layerCanvas.getCanvasView().getCanvasViewInfo();
            layerCanvas.addLayer(this);
            this.isBegin = true;
            this.canvasView = layerCanvas.getCanvasView();
            rotate(canvasViewInfo.rotateAngle , canvasViewInfo.rotateScale);
        }
    }

    private void rotate(float rotateAngle , float rotateScale){
        if (Math.abs(rotateAngle)/90%2 == 1){
            setScaleX(rotateScale);
            setScaleY(rotateScale);
        }else {
            setScaleX(1);
            setScaleY(1);
        }

        setRotation(rotateAngle);
        canvasViewInfo.rotateAngle = rotateAngle;
    }

    public boolean isBegin(){
        return isBegin;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.concat(canvasViewInfo.matrix);
        canvas.clipRect(0,0,canvasViewInfo.width , canvasViewInfo.height);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    //合并图层
    public void end(LayerCanvas layerCanvas){
        if (isBegin){
            layerCanvas.mergeLayer(this);
            layerCanvas.removeView(this);
            reset();
            isBegin = false;
            this.canvasView = null;
        }
    }

    protected void reset(){

    }

    public CanvasView getCanvasView() {
        return canvasView;
    }

    abstract public String getIdentify();


}
