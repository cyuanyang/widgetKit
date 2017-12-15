package com.cyy.jmathview

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View

/**
 * Created by cyy on 17/10/19.
 *
 */

internal inline fun c_log(msg:String){
    Log.i("JLaTexMath" , msg)
}

internal inline fun screenWidth(context:Context) = context.resources.displayMetrics.widthPixels

internal fun View.dp2px(dp:Int):Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(), context.resources.displayMetrics).toInt()

