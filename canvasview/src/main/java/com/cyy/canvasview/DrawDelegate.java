package com.cyy.canvasview;

import android.graphics.Canvas;

/**
 * Created by chenyuanyang on 2017/5/28.
 */

public interface DrawDelegate {

    void canvasScale(Canvas canvas);


    void onDraw(Canvas canvas);

}
