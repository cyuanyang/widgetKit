package com.cyy.canvasview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by study on 17/7/10.
 *
 */

class Tracker {

    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity;

    public Tracker(Context context){
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    void initVelocityTrackerIfNotExists(MotionEvent event){
        if (mVelocityTracker==null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }
    void recycleVelocityTracker(){
        if (mVelocityTracker!=null){
            mVelocityTracker.recycle();
            mVelocityTracker=null;
        }
    }

    int velocity(){
        mVelocityTracker.computeCurrentVelocity(1000 ,mMaximumVelocity );
        float xVel = mVelocityTracker.getXVelocity();
        float yVel = mVelocityTracker.getYVelocity();
        int velocity = (int) Math.sqrt(xVel*xVel + yVel*yVel);


        return velocity;
    }
}
