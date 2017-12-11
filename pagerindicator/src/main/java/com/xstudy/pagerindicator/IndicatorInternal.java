package com.xstudy.pagerindicator;

import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

/**
 * Created by study on 17/12/11.
 *
 */

public interface IndicatorInternal {

    void selectTitle(@Nullable TextView preTitle , TextView currentTitle , @ColorInt int color , @ColorInt int heightColor);

    void select(TextView preTextView , TextView currentTextView , View indicator);



}


