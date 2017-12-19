package com.cyy.jmathview.tools

import android.util.SparseArray
import java.util.regex.Pattern

/**
 * Created by study on 17/12/19.
 *
 */

class LatexHepler{

    private val FORMULA_REG = Pattern.compile("([<>&])")

    /**
     * 返回解析latex的标签
     */
    fun getText(tex:String , token:String):String{
        return "<"+token+">" + dealSpecalCahr(tex) +"</"+token+">"

    }

    //匹配 <  >  &
    fun dealSpecalCahr(s: String): String {
        val sb = StringBuffer(s)
        val starts = SparseArray<String>()
        val m = FORMULA_REG.matcher(s)
        while (m.find()) {
            val start = m.start()
            starts.append(start, m.group())
        }

        var addLength = 0
        for (i in 0 until starts.size()) {
            val start = starts.keyAt(i)
            val sign = starts.get(start)
            val replace = StringBuilder("<![CDATA[").append(sign).append("]]>").toString() //StringUtils.join("<![CDATA[", sign, "]]>")
            sb.replace(start + addLength, start + addLength + 1, replace)
            addLength += replace.length - 1
        }
        return sb.toString()
    }

}