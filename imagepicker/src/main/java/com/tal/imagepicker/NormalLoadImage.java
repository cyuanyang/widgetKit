package com.tal.imagepicker;

import android.widget.ImageView;

import com.tal.imagepicker.utils.ImageLoader;

/**
 * Created by cyy on 2016/7/5.
 *
 *
 */
public class NormalLoadImage implements LoadImageDelegate {

    @Override
    public void loadImage(ImageView view , String url) {
        ImageLoader.getInstance().loadImage(url ,view);
    }

    @Override
    public void loadImageBigger(ImageView view , String path , int reqWidth , int reqHeight){
        ImageLoader.getInstance().loadImage(path ,true , view , reqWidth , reqHeight);
    }
}
