package com.tal.imagepicker.ui;

/**
 * Created by shawn on 2017/12/29.
 */

public interface Callback {

    //底部按钮事件
    interface BottomCallback{
        void dirNameEvent();

        void originalImage(boolean original);

        void previewImageEvent();
    }
}
