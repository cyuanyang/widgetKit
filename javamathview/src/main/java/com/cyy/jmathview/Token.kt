package com.cyy.jmathview


/**
 * Created by study on 17/10/20.
 *
 */

interface Token{



}

/**
 * latex的标志类
 *
 *
 */
internal class LatexToken(latex:String , start:Int , end:Int): Token {

    //latex string
    var latex = latex
    //在字符串中的开始，结束位置 由此可以确定latex在字符串中的位置
    var start = start
    var end = end



}
