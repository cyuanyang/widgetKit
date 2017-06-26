package com.cyy.slidefinish

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout

/**
 * Created by study on 17/6/26.
 *
 * Slide Finish Layout
 */
class SlideFinishLayout:FrameLayout{

    var TAG:String = "SlideFinishLayout"

    constructor(context: Context?) : super(context){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init()
    }


    fun init(){
        Log.e(TAG , "init")

    }
}


