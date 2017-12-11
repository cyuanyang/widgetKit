package com.xstudy.pagerindicator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by study on 17/12/11.
 *
 * 布局内容和指示的位置关系
 *
 * 1.指示View覆盖在内容上  内容充满父View
 *
 * 2.指示View在内容的下面  内容与指示View共同充满父View
 */

class ContentLayout extends FrameLayout {

    boolean overlap; //

    public ContentLayout(@NonNull Context context) {
        super(context);
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child , index , params);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (overlap){
            //覆盖

        }else {

        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
