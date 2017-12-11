package com.xstudy.pagerindicator;

import android.view.View;
import android.widget.TextView;

/**
 * Created by study on 17/12/11.
 *
 */

public class Indicator implements IndicatorInternal {

    public Indicator(){

    }

    @Override
    public void selectTitle(TextView preTitle, TextView currentTitle, int color, int heightColor) {
        if (preTitle!=null){
            preTitle.setTextColor(color);
        }
        currentTitle.setTextColor(heightColor);
    }

    @Override
    public void select(TextView preTextView, TextView currentTextView, View indicator) {

    }
}
