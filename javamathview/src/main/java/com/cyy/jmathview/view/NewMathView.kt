package com.cyy.jmathview.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.cyy.jmathview.parser.*

/**
 * Created by study on 17/11/29.
 *
 * NewMathView不要使用warp_content
 */

/**
 *
 * 对的LatexView的宽度计算
 *
 * 父View计算传入的宽度 就是NewMathView的宽度
 *
 * 一般来说LatexView是于NewMathView相等的
 * LatexView 的宽度的确定有两个因素 一个是LatexView的
 *
 */

/**
 * 存在的问题 list view 复用的问题
 */
class NewMathView : HorizontalScrollView , ViewDelegate {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var contentView: LinearLayout? = null
    private val latexViews = mutableListOf<JLatexView>() //所有的latexView
    private val views = mutableListOf<View>() //所有的imageView

    //默认的tag
    private var tags = mutableListOf<Tag>()

    //处理view的
    var text:String = ""
        set(value) {
            field = value
            parseText()
        }

    init {
        //初始化content view
        initContentView()
        initTags()
    }

    private fun initContentView(){
        //横向的滚动
        contentView = ContentView(context)
        contentView?.orientation = LinearLayout.VERTICAL
        val clp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)

        addView(contentView , clp)
    }

    private fun initTags(){
        addTag(ImageTag())
        addTag(TextTag())
    }

    /**
     * 添加tag
     */
    fun addTag(tag: Tag){
        val oldTag = tags.find { it.tag() == tag.tag() }
        if (oldTag != null){
            tags.remove(oldTag)
        }
        tags.add(tag)
    }


    /**
     * 分析文本 将对应的文本放入容器当中
     */
    private fun parseText(){

        //先移除 //todo 做缓存
        contentView?.removeAllViews()

        //解析文本
        val p = XMLParser(this)
        p.parser(text)
        val views = p.getViews()
        views.forEach {
            //如果没有布局参数默认为包裹
            if (it.view.layoutParams == null){
                it.view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT)
            }
            //如果是latexView 默认为MathView 等宽
            if (it.view is JLatexView){
                latexViews.add(it.view)
            }
            contentView?.addView(it.view)
        }

    }

    override fun buildView(tag: String, attrs: Map<String, String>, text: String?): ViewModel {
        tags.find { it.tag() == tag  }?.let {
            val view = it.buildView(contentView!! , attrs , text)
            return ViewModel(view , text)
        }
        throw IllegalStateException("没有实现改标签 tag = $tag" )
    }


    /**
     * 重写计算方法
     *
     * 需要从新计算思路
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val pWidth = MeasureSpec.getSize(widthMeasureSpec)
        latexViews.forEach { it.mathViewWidth = pWidth }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (Math.abs(this.computeHorizontalScrollRange() - width) < 5) {
            return false
        }
        val range = scrollX + width
        when {
            scrollX <= 1 -> //在最左边
                parent.requestDisallowInterceptTouchEvent(false)
            Math.abs(this.computeHorizontalScrollRange() - range) <= 1 -> //在最右边
                parent.requestDisallowInterceptTouchEvent(false)
            else -> parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.onTouchEvent(ev)
    }
}

/**
 *
 */
class ContentView(context: Context) : LinearLayout(context){

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}