package com.cyy.slidefinish

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View

/**
 * Created by study on 17/7/11.
 *
 */
class Slide private constructor(){

    init { println("This ($this) is a singleton") }

    private object Holder { val INSTANCE = Slide() }

    companion object {
        val instance: Slide by lazy { Holder.INSTANCE }
    }

    internal val activitys:MutableList<Activity?> = emptyList<Activity?>().toMutableList()
    //初始化
    fun init(app:Application){
        app.registerActivityLifecycleCallbacks(LifecycleCallback())
    }
}

class LifecycleCallback: Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity?) {
        var view = activity?.window?.decorView?.findViewWithTag(SlideFinishLayout.tag) as? SlideFinishLayout
        view?.let{
            view.parallaxView = null
        }
    }

    override fun onActivityResumed(activity: Activity?) {
        var view = activity?.window?.decorView?.findViewWithTag(SlideFinishLayout.tag) as? SlideFinishLayout
        view?.let{
            val index = Slide.instance.activitys.indexOf(activity) -1
            if (Slide.instance.activitys.count() -1 > index){
                val desActivity = Slide.instance.activitys[index]
                val desView = desActivity?.window?.decorView?.findViewWithTag(SlideFinishLayout.tag)
                if (desView is SlideFinishLayout){
                    view.parallaxView = desView
                }
            }
        }
    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityDestroyed(activity: Activity?) {
        Slide.instance.activitys.remove(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        Slide.instance.activitys. add(activity)
    }

    override fun onActivityStopped(activity: Activity?) {

    }

}

