package com.cyy.canvasview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
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
            this.isBegin = true;
            this.canvasView = layerCanvas.getCanvasView();
            //根据画板的信息来创建新图层 图层的大小 和 画板一样大 位置也一样
            canvasViewInfo = layerCanvas.getCanvasView().getCanvasViewInfo();
            FrameLayout.LayoutParams lp = new LayoutParams((int) (canvasView.getWidth() * canvasViewInfo.scale), ViewGroup.LayoutParams.MATCH_PARENT);
            lp.gravity = Gravity.CENTER;
            layerCanvas.addLayer(this , lp);

            rotate(canvasViewInfo.rotateAngle , canvasViewInfo.rotateScale);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
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
        super.dispatchDraw(canvas);
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

    /**
     * 获取当前画布的旋转角度
     * @return rotateAngle
     */
    public float canvasViewrotateAngle(){
        return canvasView.getCanvasViewInfo().rotateAngle;
    }

    abstract public String getIdentify();


}
