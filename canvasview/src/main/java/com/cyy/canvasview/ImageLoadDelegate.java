package com.cyy.canvasview;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by chenyuanyang on 2017/5/28.
 * 网络请求图片代理
 */

public interface ImageLoadDelegate {

    interface LoadImage{
        void loadImage(Context context , String url , LoadImageCallback callback);
    }

    interface LoadImageCallback{
        void imageCallback(Bitmap bitmap);
    }
}
