package com.cyy.canvasview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by study on 17/12/13.
 *
 * 数据保存的问题
 * 意外销毁正在编辑的图层保存问题
 */

public class LayerCanvas extends FrameLayout {

    //绘画图层
    private CanvasView canvasView;

    public LayerCanvas(@NonNull Context context) {
        super(context);
        init();
    }

    public LayerCanvas(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LayerCanvas(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        canvasView = new CanvasView(getContext());
        addView(canvasView);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public CanvasView getCanvasView() {
        return canvasView;
    }

    public void addLayer(Layer layer){
        addView(layer);
    }

    public void addLayer(Layer layer , LayoutParams lp){
        addView(layer , lp);
    }

    public void mergeLayer(Layer layer){
        Bitmap bitmap = layer.getDrawingCache();
        canvasView.mergeLayer(bitmap);
    }
}
