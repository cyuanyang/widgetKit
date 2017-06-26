package com.cyy.slidefinish

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import android.widget.Scroller

/**
 * Created by study on 17/6/26.
 *
 * Slide Finish Layout
 */
class SlideFinishLayout:FrameLayout{

    val TAG:String = "SlideFinishLayout"
    val MIN:Int = 100 //px 最小滑动的距离 超出这个认为是滑动返回

    var mScroller:Scroller
    var mIsDrag:Boolean = false
    var mLastX:Int = 0
    var mTouchSlop:Int = 0

    var mCurrentOffset:Int = 0

    init {
        Log.e(TAG , "init")
        mScroller = Scroller(context)
    }

    constructor(context: Context?) : super(context){
        initMyView()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initMyView()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initMyView()
    }


    fun initMyView(){
        Log.e(TAG , "initMyView")
        var viewConfig:ViewConfiguration = ViewConfiguration.get(context)
        mTouchSlop = viewConfig.scaledTouchSlop
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var action = ev.action
        when (action){
            MotionEvent.ACTION_DOWN -> {
                mLastX = ev.x.toInt()
                mIsDrag = false
            }
            MotionEvent.ACTION_MOVE -> {
                mScroller.computeScrollOffset()
                mIsDrag = !mScroller.isFinished

                var deltaY:Int = ev.x.toInt() - mLastX
                if (deltaY >= mTouchSlop){
                    mIsDrag = true
                }
            }
        }
        return mIsDrag
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        var action = event.action

        when(action){
            MotionEvent.ACTION_DOWN -> {
                mLastX = event.x.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                var currentX = event.x.toInt()
                var deltaX = currentX - mLastX
                performDrag(deltaX)
                mLastX = currentX
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL ->{
                mLastX = 0
                mIsDrag = false
                mCurrentOffset = 0
            }
        }
        return true
    }

    //开始拖动
    fun performDrag(x:Int){
        Log.e(TAG , "x == $x offset = $mCurrentOffset")
        mCurrentOffset += x
        offsetLeftAndRight(mCurrentOffset)
        Log.e(TAG , "mCurrentOffset = $mCurrentOffset")
    }
}


