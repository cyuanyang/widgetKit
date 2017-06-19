package com.cyy.mathview_mathjax;

/**
 * Created by study on 17/6/8.
 *
 * 数学渲染的回调
 */

public interface MathRenderInterface {
    //暂时没有实现
    void beginRender(MathView mathView); //开始渲染
    void endRender(MathView mathView); //渲染完毕
}
