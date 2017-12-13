package com.cyy.canvasview;

import android.content.Context;
import android.widget.FrameLayout;

/**
 * Created by study on 17/12/13.
 *
 *
 */

public abstract class Layer extends FrameLayout {

    private String identify;
    private boolean isBegin;

    public Layer(Context context){
        super(context);

        identify = getIdentify();
        setDrawingCacheEnabled(true);
    }

    //开启一个图层
    public void begin(LayerCanvas layerCanvas){
        if (!isBegin){

            layerCanvas.addLayer(this);
            this.isBegin = true;
        }
    }

    public boolean isBegin(){
        return isBegin;
    }

    //合并图层
    public void end(LayerCanvas layerCanvas){
        if (isBegin){
            layerCanvas.mergeLayer(this);
            layerCanvas.removeView(this);
            reset();
            isBegin = false;
        }
    }

    protected void reset(){

    }

    abstract public String getIdentify();
}
