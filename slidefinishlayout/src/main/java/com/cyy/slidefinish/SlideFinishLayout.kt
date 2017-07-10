package com.cyy.slidefinish

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.FrameLayout
import android.widget.OverScroller

/**
 * Created by study on 17/6/26.
 *
 * Slide Finish Layout
 */
class SlideFinishLayout:FrameLayout{

    interface SlideCallback{
        fun onSliding(deltaX:Int , currentX:Int , state:SlideState) //滑动监听
        fun onSlidingEnd(isReach:Boolean) //滑动结束
    }

    //支持滑动的方向
    enum class DirectionModel {
        ONLY_LEFT,
        ONLY_RIGHT,
        LEFT_AND_RIGHT
    }

    enum class SlideState{
        IDLE , //空闲
        DRAGGING,//拖动
        SETTLING//fling
    }

    internal val TAG:String = "SlideFinishLayout"
    private val FACTOR:Float = 0.2f //px 最小滑动的距离的因数 屏幕的最大宽度*factor
    private var screenWidth:Int

    private var mScroller:OverScroller
    private var mIsDrag:Boolean = false
    private var mLastX:Int = 0
    private var mTouchSlop:Int = 0
    private var isDragLeft = true //向左滑动＝true  向右滑动＝false
    private var isReachFinish = false //是否到达关闭的目标
    private var velocityTracker:VelocityTracker?
    private var mMaximumVelocity:Float
    private var mMinimumVelocity:Float

    var slideModel:DirectionModel = DirectionModel.LEFT_AND_RIGHT //滑动模式
    //滑动的状态
    var slideState:SlideState = SlideState.IDLE
        private set
    var slideListener:SlideCallback? = null
    var finishListener:((SlideFinishLayout)->Unit)? = null

    init {
        Log.e(TAG , "init")
        mScroller = OverScroller(context)
        screenWidth = context.resources.displayMetrics.widthPixels

        val viewConfig:ViewConfiguration = ViewConfiguration.get(context)
        mTouchSlop = viewConfig.scaledTouchSlop
        mMaximumVelocity = viewConfig.scaledMaximumFlingVelocity.toFloat()
        mMinimumVelocity = viewConfig.scaledMinimumFlingVelocity.toFloat()
        velocityTracker = null
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        when (action){
            MotionEvent.ACTION_DOWN -> {
                mLastX = ev.x.toInt()
                mIsDrag = false
            }
            MotionEvent.ACTION_MOVE -> {
                mScroller.computeScrollOffset()
                mIsDrag = !mScroller.isFinished

                val deltaY:Int = ev.x.toInt() - mLastX
                if (deltaY >= mTouchSlop){
                    mIsDrag = true
                }
            }
        }
        return mIsDrag
    }

    private fun initVelocityTrackerIfNotExists(event:MotionEvent){
        if (velocityTracker==null){
            velocityTracker = VelocityTracker.obtain()
        }
        velocityTracker?.addMovement(event)
    }
    private fun recycleVelocityTracker(){
        if (velocityTracker!=null){
            velocityTracker?.recycle()
            velocityTracker=null
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val action = event.action
        initVelocityTrackerIfNotExists(event)

        when(action){
            MotionEvent.ACTION_DOWN -> {
                if (!mScroller.isFinished){
                    mScroller.abortAnimation()
                }

                mLastX = event.rawX.toInt()
                mIsDrag = true
                dispatchScroll(0 , 0 , slideState)
            }
            MotionEvent.ACTION_MOVE -> {
                val currentX = event.rawX.toInt()
                val deltaX = currentX - mLastX
                Log.e(TAG, "x = ${event.rawX} y = ${event.rawX}")
                performDrag(deltaX)
                Log.e(TAG , "deltaX = $deltaX")
                mLastX = currentX
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL ->{
                if (mIsDrag){
                    mLastX = 0
                    mIsDrag = false
                    velocityTracker?.computeCurrentVelocity(1000 , mMaximumVelocity)
                    val velocity = velocityTracker?.xVelocity?.toInt()!!

                    isReachFinish = endDrag(velocity)
                    postInvalidateOnAnimation()

                    recycleVelocityTracker()
                }
            }
        }
        return true
    }

    //开始拖动
    private fun performDrag(x:Int){
        var canOffset = false
        isDragLeft = x > 0
        slideState = SlideState.DRAGGING
        //计算 滚动的 距离
        if (slideModel == DirectionModel.ONLY_LEFT ){
            if (x > 0){
                offsetLeftAndRight(x)
                canOffset = true
            }
        }else if (slideModel == DirectionModel.ONLY_RIGHT){
            if (x<0){
                offsetLeftAndRight(x)
                canOffset = true
            }
        }else{
            offsetLeftAndRight(x)
            canOffset = true
        }

        if (canOffset){
            dispatchScroll(x , left , slideState)
        }
    }

    //手指离开后的动作
    fun endDrag(xVelocity:Int):Boolean{

        slideState = SlideState.SETTLING
        val left = this.left
        Log.e(TAG , "left = $left  screenWidth * FACTOR= ${screenWidth * FACTOR}")
        Log.e(TAG , "xVelocity = $xVelocity mMinimumVelocity= $mMinimumVelocity")
        if (Math.abs(left) > screenWidth * FACTOR
                || xVelocity > mMinimumVelocity){
            if (left>0){
                //左滑动
                mScroller.startScroll(left , 0 , screenWidth - left , 0)
            }else{
                //右滑动
                mScroller.startScroll(left , 0 , left - screenWidth , 0)
            }
            return true
        }else{
            mScroller.startScroll(left , 0 , -left , 0)
            return false
        }
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()){
            Log.e(TAG , "currX ${mScroller.currX}")
            val offsetX = mScroller.currX-left
            offsetLeftAndRight(offsetX)
            dispatchScroll(offsetX , mScroller.currX , slideState)
            postInvalidateOnAnimation()
        }

        if (mScroller.currX == mScroller.finalX ||
                (mScroller.isFinished && slideState != SlideState.IDLE)){
            slideState = SlideState.IDLE
            dispatchScroll(0 , left , slideState)

            if (isReachFinish){
                finishListener?.invoke(this)
            }
            slideListener?.onSlidingEnd(isReachFinish)
        }
    }

    //分发 滚动
    fun dispatchScroll(deltaX:Int , currentX:Int , state:SlideState){
        slideListener?.onSliding(deltaX , currentX , state)
    }

}


