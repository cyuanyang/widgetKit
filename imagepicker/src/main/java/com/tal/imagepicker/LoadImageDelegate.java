package com.tal.imagepicker;

import android.widget.ImageView;

/**
 * Created by cyy on 2016/7/5.
 *
 */
public interface LoadImageDelegate {
    void loadImage(ImageView view, String url);

    void loadImageBigger(ImageView view, String path, int reqWidth, int reqHeight);
}
